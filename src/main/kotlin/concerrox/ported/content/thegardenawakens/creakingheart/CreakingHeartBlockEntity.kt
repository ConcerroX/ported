package concerrox.ported.content.thegardenawakens.creakingheart

import com.mojang.datafixers.util.Either
import concerrox.ported.content.thegardenawakens.creaking.Creaking
import concerrox.ported.content.thegardenawakens.creaking.particle.TrailParticleOption
import concerrox.ported.registry.*
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.SpawnUtil
import net.minecraft.world.Difficulty
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import org.apache.commons.lang3.mutable.MutableObject
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

class CreakingHeartBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntityTypes.CREAKING_HEART.get(), pos, state) {

    private var creakingInfo: Either<Creaking, UUID>? = null
    private var ticksExisted = 0L
    private var ticker = 0
    private var emitter = 0
    private var emitterTarget: Vec3? = null

    var analogOutputSignal = 0

    private fun distanceToCreaking(): Double {
        return getCreakingProtector().map { sqrt(it.distanceToSqr(Vec3.atBottomCenterOf(blockPos))) }.orElse(0.0)
    }

    private fun clearCreakingInfo() {
        creakingInfo = null
        setChanged()
    }

    fun setCreakingInfo(creaking: Creaking) {
        creakingInfo = Either.left(creaking)
        setChanged()
    }

    fun setCreakingInfo(creakingUuid: UUID) {
        creakingInfo = Either.right(creakingUuid)
        ticksExisted = 0L
        setChanged()
    }

    fun getCreakingProtector(): Optional<Creaking> {
        val info = creakingInfo
        if (info == null) {
            return NO_CREAKING
        } else {
            if (info.left().isPresent) {
                val creaking = info.left().get()
                if (!creaking.isRemoved) {
                    return Optional.of(creaking)
                }
                setCreakingInfo(creaking.getUUID())
            }

            val le = level
            if (le is ServerLevel) {
                if (creakingInfo!!.right().isPresent) {
                    val uuid = creakingInfo!!.right().get()
                    val creaking = le.getEntity(uuid)
                    if (creaking is Creaking) {
                        setCreakingInfo(creaking)
                        return Optional.of(creaking)
                    }
                    if (ticksExisted >= 30L) clearCreakingInfo()
                    return NO_CREAKING
                }
            }

            return NO_CREAKING
        }
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)
    override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag = saveCustomOnly(provider)

    fun creakingHurt() {
        val level = level
        val creaking = getCreakingProtector().orElse(null)
        if (creaking !is Creaking || emitter > 0) return
        if (level !is ServerLevel) return

        emitParticles(level, 20, false)
        if (blockState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
            repeat(level.getRandom().nextIntBetweenInclusive(2, 3)) {
                spreadResin().ifPresent { pos ->
                    level.playSound(null, pos, ModSoundEvents.RESIN_PLACE, SoundSource.BLOCKS, 1f, 1f)
                    level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(blockState))
                }
            }
        }
        emitter = 100
        emitterTarget = creaking.boundingBox.center
    }

    private fun spreadResin(): Optional<BlockPos> {
        val mutable = MutableObject<BlockPos>(null)
        val level = level ?: return Optional.ofNullable(mutable.getValue())

        BlockPos.breadthFirstTraversal(worldPosition, 2, 64, { pos, result ->
            for (dir in Util.shuffledCopy(Direction.entries.toTypedArray(), level.random)) {
                val relPos = pos.relative(dir)
                if (level.getBlockState(relPos).`is`(ModBlockTags.PALE_OAK_LOGS)) result.accept(relPos)
            }
        }, { pos ->
            if (!level.getBlockState(pos).`is`(ModBlockTags.PALE_OAK_LOGS)) return@breadthFirstTraversal true

            for (dir in Util.shuffledCopy(Direction.entries.toTypedArray(), level.random)) {
                val relPos = pos.relative(dir)
                var relState = level.getBlockState(relPos)
                val oppoDir = dir.opposite

                if (relState.isAir) {
                    relState = ModBlocks.RESIN_CLUMP.get().defaultBlockState()
                } else if (relState.`is`(Blocks.WATER) && relState.fluidState.isSource) {
                    relState =
                        ModBlocks.RESIN_CLUMP.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true)
                }

                if (relState.`is`(ModBlocks.RESIN_CLUMP) && !MultifaceBlock.hasFace(relState, oppoDir)) {
                    level.setBlock(relPos, relState.setValue(MultifaceBlock.getFaceProperty(oppoDir), true), 3)
                    mutable.value = relPos
                    return@breadthFirstTraversal false
                }
            }
            return@breadthFirstTraversal true
        })
        return Optional.ofNullable(mutable.getValue())
    }

    private fun emitParticles(level: ServerLevel, count: Int, reverseDirection: Boolean) {
        val random = level.random
        val creaking = getCreakingProtector().orElse(null)
        if (creaking !is Creaking) return

        val i = if (reverseDirection) 16545810 else 6250335
        repeat(count) {
            val aabb = creaking.boundingBox
            var creakingPos = aabb.minPosition.add(
                random.nextDouble() * aabb.xsize, random.nextDouble() * aabb.ysize, random.nextDouble() * aabb.zsize
            )
            var blockPos = Vec3.atLowerCornerOf(this@CreakingHeartBlockEntity.blockPos)
                .add(random.nextDouble(), random.nextDouble(), random.nextDouble())
            if (reverseDirection) {
                val temp = creakingPos
                creakingPos = blockPos
                blockPos = temp
            }

            val particle = TrailParticleOption(blockPos, i, random.nextInt(40) + 10)
            level.players().forEach {
                level.sendParticles(
                    it, particle, true, creakingPos.x, creakingPos.y, creakingPos.z, 1, 0.0, 0.0, 0.0, 0.0
                )
            }
        }
    }

