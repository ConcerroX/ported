package concerrox.ported.content.springtolife.wildflowers

import com.google.common.collect.ImmutableMap
import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.SegmentableBlock
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Function

class FlowerBedBlock(properties: Properties) : VegetationBlock(properties), BonemealableBlock, SegmentableBlock {

    init {
        registerDefaultState(
            stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(getSegmentAmountProperty(), 1)
        )
    }

    private val shapes: Function<BlockState, VoxelShape> = makeShapes()

    override fun getShapeHeight() = 3.0
    override fun codec(): MapCodec<FlowerBedBlock> = simpleCodec(::FlowerBedBlock)
    override fun getSegmentAmountProperty() = AMOUNT

    private fun makeShapes(): Function<BlockState, VoxelShape> {
        return getShapeForEachStateFunction(
            getShapeCalculator(FACING, getSegmentAmountProperty())
        )
    }

    private fun getShapeForEachStateFunction(shapeGetter: Function<BlockState, VoxelShape>): Function<BlockState, VoxelShape> {
        val map = stateDefinition.getPossibleStates().stream()
            .collect(ImmutableMap.toImmutableMap(Function.identity(), shapeGetter))
        return Function { key -> map.get(key)!! }
    }

    override fun rotate(state: BlockState, level: LevelAccessor, pos: BlockPos, direction: Rotation): BlockState {
        return state.setValue(
            FACING, direction.rotate(state.getValue(FACING))
        )
    }

    @Suppress("DEPRECATION")
    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
    }

    override fun canBeReplaced(state: BlockState, useContext: BlockPlaceContext): Boolean {
        return super<SegmentableBlock>.canBeReplaced(state, useContext, getSegmentAmountProperty())
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return shapes.apply(state)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return super<SegmentableBlock>.getStateForPlacement(
            context, this, getSegmentAmountProperty(), FACING
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, getSegmentAmountProperty())
    }

    override fun isValidBonemealTarget(level: LevelReader, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun isBonemealSuccess(level: Level, random: RandomSource, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun performBonemeal(
        level: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState
    ) {
        val i = state.getValue(getSegmentAmountProperty()) as Int
        if (i < 4) {
            level.setBlock(pos, state.setValue(getSegmentAmountProperty(), i + 1), 2)
        } else {
            popResource(level, pos, ItemStack(this))
        }
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
        return 100
    }

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
        return 60
    }

    companion object {
        val FACING: EnumProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING
        val AMOUNT: IntegerProperty = BlockStateProperties.FLOWER_AMOUNT
    }

}