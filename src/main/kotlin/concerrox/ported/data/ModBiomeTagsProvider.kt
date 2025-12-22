package concerrox.ported.data

import concerrox.ported.registry.ModBiomes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBiomeTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
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
    }

}