//    fun preRemoveSideEffects(pos: BlockPos, p_393611_: BlockState) {
//        removeProtector(null)
//    }

    fun removeProtector(damageSource: DamageSource?) {
        val creaking = getCreakingProtector().orElse(null)
        if (creaking !is Creaking) return

        if (damageSource == null) {
            creaking.tearDown()
        } else {
            creaking.creakingDeathEffects(damageSource)
            creaking.isTearingDown = true
            creaking.health = 0f
        }
        clearCreakingInfo()
    }

    fun isProtector(creaking: Creaking): Boolean = getCreakingProtector().map { it === creaking }.orElse(false)

    fun computeAnalogOutputSignal(): Int {
        return if (creakingInfo != null && !this.getCreakingProtector().isEmpty) {
            15 - floor(Math.clamp(distanceToCreaking(), 0.0, 32.0) / 32 * 15).toInt()
        } else 0
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val c = tag.get("creaking")
        if (c == null) {
            clearCreakingInfo()
        } else {
            setCreakingInfo(UUIDUtil.CODEC.decode(NbtOps.INSTANCE, c).result().get().first)
        }
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        creakingInfo?.let {
            UUIDUtil.CODEC.encodeStart(
                NbtOps.INSTANCE, it.map(Creaking::getUUID) { uuid -> uuid }).ifSuccess { ret ->
                tag.put("creaking", ret)
            }
        }
    }

    companion object {
        //        private const val PLAYER_DETECTION_RANGE = 32
//        const val CREAKING_ROAMING_RADIUS: Int = 32
//        private const val DISTANCE_CREAKING_TOO_FAR = 34
//        private const val SPAWN_RANGE_XZ = 16
//        private const val SPAWN_RANGE_Y = 8
//        private const val ATTEMPTS_PER_SPAWN = 5
//        private const val UPDATE_TICKS = 20
//        private const val UPDATE_TICKS_VARIANCE = 5
//        private const val HURT_CALL_TOTAL_TICKS = 100
//        private const val NUMBER_OF_HURT_CALLS = 10
//        private const val HURT_CALL_INTERVAL = 10
//        private const val HURT_CALL_PARTICLE_TICKS = 50
//        private const val MAX_DEPTH = 2
//        private const val MAX_COUNT = 64
//        private const val TICKS_GRACE_PERIOD = 30
        private val NO_CREAKING: Optional<Creaking> = Optional.empty()

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, creakingHeart: CreakingHeartBlockEntity) {
            ++creakingHeart.ticksExisted

            if (level !is ServerLevel) return

            val ana = creakingHeart.computeAnalogOutputSignal()
            if (creakingHeart.analogOutputSignal != ana) {
                creakingHeart.analogOutputSignal = ana
                level.updateNeighbourForOutputSignal(pos, ModBlocks.CREAKING_HEART.get())
            }

            if (creakingHeart.emitter > 0) {
                if (creakingHeart.emitter > 50) {
                    creakingHeart.emitParticles(level, 1, true)
                    creakingHeart.emitParticles(level, 1, false)
                }

                if (creakingHeart.emitter % 10 == 0 && creakingHeart.emitterTarget != null) {
                    creakingHeart.getCreakingProtector().ifPresent {
                        creakingHeart.emitterTarget = it.boundingBox.center
                    }
                    val vec3 = Vec3.atCenterOf(pos)
                    val f = 0.2f + 0.8f * (100 - creakingHeart.emitter).toFloat() / 100f
                    val vec31 = vec3.subtract(creakingHeart.emitterTarget!!).scale(f.toDouble())
                        .add(creakingHeart.emitterTarget!!)
                    val blockPos = BlockPos.containing(vec31)
                    val f1 = creakingHeart.emitter / 2f / 100f + 0.5f
                    level.playSound(null, blockPos, ModSoundEvents.CREAKING_HEART_HURT, SoundSource.BLOCKS, f1, 1f)
                }
                --creakingHeart.emitter
            }

            if (creakingHeart.ticker-- < 0) {
                creakingHeart.ticker =
                    if (creakingHeart.level == null) 20 else creakingHeart.level!!.random.nextInt(5) + 20
                val newState = updateCreakingState(level, state, pos, creakingHeart)
                if (newState !== state) {
                    level.setBlock(pos, newState, 3)
                    if (newState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED) return
                }

                if (creakingHeart.creakingInfo == null) {
                    if (newState.getValue(CreakingHeartBlock.STATE) != CreakingHeartState.AWAKE) return
                    if (level.difficulty == Difficulty.PEACEFUL) return
                    if (!level.gameRules.getBoolean(GameRules.RULE_DOMOBSPAWNING)) return

                    level.getNearestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 32.0, false) ?: return
                    val newCreaking = spawnProtector(level, creakingHeart) ?: return

                    creakingHeart.setCreakingInfo(newCreaking)
                    newCreaking.makeSound(ModSoundEvents.CREAKING_SPAWN)
                    level.playSound(
                        null, creakingHeart.blockPos, ModSoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1f, 1f,
                    )
                } else creakingHeart.getCreakingProtector().ifPresent {
                    if (!CreakingHeartBlock.isNaturalNight(level) && !it.isPersistenceRequired || creakingHeart.distanceToCreaking() > 34 || it.playerIsStuckInYou()) {
                        creakingHeart.removeProtector(null)
                    }
                }
            }
        }

        private fun updateCreakingState(
            level: Level, state: BlockState, pos: BlockPos, creakingHeart: CreakingHeartBlockEntity
        ): BlockState {
            if (!CreakingHeartBlock.hasRequiredLogs(state, level, pos) && creakingHeart.creakingInfo == null) {
                return state.setValue(CreakingHeartBlock.STATE, CreakingHeartState.UPROOTED)
            } else {
                val flag: Boolean = CreakingHeartBlock.isNaturalNight(level)
                return state.setValue(
                    CreakingHeartBlock.STATE, if (flag) CreakingHeartState.AWAKE else CreakingHeartState.DORMANT
                )
            }
        }

        val ON_TOP_OF_COLLIDER_NO_LEAVES = SpawnUtil.Strategy { level, pos, state, pos2, state2 ->
            state2.getCollisionShape(
                level, pos2
            ).isEmpty && !state.`is`(BlockTags.LEAVES) && Block.isFaceFull(
                state.getCollisionShape(level, pos), Direction.UP
            )
        }

        private fun spawnProtector(level: ServerLevel, creakingHeart: CreakingHeartBlockEntity): Creaking? {
            val pos = creakingHeart.blockPos
            val protectorOrNull = SpawnUtil.trySpawnMob(
                ModEntityTypes.CREAKING.get(), MobSpawnType.SPAWNER, level, pos, 5, 16, 8, ON_TOP_OF_COLLIDER_NO_LEAVES
            )
            if (protectorOrNull.isEmpty) return null

            val creaking = protectorOrNull.get()
            level.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position())
            level.broadcastEntityEvent(creaking, 60.toByte())
            creaking.setTransient(pos)
            return creaking
        }
    }
}