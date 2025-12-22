package concerrox.ported.content.thegardenawakens.creakingheart

import com.mojang.serialization.MapCodec
import concerrox.ported.registry.ModBlockEntityTypes
import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import java.util.function.BiConsumer

private val Player.preventsBlockDrops get() = abilities.instabuild
private val Explosion.BlockInteraction.shouldAffectBlocklikeEntities: Boolean
    get() = this == Explosion.BlockInteraction.DESTROY || this == Explosion.BlockInteraction.DESTROY_WITH_DECAY

class CreakingHeartBlock(properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(
            defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(STATE, CreakingHeartState.UPROOTED)
                .setValue(NATURAL, false)
        )
    }

    private fun blockEntity(level: Level, pos: BlockPos) = level.getBlockEntity(pos) as? CreakingHeartBlockEntity

    override fun codec(): MapCodec<CreakingHeartBlock> = CODEC
    override fun getRenderShape(state: BlockState) = RenderShape.MODEL
    override fun newBlockEntity(pos: BlockPos, state: BlockState) = CreakingHeartBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(
        level: Level, state: BlockState, serverType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else if (state.getValue(STATE) != CreakingHeartState.UPROOTED) {
            createTickerHelper(
                serverType, ModBlockEntityTypes.CREAKING_HEART.get(), CreakingHeartBlockEntity.Companion::serverTick
            )
        } else null
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        if (isNaturalNight(level) && state.getValue(STATE) != CreakingHeartState.UPROOTED && random.nextInt(16) == 0 && isSurroundedByLogs(
                level, pos
            )
        ) {
            level.playLocalSound(
                pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                ModSoundEvents.CREAKING_HEART_IDLE, SoundSource.BLOCKS,
                1f, 1f, false,
            )
        }
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        level.scheduleTick(pos, this, 1)
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val newState = updateState(state, level, pos)
        if (newState !== state) level.setBlock(pos, newState, 3)
    }

    override fun getStateForPlacement(context: BlockPlaceContext) =
        updateState(defaultBlockState().setValue(AXIS, context.clickedFace.axis), context.level, context.clickedPos)

    override fun rotate(state: BlockState, rotation: Rotation): BlockState =
        RotatedPillarBlock.rotatePillar(state, rotation)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AXIS, STATE, NATURAL)
    }

    override fun onRemove(
        state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean
    ) {
        level.updateNeighbourForOutputSignal(pos, state.block)
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun onExplosionHit(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        explosion: Explosion,
        dropConsumer: BiConsumer<ItemStack?, BlockPos?>
    ) {
        blockEntity(level, pos)?.let {
            if (!level.isClientSide && explosion.blockInteraction.shouldAffectBlocklikeEntities) {
                it.removeProtector(explosion.damageSource)
                val entity = explosion.indirectSourceEntity
                if (entity is Player) tryAwardExperience(entity, state, level, pos)
            }
        }
        super.onExplosionHit(state, level, pos, explosion, dropConsumer)
    }

    override fun playerWillDestroy(level: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        blockEntity(level, pos)?.let {
            it.removeProtector(player.damageSources().playerAttack(player))
            tryAwardExperience(player, state, level, pos)
        }
        return super.playerWillDestroy(level, pos, state, player)
    }

    private fun tryAwardExperience(player: Player, state: BlockState, level: Level, pos: BlockPos) {
        if (!player.preventsBlockDrops && !player.isSpectator && state.getValue(NATURAL) && level is ServerLevel) {
            popExperience(level, pos, level.random.nextIntBetweenInclusive(20, 24))
        }
    }

    override fun hasAnalogOutputSignal(state: BlockState) = true

    override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int {
        return if (state.getValue(STATE) === CreakingHeartState.UPROOTED) {
            0
        } else {
            blockEntity(level, pos)?.analogOutputSignal ?: 0
        }
    }

    companion object {
        val CODEC: MapCodec<CreakingHeartBlock> = simpleCodec(::CreakingHeartBlock)
        val AXIS: EnumProperty<Direction.Axis> = BlockStateProperties.AXIS
        val STATE: EnumProperty<CreakingHeartState> =
            EnumProperty.create("creaking_heart_state", CreakingHeartState::class.java)
        val NATURAL: BooleanProperty = BooleanProperty.create("natural")

        fun isNaturalNight(level: Level): Boolean {
            if (!level.dimensionType().natural()) {
                return false
            } else {
                val i = (level.dayTime % 24000L).toInt()
                return i in 12600..23400
            }
        }

        private fun updateState(state: BlockState, level: Level, pos: BlockPos): BlockState {
            val hasRequiredLogs = hasRequiredLogs(state, level, pos)
            val isUprooted = state.getValue(STATE) == CreakingHeartState.UPROOTED
            return if (hasRequiredLogs && isUprooted) {
                state.setValue(
                    STATE, if (isNaturalNight(level)) CreakingHeartState.AWAKE else CreakingHeartState.DORMANT
                )
            } else state
        }

        private val Direction.Axis.directions
            get() = when (this) {
                Direction.Axis.X -> arrayOf(Direction.EAST, Direction.WEST)
                Direction.Axis.Y -> arrayOf(Direction.UP, Direction.DOWN)
                Direction.Axis.Z -> arrayOf(Direction.SOUTH, Direction.NORTH)
            }

        fun hasRequiredLogs(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
            val axis = state.getValue(AXIS)
            for (direction in axis.directions) {
                val relState = level.getBlockState(pos.relative(direction))
                if (!relState.`is`(ModBlockTags.PALE_OAK_LOGS) || relState.getValue(AXIS) != axis) return false
            }
            return true
        }

        private fun isSurroundedByLogs(level: LevelAccessor, pos: BlockPos): Boolean {
            for (direction in Direction.entries) {
                if (!level.getBlockState(pos.relative(direction)).`is`(ModBlockTags.PALE_OAK_LOGS)) return false
            }
            return true
        }

    }

}