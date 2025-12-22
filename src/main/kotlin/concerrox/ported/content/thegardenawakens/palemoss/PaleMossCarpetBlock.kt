package concerrox.ported.content.thegardenawakens.palemoss

import com.mojang.serialization.MapCodec
import concerrox.ported.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Plane
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.WallSide
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.BooleanSupplier

class PaleMossCarpetBlock(properties: Properties) : Block(properties), BonemealableBlock {

    companion object {

        val BASE: BooleanProperty = BlockStateProperties.BOTTOM
        val NORTH: EnumProperty<WallSide> = BlockStateProperties.NORTH_WALL
        val EAST: EnumProperty<WallSide> = BlockStateProperties.EAST_WALL
        val SOUTH: EnumProperty<WallSide> = BlockStateProperties.SOUTH_WALL
        val WEST: EnumProperty<WallSide> = BlockStateProperties.WEST_WALL

        val BASE_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)

        val WEST_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0)
        val EAST_SHAPE: VoxelShape = box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        val NORTH_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0)
        val SOUTH_SHAPE: VoxelShape = box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0)

        val WEST_LOW_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0)
        val EAST_LOW_SHAPE: VoxelShape = box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0)
        val NORTH_LOW_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 10.0, 1.0)
        val SOUTH_LOW_SHAPE: VoxelShape = box(0.0, 0.0, 15.0, 16.0, 10.0, 16.0)

        private val PROPERTY_BY_DIRECTION = mapOf(
            Direction.NORTH to NORTH, Direction.EAST to EAST, Direction.SOUTH to SOUTH, Direction.WEST to WEST
        )

        private fun getPropertyForFace(direction: Direction) = PROPERTY_BY_DIRECTION.getValue(direction)

        private fun hasFaces(state: BlockState): Boolean {
            if (state.getValue(BASE)) return true
            for (entry in PROPERTY_BY_DIRECTION) {
                if (state.getValue(entry.value) != WallSide.NONE) return true
            }
            return false
        }

        private fun canSupportAtFace(level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
            if (direction == Direction.UP) return false

            val relPos = pos.relative(direction)
            val relState = level.getBlockState(relPos)
            return MultifaceBlock.canAttachTo(level, direction, relPos, relState)
        }

        private fun getUpdatedState(state: BlockState, level: BlockGetter, pos: BlockPos, tip: Boolean): BlockState {
            var state = state
            val tip = tip || state.getValue(BASE)
            var stateAbove: BlockState
            var stateBelow: BlockState

            for (dir in Plane.HORIZONTAL) {
                val property = getPropertyForFace(dir)
                var wallside = if (canSupportAtFace(level, pos, dir)) {
                    if (tip) WallSide.LOW else state.getValue(property)
                } else WallSide.NONE

                if (wallside == WallSide.LOW) {
                    stateAbove = level.getBlockState(pos.above())
                    if (stateAbove.`is`(ModBlocks.PALE_MOSS_CARPET) && stateAbove.getValue(property) != WallSide.NONE && !stateAbove.getValue(
                            BASE
                        )
                    ) {
                        wallside = WallSide.TALL
                    }
                    if (!state.getValue(BASE)) {
                        stateBelow = level.getBlockState(pos.below())
                        if (stateBelow.`is`(ModBlocks.PALE_MOSS_CARPET) && stateBelow.getValue(property) == WallSide.NONE) {
                            wallside = WallSide.NONE
                        }
                    }
                }
                state = state.setValue(property, wallside)
            }
            return state
        }

