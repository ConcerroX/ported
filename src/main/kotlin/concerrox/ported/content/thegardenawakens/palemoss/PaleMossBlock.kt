package concerrox.ported.content.thegardenawakens.palemoss

import concerrox.ported.registry.ModConfiguredFeatures
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.MossBlock
import net.minecraft.world.level.block.state.BlockState

class PaleMossBlock(properties: Properties) : MossBlock(properties) {

    override fun performBonemeal(level: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
        level.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap {
            it.getHolder(ModConfiguredFeatures.PALE_MOSS_PATCH_BONEMEAL)
        }.ifPresent {
            it.value().place(level, level.chunkSource.generator, random, pos.above())
        }
    }

}