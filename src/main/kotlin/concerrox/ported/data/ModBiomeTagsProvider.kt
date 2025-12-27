package concerrox.ported.data

import concerrox.ported.registry.ModBiomeTags
import concerrox.ported.registry.ModBiomes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biomes
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBiomeTagsProvider(
    output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper
) : BiomeTagsProvider(output, lookupProvider, ResourceLocation.DEFAULT_NAMESPACE, existingFileHelper) {

    override fun addTags(provider: HolderLookup.Provider) {
//        val biomes = provider.lookupOrThrow(Registries.BIOME)
//        val paleGarden = biomes.getOrThrow(ModBiomes.PALE_GARDEN)
        tag(BiomeTags.IS_FOREST).addOptional(ModBiomes.PALE_GARDEN.location())
        tag(BiomeTags.HAS_WOODLAND_MANSION).addOptional(ModBiomes.PALE_GARDEN.location())
        tag(BiomeTags.STRONGHOLD_BIASED_TO).addOptional(ModBiomes.PALE_GARDEN.location())
        tag(Tags.Biomes.IS_DECIDUOUS_TREE).addOptional(ModBiomes.PALE_GARDEN.location())
        tag(Tags.Biomes.IS_SPOOKY).addOptional(ModBiomes.PALE_GARDEN.location())
        tag(Tags.Biomes.IS_RARE).addOptional(ModBiomes.PALE_GARDEN.location())

        tag(ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS).add(Biomes.DESERT).add(Biomes.WARM_OCEAN)
            .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_NETHER)
            .addTag(BiomeTags.IS_BADLANDS).add(Biomes.MANGROVE_SWAMP).add(Biomes.DEEP_LUKEWARM_OCEAN)
            .add(Biomes.LUKEWARM_OCEAN)

        tag(ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES)
            .add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_OCEAN)
            .add(Biomes.DEEP_FROZEN_OCEAN).add(Biomes.GROVE).add(Biomes.DEEP_DARK).add(Biomes.FROZEN_RIVER)
            .add(Biomes.SNOWY_TAIGA).add(Biomes.SNOWY_BEACH).addTag(BiomeTags.IS_END).add(Biomes.COLD_OCEAN)
            .add(Biomes.DEEP_COLD_OCEAN).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
            .add(Biomes.TAIGA).add(Biomes.WINDSWEPT_FOREST).add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
            .add(Biomes.WINDSWEPT_HILLS).add(Biomes.STONY_PEAKS)

    }

}