package concerrox.ported.content.springtolife.firefly

import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import concerrox.ported.registry.ModParticleTypes
import concerrox.ported.registry.ModSoundEvents
import concerrox.ported.util.BonemealableBlockUtils
import concerrox.ported.util.isMoonVisible
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.Heightmap

class FireflyBushBlock(properties: Properties) : VegetationBlock(properties), BonemealableBlock {

    override fun codec(): MapCodec<FireflyBushBlock> = simpleCodec(::FireflyBushBlock)
    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 60
    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN) == 0 && level.isMoonVisible() && level.getHeight(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.x, pos.z
            ) <= pos.y
        ) {
            level.playLocalSound(pos, ModSoundEvents.FIREFLY_BUSH_IDLE, SoundSource.AMBIENT, 1f, 1f, false)
        }

        if (level.getMaxLocalRawBrightness(pos) <= FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL && random.nextDouble() <= FIREFLY_CHANCE_PER_TICK) {
            val d0 = pos.x.toDouble() + random.nextDouble() * FIREFLY_HORIZONTAL_RANGE - FIREFLY_VERTICAL_RANGE
            val d1 = pos.y.toDouble() + random.nextDouble() * FIREFLY_VERTICAL_RANGE
            val d2 = pos.z.toDouble() + random.nextDouble() * FIREFLY_HORIZONTAL_RANGE - FIREFLY_VERTICAL_RANGE
            level.addParticle(ModParticleTypes.FIREFLY.get(), d0, d1, d2, 0.0, 0.0, 0.0)
        }
    }

    override fun isBonemealSuccess(p0: Level, p1: RandomSource, p2: BlockPos, p3: BlockState) = true

    override fun isValidBonemealTarget(p0: LevelReader, p1: BlockPos, p2: BlockState): Boolean {
        return BonemealableBlockUtils.hasSpreadableNeighbourPos(p0, p1, p2)
    }

    override fun performBonemeal(level: ServerLevel, p1: RandomSource, p2: BlockPos, p3: BlockState) {
        BonemealableBlockUtils.findSpreadableNeighbourPos(level, p2, p3)
            .ifPresent { level.setBlockAndUpdate(it, defaultBlockState()) }
    }

    companion object {
        private const val FIREFLY_CHANCE_PER_TICK = 0.7
        private const val FIREFLY_HORIZONTAL_RANGE = 10.0
        private const val FIREFLY_VERTICAL_RANGE = 5.0
        private const val FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL = 13
        private const val FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN = 30
    }

}