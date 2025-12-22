package concerrox.ported.content.thegardenawakens.creaking

import com.mojang.serialization.Dynamic
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlock
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlockEntity
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartState
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModItemTags
import concerrox.ported.registry.ModParticleTypes
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.game.DebugPackets
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.BodyRotationControl
import net.minecraft.world.entity.ai.control.JumpControl
import net.minecraft.world.entity.ai.control.LookControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.pathfinder.PathFinder
import net.minecraft.world.level.pathfinder.PathType
import net.minecraft.world.level.pathfinder.PathfindingContext
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import java.util.*
import java.util.function.BiPredicate
import kotlin.math.min

class Creaking(type: EntityType<out Creaking>, level: Level) : Monster(type, level) {

    private var attackAnimationRemainingTicks = 0
    val attackAnimationState = AnimationState()
    val invulnerabilityAnimationState = AnimationState()
    val deathAnimationState = AnimationState()
    private var invulnerabilityAnimationRemainingTicks = 0
    var eyesGlowing = false
    private var nextFlickerTime = 0
    private var playerStuckCounter = 0
    val isHeartBound: Boolean get() = homePos != null

    init {
        lookControl = this.CreakingLookControl(this)
        moveControl = this.CreakingMoveControl(this)
        jumpControl = this.CreakingJumpControl(this)
        xpReward = 0
        val groundPathNavigation = getNavigation()
        groundPathNavigation.setCanFloat(true)
    }

