package concerrox.ported.content.springtolife.fallentree

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Plane
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import java.util.function.BiConsumer
import java.util.function.Function

class FallenTreeFeature(configCodec: Codec<FallenTreeConfiguration>) : Feature<FallenTreeConfiguration>(configCodec) {

    override fun place(context: FeaturePlaceContext<FallenTreeConfiguration>): Boolean {
        placeFallenTree(context.config(), context.origin(), context.level(), context.random())
        return true
    }

    private fun placeFallenTree(
        config: FallenTreeConfiguration, origin: BlockPos, level: WorldGenLevel, random: RandomSource
    ) {
        placeStump(config, level, random, origin.mutable())
        val direction = Plane.HORIZONTAL.getRandomDirection(random)
        val i = config.logLength.sample(random) - 2
        val pos = origin.relative(direction, 2 + random.nextInt(2)).mutable()
        setGroundHeightForFallenLogStartPos(level, pos)
        if (canPlaceEntireFallenLog(level, i, pos, direction)) {
            placeFallenLog(config, level, random, i, pos, direction)
        }
    }

    private fun setGroundHeightForFallenLogStartPos(level: WorldGenLevel, pos: MutableBlockPos) {
        pos.move(Direction.UP, STUMP_HEIGHT)
        (0..FALLEN_LOG_MAX_FALL_HEIGHT_TO_GROUND).forEach { _ ->
            if (mayPlaceOn(level, pos)) return
            pos.move(Direction.DOWN)
        }
    }

    private fun placeStump(
        config: FallenTreeConfiguration, level: WorldGenLevel, random: RandomSource, pos: MutableBlockPos
    ) {
        val blocks = placeLogBlock(config, level, random, pos, Function.identity())
        decorateLogs(level, random, mutableSetOf(blocks), config.stumpDecorators)
    }

    private fun canPlaceEntireFallenLog(
        level: WorldGenLevel, logLength: Int, pos: MutableBlockPos, direction: Direction
    ): Boolean {
        var i = 0

        (0..<logLength).forEach { _ ->
            if (!TreeFeature.validTreePos(level, pos)) {
                return false
            }

            if (!this.isOverSolidGround(level, pos)) {
                ++i
                if (i > 2) {
                    return false
                }
            } else {
                i = 0
            }

            pos.move(direction)
        }

        pos.move(direction.opposite, logLength)
        return true
    }

    private fun placeFallenLog(
        config: FallenTreeConfiguration,
        level: WorldGenLevel,
        random: RandomSource,
        logLength: Int,
        pos: MutableBlockPos,
        direction: Direction
    ) {
        val set = hashSetOf<BlockPos>()
        (0..<logLength).forEach { _ ->
            set.add(placeLogBlock(config, level, random, pos, getSidewaysStateModifier(direction)))
            pos.move(direction)
        }
        decorateLogs(level, random, set, config.logDecorators)
    }

    private fun mayPlaceOn(level: LevelAccessor, pos: BlockPos): Boolean {
        return TreeFeature.validTreePos(level, pos) && this.isOverSolidGround(level, pos)
    }

    private fun isOverSolidGround(level: LevelAccessor, pos: BlockPos): Boolean {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP)
    }

    private fun placeLogBlock(
        config: FallenTreeConfiguration,
        level: WorldGenLevel,
        random: RandomSource,
        pos: MutableBlockPos,
        stateModifier: Function<BlockState, BlockState>
    ): BlockPos {
        level.setBlock(pos, stateModifier.apply(config.trunkProvider.getState(random, pos)), 3)
        markAboveForPostProcessing(level, pos)
        return pos.immutable()
    }

    private fun decorateLogs(
        level: WorldGenLevel,
        random: RandomSource,
        logPositions: MutableSet<BlockPos>,
        decorators: MutableList<TreeDecorator>
    ) {
        if (!decorators.isEmpty()) {
            val context = TreeDecorator.Context(
                level, getDecorationSetter(level), random, logPositions, mutableSetOf(), mutableSetOf()
            )
            decorators.forEach { it.place(context) }
        }
    }

    private fun getDecorationSetter(level: WorldGenLevel): BiConsumer<BlockPos, BlockState> {
        return BiConsumer { pos, state -> level.setBlock(pos, state, 19) }
    }

    companion object {

        private const val STUMP_HEIGHT = 1
        private const val STUMP_HEIGHT_PLUS_EMPTY_SPACE = 2
        private const val FALLEN_LOG_MAX_FALL_HEIGHT_TO_GROUND = 5
        private const val FALLEN_LOG_MAX_GROUND_GAP = 2
        private const val FALLEN_LOG_MAX_SPACE_FROM_STUMP = 2

        private fun getSidewaysStateModifier(direction: Direction): Function<BlockState, BlockState> {
            return Function {
                it.trySetValue(RotatedPillarBlock.AXIS, direction.axis)
            }
        }

    }
}