package concerrox.ported.content.springtolife.drygrass

import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks

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


}