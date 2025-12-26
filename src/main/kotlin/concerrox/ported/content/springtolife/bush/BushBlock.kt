package concerrox.ported.content.springtolife.bush

import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import concerrox.ported.util.BonemealableBlockUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.state.BlockState

class BushBlock(properties: Properties) : VegetationBlock(properties), BonemealableBlock {

    override fun codec(): MapCodec<BushBlock> = simpleCodec(::BushBlock)
    override fun isBonemealSuccess(p0: Level, p1: RandomSource, p2: BlockPos, p3: BlockState) = true
    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 60
    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100

    override fun isValidBonemealTarget(p0: LevelReader, p1: BlockPos, p2: BlockState): Boolean {
        return BonemealableBlockUtils.hasSpreadableNeighbourPos(p0, p1, p2)
    }

    override fun performBonemeal(level: ServerLevel, p1: RandomSource, p2: BlockPos, p3: BlockState) {
        BonemealableBlockUtils.findSpreadableNeighbourPos(level, p2, p3)
            .ifPresent { level.setBlockAndUpdate(it, defaultBlockState()) }
    }

}