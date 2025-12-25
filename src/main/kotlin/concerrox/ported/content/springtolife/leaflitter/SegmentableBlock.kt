package concerrox.ported.content.springtolife.leaflitter

import concerrox.ported.util.ShapeUtils
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Function
import kotlin.math.min

interface SegmentableBlock {

    fun getShapeHeight() = 1.0
    fun getSegmentAmountProperty() = AMOUNT

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getShapeCalculator(
        directionProperty: EnumProperty<Direction>, amountProperty: IntegerProperty
    ): Function<BlockState, VoxelShape> {
        val map = ShapeUtils.rotateHorizontal(Block.box(0.0, 0.0, 0.0, 8.0, getShapeHeight(), 8.0))
        return Function { state ->
            var shape = Shapes.empty()
            var direction = state.getValue(directionProperty)
            val i = state.getValue(amountProperty)
            (0..<i).forEach { j ->
                shape = Shapes.or(shape, map[direction])
                direction = direction.counterClockWise
            }
            shape.singleEncompassing()
        }
    }

    fun canBeReplaced(state: BlockState, context: BlockPlaceContext, amountProperty: IntegerProperty): Boolean {
        return !context.isSecondaryUseActive && context.itemInHand.`is`(state.block.asItem()) && state.getValue(
            amountProperty
        ) < MAX_SEGMENT
    }

    fun getStateForPlacement(
        context: BlockPlaceContext,
        block: Block,
        amountProperty: IntegerProperty,
        directionProperty: EnumProperty<Direction>
    ): BlockState {
        val state = context.level.getBlockState(context.clickedPos)
        return if (state.`is`(block)) {
            state.setValue(amountProperty, min(MAX_SEGMENT, state.getValue(amountProperty) + 1))
        } else {
            block.defaultBlockState().setValue(directionProperty, context.horizontalDirection.opposite)
        }
    }

    companion object {
        const val MIN_SEGMENT = 1
        const val MAX_SEGMENT = 4
        val AMOUNT: IntegerProperty = IntegerProperty.create("segment_amount", 1, 4)
    }

}