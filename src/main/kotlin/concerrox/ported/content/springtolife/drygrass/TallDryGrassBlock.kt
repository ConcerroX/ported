package concerrox.ported.content.springtolife.drygrass

import concerrox.ported.registry.ModBlocks
import concerrox.ported.util.BonemealableBlockUtils
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

class TallDryGrassBlock(properties: Properties) : DryVegetationBlock(properties), BonemealableBlock {

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(level, pos, random)
    }

    override fun isValidBonemealTarget(p_401899_: LevelReader, p_401858_: BlockPos, p_401909_: BlockState): Boolean {
        return BonemealableBlockUtils.hasSpreadableNeighbourPos(
            p_401899_, p_401858_, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState()
        )
    }

    override fun isBonemealSuccess(
        p_401931_: Level, p_401815_: RandomSource, p_401808_: BlockPos, p_401935_: BlockState
    ): Boolean {
        return true
    }

    override fun performBonemeal(
        p_401804_: ServerLevel, p_401769_: RandomSource, p_401777_: BlockPos, p_401790_: BlockState
    ) {
        BonemealableBlockUtils.findSpreadableNeighbourPos(
            p_401804_, p_401777_, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState()
        ).ifPresent {
            p_401804_.setBlockAndUpdate(it, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState())
        }
    }

    companion object {
        private val SHAPE = ShapeUtils.column(14.0, 0.0, 16.0)
    }

}