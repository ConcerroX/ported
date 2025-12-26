package concerrox.ported.content.springtolife.drygrass

import concerrox.ported.registry.ModBlocks
import concerrox.ported.util.ShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

class ShortDryGrassBlock(properties: Properties) : DryVegetationBlock(properties), BonemealableBlock {

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(level, pos, random)
    }

    override fun isValidBonemealTarget(p_401814_: LevelReader, p_401760_: BlockPos, p_401924_: BlockState): Boolean {
        return true
    }

    override fun isBonemealSuccess(
        p_401806_: Level, p_401772_: RandomSource, p_401791_: BlockPos, p_401942_: BlockState
    ): Boolean {
        return true
    }

    override fun performBonemeal(
        p_401950_: ServerLevel, p_401831_: RandomSource, p_401948_: BlockPos, p_401868_: BlockState
    ) {
        p_401950_.setBlockAndUpdate(p_401948_, ModBlocks.TALL_DRY_GRASS.get().defaultBlockState())
    }

    companion object {
        private val SHAPE = ShapeUtils.column(12.0, 0.0, 10.0)
    }

}