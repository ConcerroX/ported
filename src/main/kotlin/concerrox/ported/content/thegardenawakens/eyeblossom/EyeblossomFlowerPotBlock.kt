package concerrox.ported.content.thegardenawakens.eyeblossom

import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlock
import concerrox.ported.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class EyeblossomFlowerPotBlock(
    emptyPot: Supplier<FlowerPotBlock>?, block: Supplier<out Block>, properties: Properties
) : FlowerPotBlock(emptyPot, block, properties) {

    override fun isRandomlyTicking(state: BlockState) =
        state.`is`(ModBlocks.POTTED_OPEN_EYEBLOSSOM) || state.`is`(ModBlocks.POTTED_CLOSED_EYEBLOSSOM)

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (isRandomlyTicking(state) && level.dimensionType().natural()) {
            val isOpen = potted === ModBlocks.OPEN_EYEBLOSSOM.get()
            val isNaturalNight = CreakingHeartBlock.isNaturalNight(level)
            if (isOpen != isNaturalNight) {
                level.setBlockAndUpdate(pos, opposite(state))
                val type = EyeblossomBlock.Type.fromBoolean(isOpen).transform()
                type.spawnTransformParticle(level, pos, random)
                level.playSound(null, pos, type.longSwitchSound, SoundSource.BLOCKS, 1f, 1f)
            }
        }
        super.randomTick(state, level, pos, random)
    }

    fun opposite(state: BlockState): BlockState {
        return if (state.`is`(ModBlocks.POTTED_OPEN_EYEBLOSSOM)) {
            ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get().defaultBlockState()
        } else {
            if (state.`is`(ModBlocks.POTTED_CLOSED_EYEBLOSSOM)) ModBlocks.POTTED_OPEN_EYEBLOSSOM.get()
                .defaultBlockState() else state
        }
    }

}