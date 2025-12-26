package concerrox.ported.content.springtolife.leaflitter

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathComputationType

abstract class VegetationBlock(properties: Properties) : Block(properties) {

    abstract override fun codec(): MapCodec<out VegetationBlock>

    protected open fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return state.`is`(BlockTags.DIRT) || state.block is FarmBlock
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
            super.updateShape(state, direction, neighborState, level, pos, neighborPos)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val below = pos.below()
        val belowBlockState = level.getBlockState(below)
        val soilDecision = belowBlockState.canSustainPlant(level, below, Direction.UP, state)
        return if (!soilDecision.isDefault) {
            soilDecision.isTrue
        } else {
            mayPlaceOn(belowBlockState, level, below)
        }
    }

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return state.fluidState.isEmpty
    }

    override fun isPathfindable(state: BlockState, type: PathComputationType): Boolean {
        return (type == PathComputationType.AIR && !hasCollision) || super.isPathfindable(state, type)
    }

}