//        fun placeAt(level: LevelAccessor, pos: BlockPos, random: RandomSource, flags: Int) {
//            val blockstate: BlockState = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState()
//            val blockstate1 = getUpdatedState(blockstate, level, pos, true)
//            level.setBlock(pos, blockstate1, flags)
//            val blockstate2 = createTopperWithSideChance(level, pos) { random.nextBoolean() }
//            if (!blockstate2.isAir) {
//                level.setBlock(pos.above(), blockstate2, flags)
//                val blockstate3 = getUpdatedState(blockstate1, level, pos, true)
//                level.setBlock(pos, blockstate3, flags)
//            }
//        }

        private fun createTopperWithSideChance(
            level: BlockGetter, pos: BlockPos, placeSide: BooleanSupplier
        ): BlockState {
            val abovePos = pos.above()
            val aboveState = level.getBlockState(abovePos)
            val aboveIsCarpet = aboveState.`is`(ModBlocks.PALE_MOSS_CARPET)
            if ((!aboveIsCarpet || !aboveState.getValue(BASE)) && (aboveIsCarpet || aboveState.canBeReplaced())) {
                val newAboveState = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState().setValue(BASE, false)
                var updatedAboveState = getUpdatedState(newAboveState, level, pos.above(), true)
                for (direction in Plane.HORIZONTAL) {
                    val property = getPropertyForFace(direction)
                    if (updatedAboveState.getValue(property) != WallSide.NONE && !placeSide.asBoolean) {
                        updatedAboveState = updatedAboveState.setValue(property, WallSide.NONE)
                    }
                }
                return if (hasFaces(updatedAboveState) && updatedAboveState !== aboveState) updatedAboveState else Blocks.AIR.defaultBlockState()
            } else {
                return Blocks.AIR.defaultBlockState()
            }
        }
    }

    init {
        registerDefaultState(
            stateDefinition.any().setValue(BASE, true).setValue(NORTH, WallSide.NONE).setValue(EAST, WallSide.NONE)
                .setValue(SOUTH, WallSide.NONE).setValue(WEST, WallSide.NONE)
        )
    }

    override fun codec(): MapCodec<PaleMossCarpetBlock> = simpleCodec(::PaleMossCarpetBlock)
    override fun getOcclusionShape(state: BlockState, level: BlockGetter, pos: BlockPos): VoxelShape = Shapes.empty()

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val shapes = mutableSetOf<VoxelShape>()
        when (state.getValue(NORTH)) {
            WallSide.LOW -> shapes += NORTH_LOW_SHAPE
            WallSide.TALL -> shapes += NORTH_SHAPE
            else -> {}
        }
        when (state.getValue(SOUTH)) {
            WallSide.LOW -> shapes += SOUTH_LOW_SHAPE
            WallSide.TALL -> shapes += SOUTH_SHAPE
            else -> {}
        }
        when (state.getValue(EAST)) {
            WallSide.LOW -> shapes += EAST_LOW_SHAPE
            WallSide.TALL -> shapes += EAST_SHAPE
            else -> {}
        }
        when (state.getValue(WEST)) {
            WallSide.LOW -> shapes += WEST_LOW_SHAPE
            WallSide.TALL -> shapes += WEST_SHAPE
            else -> {}
        }
        return Shapes.or(if (state.getValue(BASE)) BASE_SHAPE else Shapes.empty(), *shapes.toTypedArray())
    }

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true
    override fun getStateForPlacement(context: BlockPlaceContext) =
        getUpdatedState(defaultBlockState(), context.level, context.clickedPos, true)

    override fun getCollisionShape(
        state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext
    ): VoxelShape {
        return if (state.getValue(BASE)) BASE_SHAPE else Shapes.empty()
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val below = level.getBlockState(pos.below())
        return if (state.getValue(BASE)) !below.isAir else below.`is`(this) && below.getValue(BASE)
    }

    override fun setPlacedBy(
        level: Level, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack
    ) {
        if (level.isClientSide) return
        val random = level.getRandom()
        val topState = createTopperWithSideChance(level, pos) { random.nextBoolean() }
        if (!topState.isAir) level.setBlock(pos.above(), topState, 3)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (!state.canSurvive(level, pos)) {
            Blocks.AIR.defaultBlockState()
        } else {
            val newState = getUpdatedState(state, level, pos, false)
            if (!hasFaces(newState)) Blocks.AIR.defaultBlockState() else newState
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BASE, NORTH, EAST, SOUTH, WEST)
    }

    override fun isValidBonemealTarget(level: LevelReader, pos: BlockPos, state: BlockState): Boolean {
        return state.getValue(BASE) && !createTopperWithSideChance(level, pos) { true }.isAir
    }

    override fun isBonemealSuccess(level: Level, random: RandomSource, pos: BlockPos, state: BlockState) = true

    override fun performBonemeal(
        serverLevel: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState
    ) {
        val topState = createTopperWithSideChance(serverLevel, pos) { true }
        if (!topState.isAir) serverLevel.setBlock(pos.above(), topState, 3)
    }

}