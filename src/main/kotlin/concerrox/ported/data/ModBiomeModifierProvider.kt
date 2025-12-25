package concerrox.ported.data

import concerrox.ported.registry.ModPlacedFeatures
import concerrox.ported.res
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.levelgen.GenerationStep
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

object ModBiomeModifierProvider {

    fun bootstrap(context: BootstrapContext<BiomeModifier>) {
        val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
        val biomes = context.lookup(Registries.BIOME)

        fun key(path: String) = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, res(path))

        context.register(
            key("add_patch_leaf_litter"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DARK_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_LEAF_LITTER)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("remove_trees_birch_and_oak"), BiomeModifiers.RemoveFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(VegetationPlacements.TREES_BIRCH_AND_OAK)),
                setOf(GenerationStep.Decoration.VEGETAL_DECORATION)
            )
        )
        context.register(
            key("add_trees_birch_and_oak_leaf_litter"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )

    }

}