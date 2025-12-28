package concerrox.ported.content.springtolife.drygrass

import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Plane
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.Heightmap
import kotlin.math.abs

object AmbientDesertBlockSoundsPlayer {

    fun playAmbientDryGrassSounds(level: Level, pos: BlockPos, random: RandomSource) {
        if (level !is ClientLevel) return
        if (random.nextInt(200) == 0 && shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
            val minecraft = Minecraft.getInstance()
            val player = minecraft.player
            if (player != null) {
                minecraft.soundManager.play(
                    EntityBoundSoundInstance(
                        ModSoundEvents.DRY_GRASS, SoundSource.AMBIENT, 1f, 1f, player, random.nextLong()
                    )
                )
            }
        }
    }

    fun playAmbientDeadBushSounds(level: Level, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(130) == 0) {
            val blockstate = level.getBlockState(pos.below())
            if ((blockstate.`is`(Blocks.RED_SAND) || blockstate.`is`(BlockTags.TERRACOTTA)) && random.nextInt(3) != 0) {
                return
            }
            if (shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
                level.playLocalSound(
                    pos.x.toDouble(),
                    pos.y.toDouble(),
                    pos.z.toDouble(),
                    ModSoundEvents.DEAD_BUSH_IDLE,
                    SoundSource.AMBIENT,
                    1.0f,
                    1.0f,
                    false
                )
            }
        }
    }

    fun shouldPlayDesertDryVegetationBlockSounds(level: Level, pos: BlockPos): Boolean {
        return level.getBlockState(pos)
            .`is`(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS) && level.getBlockState(pos.below())
            .`is`(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS)
    }

    private fun columnContainsTriggeringBlock(level: Level, pos: MutableBlockPos): Boolean {
        val i: Int = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.x, pos.z) - 1
        if (abs(i - pos.getY()) > 5) {
            pos.move(Direction.UP, 6)
            var blockstate1 = level.getBlockState(pos)
            pos.move(Direction.DOWN)

            for (j in 0..9) {
                val blockstate = level.getBlockState(pos)
                if (blockstate1.isAir() && canTriggerAmbientDesertSandSounds(blockstate)) {
                    return true
                }

                blockstate1 = blockstate
                pos.move(Direction.DOWN)
            }

            return false
        } else {
            val flag = level.getBlockState(pos.setY(i + 1)).isAir()
            return flag && canTriggerAmbientDesertSandSounds(
                level.getBlockState(
                    pos.setY(
                        i
                    )
                )
            )
        }
    }

    private fun shouldPlayAmbientSandSound(level: Level, pos: BlockPos): Boolean {
        var i = 0
        var j = 0
        val mutableBlockPos = pos.mutable()

        for (direction in Plane.HORIZONTAL) {
            mutableBlockPos.set(pos).move(direction, 8)
            if (columnContainsTriggeringBlock(
                    level, mutableBlockPos
                ) && i++ >= 3
            ) {
                return true
            }

            ++j
            val k = 4 - j
            val l = k + i
            val flag = l >= 3
            if (!flag) {
                return false
            }
        }

        return false
    }

    fun playAmbientSandSounds(level: Level, pos: BlockPos, random: RandomSource) {
        if (level.getBlockState(pos.above())
                .`is`(Blocks.AIR) && random.nextInt(2100) == 0 && shouldPlayAmbientSandSound(
                level, pos
            )
        ) {
            level.playLocalSound(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                ModSoundEvents.SAND_IDLE,
                SoundSource.AMBIENT,
                1.0f,
                1.0f,
                false
            )
        }
    }

    private fun canTriggerAmbientDesertSandSounds(state: BlockState): Boolean {
        return state.`is`(ModBlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS)
    }

}