    fun setTransient(homePos: BlockPos) {
        this.homePos = homePos
        setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0f)
        setPathfindingMalus(PathType.POWDER_SNOW, 8.0f)
        setPathfindingMalus(PathType.LAVA, 8.0f)
        setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0f)
        setPathfindingMalus(PathType.DANGER_FIRE, 0.0f)
    }

    override fun createBodyControl() = this.CreakingBodyRotationControl(this)
    override fun brainProvider() = CreakingAi.brainProvider()
    override fun makeBrain(dynamic: Dynamic<*>) = CreakingAi.makeBrain(this, brainProvider().makeBrain(dynamic))

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(CAN_MOVE, true)
        builder.define(IS_ACTIVE, false)
        builder.define(IS_TEARING_DOWN, false)
        builder.define(HOME_POS, Optional.empty())
    }

    fun canMove(): Boolean = entityData.get(CAN_MOVE)

    override fun doHurtTarget(entity: Entity): Boolean {
        return if (entity !is LivingEntity) {
            false
        } else {
            attackAnimationRemainingTicks = 15
            level().broadcastEntityEvent(this, 4.toByte())
            super.doHurtTarget(entity)
        }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (level().isClientSide) return super.hurt(source, amount)

        val homePos = this@Creaking.homePos
        if (homePos != null && !source.`is`(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (!isInvulnerableTo(source) && invulnerabilityAnimationRemainingTicks <= 0 && !isDeadOrDying) {
                val player = blameSourceForDamage(source)
                val entity = source.directEntity
                if (entity !is LivingEntity && entity !is Projectile && player == null) {
                    return false
                } else {
                    invulnerabilityAnimationRemainingTicks = 8
                    level().broadcastEntityEvent(this, 66.toByte())
                    gameEvent(GameEvent.ENTITY_ACTION)
                    val blockEntity = level().getBlockEntity(homePos)
                    if (blockEntity is CreakingHeartBlockEntity) {
                        if (blockEntity.isProtector(this)) {
                            if (player != null) blockEntity.creakingHurt()
                            playHurtSound(source)
                        }
                    }
                    return true
                }
            } else {
                return false
            }
        } else {
            return super.hurt(source, amount)
        }
    }

    fun blameSourceForDamage(damageSource: DamageSource): Player? {
        resolveMobResponsibleForDamage(damageSource)
        return resolvePlayerResponsibleForDamage(damageSource)
    }

    private fun resolveMobResponsibleForDamage(damageSource: DamageSource) {
        val entity = damageSource.entity
        if (entity is LivingEntity) {
            if (!damageSource.`is`(DamageTypeTags.NO_ANGER) && (!damageSource.`is`(DamageTypes.WIND_CHARGE) || !type.`is`(
                    EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE
                ))
            ) {
                setLastHurtMob(entity)
            }
        }
    }

    private fun resolvePlayerResponsibleForDamage(damageSource: DamageSource): Player? {
        val entity = damageSource.entity
        if (entity is Player) {
            setLastHurtByPlayer(entity)
        } else if (entity is TamableAnimal && entity.isTame) {
            val ownerChecked = entity.owner
            if (ownerChecked != null && ownerChecked is Player) {
                setLastHurtByPlayer(ownerChecked)
            } else {
                setLastHurtByPlayer(null)
            }
        }
        return lastHurtByPlayer
    }

    override fun isPushable() = super.isPushable() && canMove()

    override fun push(x: Double, y: Double, z: Double) {
        if (canMove()) super.push(x, y, z)
    }

    @Suppress("UNCHECKED_CAST")
    override fun customServerAiStep() {
        val profilerFiller = level().profiler
        profilerFiller.push("creakingBrain")
        (getBrain() as Brain<Creaking>).tick(level() as ServerLevel, this)
        profilerFiller.pop()
        CreakingAi.updateActivity(this)
    }

    override fun aiStep() {
        if (invulnerabilityAnimationRemainingTicks > 0) --invulnerabilityAnimationRemainingTicks
        if (attackAnimationRemainingTicks > 0) --attackAnimationRemainingTicks

        if (!level().isClientSide) {
            val canMove = entityData.get(CAN_MOVE)
            val checked = checkCanMove()
            if (checked != canMove) {
                gameEvent(GameEvent.ENTITY_ACTION)
                if (checked) {
                    makeSound(ModSoundEvents.CREAKING_UNFREEZE)
                } else {
                    stopInPlace()
                    makeSound(ModSoundEvents.CREAKING_FREEZE)
                }
            }
            entityData.set(CAN_MOVE, checked)
        }
        super.aiStep()
    }

    override fun tick() {
        if (!level().isClientSide) homePos?.let {
            var hasProtector = false
//                label21@{
            val blockEntity = level().getBlockEntity(it)
            if (blockEntity is CreakingHeartBlockEntity) {
                if (blockEntity.isProtector(this)) {
                    hasProtector = true
//                        break@label21
                }
            }
//                }
            if (!hasProtector) health = 0f
        }
        super.tick()

        if (level().isClientSide) {
            setupAnimationStates()
            checkEyeBlink()
        }
    }

    override fun tickDeath() {
        if (isHeartBound && isTearingDown) {
            ++deathTime
            if (!level().isClientSide() && deathTime > 45 && !isRemoved) tearDown()
        } else {
            super.tickDeath()
        }
    }

    override fun updateWalkAnimation(partialTick: Float) {
        val f = min(partialTick * 25f, 3f)
        walkAnimation.update(f, 0.4f)
    }

    private fun setupAnimationStates() {
        attackAnimationState.animateWhen(attackAnimationRemainingTicks > 0, tickCount)
        invulnerabilityAnimationState.animateWhen(invulnerabilityAnimationRemainingTicks > 0, tickCount)
        deathAnimationState.animateWhen(isTearingDown, tickCount)
    }

    fun tearDown() {
        val level = level()
        if (level is ServerLevel) {
            val aabb = boundingBox
            val center = aabb.center
            val xSize = aabb.xsize * 0.3
            val ySize = aabb.ysize * 0.3
            val zSize = aabb.zsize * 0.3
            level.sendParticles(
                BlockParticleOption(ModParticleTypes.BLOCK_CRUMBLE.get(), ModBlocks.PALE_OAK_WOOD.get().defaultBlockState()),
                center.x, center.y, center.z, 100, xSize, ySize, zSize, 0.0,
            )
            level.sendParticles(
                BlockParticleOption(
                    ModParticleTypes.BLOCK_CRUMBLE.get(),
                    ModBlocks.CREAKING_HEART.get().defaultBlockState()
                        .setValue(CreakingHeartBlock.STATE, CreakingHeartState.AWAKE)
                ),
                center.x, center.y, center.z, 10, xSize, ySize, zSize, 0.0,
            )
        }

        makeSound(deathSound)
        remove(RemovalReason.DISCARDED)
    }

    fun creakingDeathEffects(damageSource: DamageSource) {
        blameSourceForDamage(damageSource)
        die(damageSource)
        makeSound(ModSoundEvents.CREAKING_TWITCH)
    }

    override fun handleEntityEvent(id: Byte) {
        if (id.toInt() == 66) {
            invulnerabilityAnimationRemainingTicks = 8
            playHurtSound(damageSources().generic())
        } else if (id.toInt() == 4) {
            attackAnimationRemainingTicks = 15
            playAttackSound()
        } else {
            super.handleEntityEvent(id)
        }
    }

    override fun fireImmune(): Boolean {
        return isHeartBound || super.fireImmune()
    }

    override fun canAddPassenger(entity: Entity): Boolean {
        return !isHeartBound && super.canAddPassenger(entity)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun couldAcceptPassenger(): Boolean {
        return !isHeartBound && super.couldAcceptPassenger()
    }

    override fun addPassenger(entity: Entity) {
        check(!isHeartBound) { "Should never addPassenger without checking couldAcceptPassenger()" }
    }

    override fun canUsePortal(allowPassengers: Boolean): Boolean {
        return !isHeartBound && super.canUsePortal(allowPassengers)
    }

    override fun createNavigation(level: Level): PathNavigation {
        return this.CreakingPathNavigation(this, level)
    }

    fun playerIsStuckInYou(): Boolean {
        val list = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf<Player>())
        return if (list.isEmpty()) {
            playerStuckCounter = 0
            false
        } else {
            val aabb = boundingBox
            for (player in list) {
                if (aabb.contains(player.eyePosition)) {
                    ++playerStuckCounter
                    return this.playerStuckCounter > 4
                }
            }
            playerStuckCounter = 0
            false
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        compound.get("home_pos")?.let {
            setTransient(BlockPos.CODEC.decode(NbtOps.INSTANCE, it).result().get().first)
        }
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.put("home_pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, homePos).result().get())
    }

    var homePos: BlockPos?
        get() = entityData.get(HOME_POS).orElse(null)
        set(value) {
            entityData.set(HOME_POS, Optional.of(value!!))
        }

    var isTearingDown: Boolean
        get() = entityData.get(IS_TEARING_DOWN)
        set(value) {
            entityData.set(IS_TEARING_DOWN, value)
        }

    fun checkEyeBlink() {
        if (deathTime > nextFlickerTime) {
            nextFlickerTime = deathTime + getRandom().nextIntBetweenInclusive(
                if (eyesGlowing) 2 else deathTime / 4, if (eyesGlowing) 8 else deathTime / 2
            )
            eyesGlowing = !eyesGlowing
        }
    }

    override fun playAttackSound() {
        makeSound(ModSoundEvents.CREAKING_ATTACK)
    }

    override fun getAmbientSound(): SoundEvent? {
        return if (isActive) null else ModSoundEvents.CREAKING_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return if (isHeartBound) ModSoundEvents.CREAKING_SWAY else super.getHurtSound(source)!!
    }

    override fun getDeathSound(): SoundEvent {
        return ModSoundEvents.CREAKING_DEATH
    }

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(ModSoundEvents.CREAKING_STEP, 0.15f, 1f)
    }

    override fun getTarget(): LivingEntity? {
        return targetFromBrain
    }

    override fun sendDebugPackets() {
        super.sendDebugPackets()
        DebugPackets.sendEntityBrain(this)
    }

    override fun knockback(x: Double, y: Double, z: Double) {
        if (canMove()) super.knockback(x, y, z)
    }


    fun checkCanMove(): Boolean {
        val list = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf<Player>())
        val flag = isActive
        if (list.isEmpty()) {
            if (flag) deactivate()
            return true
        } else {
            var flag1 = false
            for (player in list) {
                if (canAttack(player) && !isAlliedTo(player)) {
                    flag1 = true
                    if ((!flag || PLAYER_NOT_WEARING_DISGUISE_ITEM_FOR_TARGET.test(
                            player, this
                        )) && isLookingAtMe(
                            player, 0.5, false, true, eyeY, y + 0.5 * scale, (eyeY + y) / 2.0
                        )
                    ) {
                        if (flag) {
                            return false
                        }
                        if (player.distanceToSqr(this) < 144.0) {
                            activate(player)
                            return false
                        }
                    }
                }
            }

            if (!flag1 && flag) {
                deactivate()
            }
            return true
        }
    }

    private fun isLookingAtMe(
        player: LivingEntity, tolerance: Double, scaleByDistance: Boolean, visual: Boolean, vararg yValues: Double
    ): Boolean {
        val vec3 = player.getViewVector(1f).normalize()
        for (d0 in yValues) {
            var vec31 = Vec3(this.x - player.x, d0 - player.eyeY, this.z - player.z)
            val d1 = vec31.length()
            vec31 = vec31.normalize()
            val d2 = vec3.dot(vec31)
            if (d2 > 1.0 - tolerance / (if (scaleByDistance) d1 else 1.0) && hasLineOfSight(
                    player,
                    this,
                    if (visual) ClipContext.Block.VISUAL else ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    d0
                )
            ) {
                return true
            }
        }
        return false
    }

    fun hasLineOfSight(
        entity: Entity, target: Entity, block: ClipContext.Block, fluid: ClipContext.Fluid, y: Double
    ): Boolean {
        if (target.level() !== entity.level()) {
            return false
        } else {
            val vec3 = Vec3(entity.x, entity.eyeY, entity.z)
            val vec31 = Vec3(target.x, y, target.z)
            return if (vec31.distanceTo(vec3) > 128.0) false else entity.level()
                .clip(ClipContext(vec3, vec31, block, fluid, entity)).type == HitResult.Type.MISS
        }
    }

    fun activate(player: Player?) {
        getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player)
        gameEvent(GameEvent.ENTITY_ACTION)
        makeSound(ModSoundEvents.CREAKING_ACTIVATE)
        isActive = true
    }

    fun deactivate() {
        getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET)
        gameEvent(GameEvent.ENTITY_ACTION)
        makeSound(ModSoundEvents.CREAKING_DEACTIVATE)
        isActive = false
    }

    var isActive: Boolean
        get() = entityData.get(IS_ACTIVE)
        set(value) {
            entityData.set(IS_ACTIVE, value)
        }

    override fun getWalkTargetValue(pos: BlockPos, reader: LevelReader) = 0f

    inner class CreakingBodyRotationControl(creaking: Creaking) : BodyRotationControl(creaking) {
        override fun clientTick() {
            if (this@Creaking.canMove()) super.clientTick()
        }
    }

    internal inner class CreakingJumpControl(creaking: Creaking) : JumpControl(creaking) {
        override fun tick() {
            if (this@Creaking.canMove()) {
                super.tick()
            } else {
                this@Creaking.setJumping(false)
            }
        }
    }

    internal inner class CreakingLookControl(creaking: Creaking) : LookControl(creaking) {
        override fun tick() {
            if (this@Creaking.canMove()) super.tick()
        }
    }

    internal inner class CreakingMoveControl(creaking: Creaking) : MoveControl(creaking) {
        override fun tick() {
            if (this@Creaking.canMove()) super.tick()
        }
    }

    internal inner class CreakingPathNavigation(creaking: Creaking, level: Level) :
        GroundPathNavigation(creaking, level) {

        override fun tick() {
            if (this@Creaking.canMove()) super.tick()
        }

        override fun createPathFinder(maxVisitedNodes: Int): PathFinder {
            val creaking = this@Creaking
            Objects.requireNonNull(creaking)
            nodeEvaluator = creaking.HomeNodeEvaluator()
            nodeEvaluator.setCanPassDoors(true)
            return PathFinder(nodeEvaluator, maxVisitedNodes)
        }

    }

    internal inner class HomeNodeEvaluator : WalkNodeEvaluator() {

        override fun getPathType(context: PathfindingContext, x: Int, y: Int, z: Int): PathType {
            val homePos = this@Creaking.homePos
            return if (homePos == null) {
                super.getPathType(context, x, y, z)
            } else {
                val dist = homePos.distSqr(Vec3i(x, y, z))
                if (dist > 1024 && dist >= homePos.distSqr(context.mobPosition())) {
                    PathType.BLOCKED
                } else super.getPathType(context, x, y, z)
            }
        }

//        companion object {
//            private const val MAX_DISTANCE_TO_HOME_SQ = 1024
//        }

    }

    companion object {
        private val CAN_MOVE = SynchedEntityData.defineId(Creaking::class.java, EntityDataSerializers.BOOLEAN)
        private val IS_ACTIVE = SynchedEntityData.defineId(Creaking::class.java, EntityDataSerializers.BOOLEAN)
        private val IS_TEARING_DOWN = SynchedEntityData.defineId(Creaking::class.java, EntityDataSerializers.BOOLEAN)
        private val HOME_POS =
            SynchedEntityData.defineId(Creaking::class.java, EntityDataSerializers.OPTIONAL_BLOCK_POS)

        val PLAYER_NOT_WEARING_DISGUISE_ITEM_FOR_TARGET = BiPredicate { entity: LivingEntity, _: LivingEntity ->
            return@BiPredicate if (entity is Player) !isGazeDisguise(entity.getItemBySlot(EquipmentSlot.HEAD))
            else true
        }

        fun isGazeDisguise(stack: ItemStack) = stack.`is`(ModItemTags.GAZE_DISGUISE_EQUIPMENT)
//        private const val ATTACK_ANIMATION_DURATION = 15
//        private const val MAX_HEALTH = 1
//        private const val ATTACK_DAMAGE = 3.0f
//        private const val FOLLOW_RANGE = 32.0f
//        private const val ACTIVATION_RANGE_SQ = 144.0f
//        const val ATTACK_INTERVAL: Int = 40
//        private const val MOVEMENT_SPEED_WHEN_FIGHTING = 0.4f
//        const val SPEED_MULTIPLIER_WHEN_IDLING: Float = 0.3f
//        const val CREAKING_ORANGE: Int = 16545810
//        const val CREAKING_GRAY: Int = 6250335
//        const val INVULNERABILITY_ANIMATION_DURATION: Int = 8
//        const val TWITCH_DEATH_DURATION: Int = 45
//        private const val MAX_PLAYER_STUCK_COUNTER = 4

        fun createAttributes(): AttributeSupplier.Builder =
            createMonsterAttributes().add(Attributes.MAX_HEALTH, 1.0).add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.STEP_HEIGHT, 1.0625)

    }
}