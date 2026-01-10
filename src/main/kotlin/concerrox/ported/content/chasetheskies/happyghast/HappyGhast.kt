//package concerrox.ported.content.chasetheskies.happyghast
//
//import com.mojang.serialization.Dynamic
//import net.minecraft.core.BlockPos
//import net.minecraft.nbt.CompoundTag
//import net.minecraft.network.syncher.EntityDataSerializers
//import net.minecraft.network.syncher.SynchedEntityData
//import net.minecraft.server.level.ServerLevel
//import net.minecraft.sounds.SoundEvents
//import net.minecraft.sounds.SoundSource
//import net.minecraft.tags.ItemTags
//import net.minecraft.util.Mth
//import net.minecraft.world.InteractionHand
//import net.minecraft.world.InteractionResult
//import net.minecraft.world.damagesource.DamageSource
//import net.minecraft.world.entity.*
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier
//import net.minecraft.world.entity.ai.attributes.Attributes
//import net.minecraft.world.entity.ai.control.BodyRotationControl
//import net.minecraft.world.entity.ai.control.FlyingMoveControl
//import net.minecraft.world.entity.ai.control.LookControl
//import net.minecraft.world.entity.ai.goal.FloatGoal
//import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
//import net.minecraft.world.entity.ai.navigation.PathNavigation
//import net.minecraft.world.entity.animal.Animal
//import net.minecraft.world.entity.monster.Ghast
//import net.minecraft.world.entity.monster.Ghast.GhastMoveControl
//import net.minecraft.world.entity.monster.Ghast.RandomFloatAroundGoal
//import net.minecraft.world.entity.player.Player
//import net.minecraft.world.item.ItemStack
//import net.minecraft.world.level.Level
//import net.minecraft.world.level.LevelReader
//import net.minecraft.world.level.biome.Biome
//import net.minecraft.world.level.block.state.BlockState
//import net.minecraft.world.phys.AABB
//import net.minecraft.world.phys.Vec2
//import net.minecraft.world.phys.Vec3
//import kotlin.math.min
//
//class HappyGhast(type: EntityType<out HappyGhast>, level: Level) : Animal(type, level) {
//
//    private var leashHolderTime = 0
//    private var serverStillTimeout = 0
//
//    init {
//        moveControl = GhastMoveControl(this, true, { isOnStillTimeout })
//        lookControl = HappyGhastLookControl()
//    }
//
//    private fun setServerStillTimeout(serverStillTimeout: Int) {
//        val level = level()
//        if (this.serverStillTimeout <= 0 && serverStillTimeout > 0 && level is ServerLevel) {
//            syncPacketPositionCodec(x, y, z)
//            level.chunkSource.chunkMap.sendToTrackingPlayers(this, ClientboundEntityPositionSyncPacket.of(this))
//        }
//        this.serverStillTimeout = serverStillTimeout
//        syncStayStillFlag()
//    }
//
//    private fun createBabyNavigation(level: Level): PathNavigation {
//        return BabyFlyingPathNavigation(this, level)
//    }
//
//    override fun registerGoals() {
//        goalSelector.addGoal(3, HappyGhastFloatGoal())
//        goalSelector.addGoal(
//            4, ForNonPathfinders(
//                this, 1.0, { p_478656_ ->
//                    if (!isWearingBodyArmor && !isBaby) p_478656_.`is`(ItemTags.HAPPY_GHAST_TEMPT_ITEMS)
//                    else p_478656_.`is`(ItemTags.HAPPY_GHAST_FOOD)
//                }, false, 7.0
//            )
//        )
//        goalSelector.addGoal(5, RandomFloatAroundGoal(this, WANDER_GROUND_DISTANCE))
//    }
//
//    private fun adultGhastSetup() {
//        moveControl = GhastMoveControl(this, true, { isOnStillTimeout })
//        lookControl = HappyGhastLookControl()
//        navigation = createNavigation(level())
//        val level = level()
//        if (level is ServerLevel) {
//            removeAllGoals { _ -> true }
//            registerGoals()
//            brain.stopAll(level, this)
//            brain.clearMemories()
//        }
//    }
//
//    private fun babyGhastSetup() {
//        moveControl = FlyingMoveControl(this, 180, true)
//        lookControl = LookControl(this)
//        navigation = createBabyNavigation(level())
//        setServerStillTimeout(0)
//        removeAllGoals { _ -> true }
//    }
//
//    override fun ageBoundaryReached() {
//        if (isBaby) babyGhastSetup() else adultGhastSetup()
//        super.ageBoundaryReached()
//    }
//
//    override fun sanitizeScale(size: Float) = min(size, MAX_SCALE)
//    override fun checkFallDamage(y: Double, onGround: Boolean, state: BlockState, pos: BlockPos) {}
//    override fun onClimbable() = false
//
//    override fun travel(travelVector: Vec3) {
//        val v = getAttributeValue(Attributes.FLYING_SPEED).toFloat() * 5f / 3f
//        travelFlying(travelVector, v, v, v)
//    }
//
//    override fun getWalkTargetValue(pos: BlockPos, level: LevelReader) = if (!level.isEmptyBlock(pos)) {
//        0f
//    } else if (level.isEmptyBlock(pos.below()) && !level.isEmptyBlock(pos.below(2))) {
//        10f
//    } else {
//        5f
//    }
//
//    override fun canBreatheUnderwater(): Boolean {
//        return if (isBaby) true else super.canBreatheUnderwater()
//    }
//
//    override fun shouldStayCloseToLeashHolder() = false
//    override fun playStepSound(pos: BlockPos, state: BlockState) {}
//    override fun getVoicePitch() = 1f
//    override fun getSoundSource() = SoundSource.NEUTRAL
//
//    override fun getAmbientSoundInterval(): Int {
//        val i = super.getAmbientSoundInterval()
//        return if (isVehicle) i * 6 else i
//    }
//
//    override fun getAmbientSound() = if (isBaby) SoundEvents.GHASTLING_AMBIENT else SoundEvents.HAPPY_GHAST_AMBIENT
//    override fun getHurtSound(source: DamageSource) =
//        if (isBaby) SoundEvents.GHASTLING_HURT else SoundEvents.HAPPY_GHAST_HURT
//
//    override fun getDeathSound() = if (isBaby) SoundEvents.GHASTLING_DEATH else SoundEvents.HAPPY_GHAST_DEATH
//    override fun getSoundVolume() = if (isBaby) 1f else 4f
//    override fun getMaxSpawnClusterSize() = 1
//
//    override fun getBreedOffspring(level: ServerLevel, other: AgeableMob): AgeableMob {
//        return EntityType.HAPPY_GHAST.create(level, EntitySpawnReason.BREEDING)
//    }
//
//    override fun canFallInLove() = false
//    override fun getAgeScale() = if (isBaby) BABY_SCALE else MAX_SCALE
//    override fun isFood(stack: ItemStack) = stack.`is`(ItemTags.HAPPY_GHAST_FOOD)
//
//    override fun canUseSlot(slot: EquipmentSlot): Boolean {
//        return if (slot != EquipmentSlot.BODY) super.canUseSlot(slot) else isAlive && !isBaby
//    }
//
//    override fun canDispenserEquipIntoSlot(slot: EquipmentSlot): Boolean {
//        return slot == EquipmentSlot.BODY
//    }
//
//    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
//        if (isBaby) {
//            return super.mobInteract(player, hand)
//        } else {
//            val stack = player.getItemInHand(hand)
//            if (!stack.isEmpty) {
//                val ret = stack.interactLivingEntity(player, this, hand)
//                if (ret.consumesAction()) return ret
//            }
//            if (isWearingBodyArmor && !player.isSecondaryUseActive) {
//                doPlayerRide(player)
//                return InteractionResult.SUCCESS
//            } else {
//                return super.mobInteract(player, hand)
//            }
//        }
//    }
//
//    private fun doPlayerRide(player: Player) {
//        if (!level().isClientSide) player.startRiding(this)
//    }
//
//    override fun addPassenger(entity: Entity) {
//        if (!isVehicle) {
//            level().playSound(null, x, y, z, SoundEvents.HARNESS_GOGGLES_DOWN, getSoundSource(), 1f, 1f)
//        }
//        super.addPassenger(entity)
//        if (!level().isClientSide) {
//            if (!scanPlayerAboveGhast()) {
//                setServerStillTimeout(0)
//            } else if (serverStillTimeout > MAX_STILL_TIMEOUT) {
//                setServerStillTimeout(MAX_STILL_TIMEOUT)
//            }
//        }
//    }
//
//    override fun removePassenger(entity: Entity) {
//        super.removePassenger(entity)
//        if (!level().isClientSide) setServerStillTimeout(MAX_STILL_TIMEOUT)
//
//        if (!isVehicle) {
//            clearHome()
//            level().playSound(null, x, y, z, SoundEvents.HARNESS_GOGGLES_UP, getSoundSource(), 1f, 1f)
//        }
//    }
//
//    override fun canAddPassenger(entity: Entity) = passengers.size < MAX_PASSENGERS
//
//    override fun getControllingPassenger(): LivingEntity? {
//        val firstPassenger = firstPassenger
//        return if (isWearingBodyArmor && !isOnStillTimeout && firstPassenger is Player) {
//            firstPassenger
//        } else {
//            super.getControllingPassenger()
//        }
//    }
//
//    override fun getRiddenInput(player: Player, travelVector: Vec3): Vec3 {
//        val xxa = player.xxa
//        var f1 = 0f
//        var f2 = 0f
//        if (player.zza != 0f) {
//            var f3 = Mth.cos(player.xRot * (Math.PI / 180.0).toFloat())
//            var f4 = -Mth.sin(player.xRot * (Math.PI / 180.0).toFloat())
//            if (player.zza < 0f) {
//                f3 *= -0.5f
//                f4 *= -0.5f
//            }
//            f2 = f4
//            f1 = f3
//        }
//        if (player.isJumping()) {
//            f2 += 0.5f
//        }
//        return Vec3(
//            xxa.toDouble(), f2.toDouble(), f1.toDouble()
//        ).scale(3.9f * getAttributeValue(Attributes.FLYING_SPEED))
//    }
//
//    private fun getRiddenRotation(entity: LivingEntity): Vec2 {
//        return Vec2(entity.xRot * 0.5f, entity.yRot)
//    }
//
//    override fun tickRidden(player: Player, travelVector: Vec3) {
//        super.tickRidden(player, travelVector)
//        val rot = getRiddenRotation(player)
//        var yRotation = yRot
//        yRotation += Mth.wrapDegrees(rot.y - yRotation) * 0.08f
//        setRot(yRotation, rot.x)
//        yHeadRot = yRotation
//        yBodyRot = yHeadRot
//        yRotO = yBodyRot
//    }
//
//    override fun brainProvider() = HappyGhastAi.brainProvider()
//    override fun makeBrain(dynamic: Dynamic<*>) = HappyGhastAi.makeBrain(brainProvider().makeBrain(dynamic))
//
//    override fun customServerAiStep() {
//        val level = level()
//        if (isBaby && level is ServerLevel) {
//            brain.tick(level, this)
//            HappyGhastAi.updateActivity(this)
//        }
//        checkRestriction()
//        super.customServerAiStep()
//    }
//
//    override fun tick() {
//        super.tick()
//        if (!level().isClientSide) {
//            if (leashHolderTime > 0) leashHolderTime--
//            isLeashHolder = leashHolderTime > 0
//            if (serverStillTimeout > 0) {
//                if (tickCount > STILL_TIMEOUT_ON_LOAD_GRACE_PERIOD) serverStillTimeout--
//                setServerStillTimeout(serverStillTimeout)
//            }
//            if (scanPlayerAboveGhast()) setServerStillTimeout(MAX_STILL_TIMEOUT)
//        }
//    }
//
//    override fun aiStep() {
//        if (!level().isClientSide) {
//            setRequiresPrecisePosition(isOnStillTimeout)
//        }
//        super.aiStep()
//        continuousHeal()
//    }
//
//    private val happyGhastRestrictionRadius: Int
//        get() = if (!isBaby && getItemBySlot(EquipmentSlot.BODY).isEmpty) LARGE_RESTRICTION_RADIUS else SMALL_RESTRICTION_RADIUS
//
//    private fun checkRestriction() {
//        if (!isLeashed && !isVehicle) {
//            val r = happyGhastRestrictionRadius
//            if (!hasHome() || !getHomePosition().closerThan(
//                    blockPosition(), r + RESTRICTION_RADIUS_BUFFER
//                ) || r != getHomeRadius()
//            ) {
//                setHomeTo(blockPosition(), r)
//            }
//        }
//    }
//
//    private fun continuousHeal() {
//        val level = level()
//        if (level is ServerLevel && isAlive && deathTime == 0 && maxHealth != health) {
//            val canHeal = isInClouds() || level.precipitationAt(blockPosition()) != Biome.Precipitation.NONE
//            if (tickCount % (if (canHeal) FAST_HEALING_TICKS else SLOW_HEALING_TICKS) == 0) heal(1f)
//        }
//    }
//
//    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
//        super.defineSynchedData(builder)
//        builder.define(IS_LEASH_HOLDER, false)
//        builder.define(STAYS_STILL, false)
//    }
//
//    val staysStill: Boolean get() = entityData.get(STAYS_STILL)
//    var isLeashHolder: Boolean
//        get() = entityData.get(IS_LEASH_HOLDER)
//        private set(leashHolder) {
//            entityData.set(IS_LEASH_HOLDER, leashHolder)
//        }
//
//    private fun syncStayStillFlag() {
//        entityData.set(STAYS_STILL, serverStillTimeout > 0)
//    }
//
//    override fun supportQuadLeashAsHolder(): Boolean {
//        return true
//    }
//
//    val quadLeashHolderOffsets: Array<Vec3>
//        get() = Leashable.createQuadLeashOffsets(this, -0.03125, 0.4375, 0.46875, 0.03125)
//
//    public override fun getLeashOffset(): Vec3 {
//        return Vec3.ZERO
//    }
//
//    public override fun leashElasticDistance(): Double {
//        return 10.0
//    }
//
//    public override fun leashSnapDistance(): Double {
//        return 16.0
//    }
//
//    public override fun onElasticLeashPull() {
//        super.onElasticLeashPull()
//        getMoveControl().setWait()
//    }
//
//    public override fun notifyLeashHolder(leashable: Leashable) {
//        if (leashable.supportQuadLeash()) {
//            this.leashHolderTime = 5
//        }
//    }
//
//    override fun addAdditionalSaveData(tag: CompoundTag) {
//        super.addAdditionalSaveData(tag)
//        tag.putInt("still_timeout", serverStillTimeout)
//    }
//
//    override fun readAdditionalSaveData(tag: CompoundTag) {
//        super.readAdditionalSaveData(tag)
//        setServerStillTimeout(tag.getInt("still_timeout"))
//    }
//
//    val isOnStillTimeout: Boolean get() = staysStill() || serverStillTimeout > 0
//
//    private fun scanPlayerAboveGhast(): Boolean {
//        val aabb = boundingBox
//        val aabb1 = AABB(
//            aabb.minX - 1.0,
//            aabb.maxY - 1.0E-5f,
//            aabb.minZ - 1.0,
//            aabb.maxX + 1.0,
//            aabb.maxY + aabb.ysize / 2.0,
//            aabb.maxZ + 1.0
//        )
//        for (player in level().players()) {
//            if (!player.isSpectator) {
//                val entity = player.rootVehicle
//                if (entity !is HappyGhast && aabb1.contains(entity.position())) return true
//            }
//        }
//        return false
//    }
//
//    override fun createBodyControl(): BodyRotationControl = HappyGhastBodyRotationControl()
//    override fun getDismountLocationForPassenger(entity: LivingEntity) = Vec3(x, boundingBox.maxY, z)
//
//    override fun canBeCollidedWith(entity: Entity?): Boolean {
//        return if (!isBaby && isAlive) {
//            if (level().isClientSide && entity is Player && entity.position().y >= boundingBox.maxY) {
//                true
//            } else if (isVehicle && entity is HappyGhast) {
//                true
//            } else {
//                isOnStillTimeout
//            }
//        } else {
//            false
//        }
//    }
//
//    internal class BabyFlyingPathNavigation(happyGhast: HappyGhast, level: Level) :
//        FlyingPathNavigation(happyGhast, level) {
//
//        init {
//            setCanOpenDoors(false)
//            setCanFloat(true)
//            setRequiredPathLength(48f)
//        }
//
//        override fun canMoveDirectly(v1: Vec3, v2: Vec3) = isClearForMovementBetween(mob, v1, v2, false)
//
//    }
//
//    internal inner class HappyGhastBodyRotationControl : BodyRotationControl(this@HappyGhast) {
//        override fun clientTick() {
//            if (this@HappyGhast.isVehicle) {
//                this@HappyGhast.yHeadRot = this@HappyGhast.yRot
//                this@HappyGhast.yBodyRot = this@HappyGhast.yHeadRot
//            }
//            super.clientTick()
//        }
//    }
//
//    internal inner class HappyGhastFloatGoal : FloatGoal(this@HappyGhast) {
//        override fun canUse() = !this@HappyGhast.isOnStillTimeout && super.canUse()
//    }
//
//    internal inner class HappyGhastLookControl : LookControl(this@HappyGhast) {
//
//        override fun tick() {
//            if (this@HappyGhast.isOnStillTimeout) {
//                val f = wrapDegrees90(this@HappyGhast.yRot)
//                this@HappyGhast.yRot -= f
//                this@HappyGhast.setYHeadRot(this@HappyGhast.yRot)
//            } else if (this.lookAtCooldown > 0) {
//                this.lookAtCooldown--
//                val d0 = this.wantedX - this@HappyGhast.x
//                val d1 = this.wantedZ - this@HappyGhast.z
//                this@HappyGhast.yRot = -(Mth.atan2(d0, d1).toFloat()) * (180f / Math.PI.toFloat())
//                this@HappyGhast.yBodyRot = this@HappyGhast.yRot
//                this@HappyGhast.yHeadRot = this@HappyGhast.yBodyRot
//            } else {
//                Ghast.faceMovementDirection(this.mob)
//            }
//        }
//
//        private fun wrapDegrees90(degrees: Float): Float {
//            var f = degrees % 90f
//            if (f >= 45f) f -= 90f
//            if (f < -45f) f += 90f
//            return f
//        }
//
//    }
//
//    companion object {
//        const val BABY_SCALE = 0.2375f
//        const val WANDER_GROUND_DISTANCE = 16
//        const val SMALL_RESTRICTION_RADIUS = 32
//        const val LARGE_RESTRICTION_RADIUS = 64
//        const val RESTRICTION_RADIUS_BUFFER = 16
//        const val FAST_HEALING_TICKS = 20
//        const val SLOW_HEALING_TICKS = 600
//        const val MAX_PASSENGERS = 4
//        private const val STILL_TIMEOUT_ON_LOAD_GRACE_PERIOD = 60
//        private const val MAX_STILL_TIMEOUT = 10
//        private val IS_LEASH_HOLDER = SynchedEntityData.defineId(HappyGhast::class.java, EntityDataSerializers.BOOLEAN)
//        private val STAYS_STILL = SynchedEntityData.defineId(HappyGhast::class.java, EntityDataSerializers.BOOLEAN)
//        private const val MAX_SCALE = 1f
//
//        fun createAttributes(): AttributeSupplier.Builder {
//            return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.TEMPT_RANGE, 16.0)
//                .add(Attributes.FLYING_SPEED, 0.05).add(Attributes.MOVEMENT_SPEED, 0.05)
//                .add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.CAMERA_DISTANCE, 8.0)
//        }
//    }
//}