package concerrox.ported.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Plane
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import java.util.*

object BonemealableBlockUtils {

    fun hasSpreadableNeighbourPos(level: LevelReader, pos: BlockPos, state: BlockState): Boolean {
        return getSpreadableNeighbourPos(Plane.HORIZONTAL.stream().toList(), level, pos, state).isPresent
    }

    fun findSpreadableNeighbourPos(level: Level, pos: BlockPos, state: BlockState): Optional<BlockPos> {
        return getSpreadableNeighbourPos(Plane.HORIZONTAL.shuffledCopy(level.random), level, pos, state)
    }

    private fun getSpreadableNeighbourPos(
        directions: MutableList<Direction>, level: LevelReader, pos: BlockPos, state: BlockState
    ): Optional<BlockPos> {
        for (direction in directions) {
            val relPos = pos.relative(direction)
            if (level.isEmptyBlock(relPos) && state.canSurvive(level, relPos)) {
                return Optional.of(relPos)
            }
        }
        return Optional.empty()
    }

}