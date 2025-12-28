package concerrox.ported.data

import concerrox.ported.registry.ModBiomes
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

        context.register(
            key("add_wildflowers_birch_forest"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST)
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILDFLOWERS_BIRCH_FOREST)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("add_wildflowers_meadow"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.MEADOW)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILDFLOWERS_MEADOW)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )

        context.register(
            key("add_patch_bush"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.WINDSWEPT_HILLS),
                    biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS),
                    biomes.getOrThrow(Biomes.WINDSWEPT_FOREST),
                    biomes.getOrThrow(Biomes.PLAINS),
                    biomes.getOrThrow(Biomes.FOREST),
                    biomes.getOrThrow(Biomes.BIRCH_FOREST),
                    biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST),
                    biomes.getOrThrow(Biomes.RIVER),
                    biomes.getOrThrow(Biomes.FROZEN_RIVER)
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_BUSH)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("add_patch_firefly_bush_near_water"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.MUSHROOM_FIELDS),
                    biomes.getOrThrow(Biomes.BADLANDS),
                    biomes.getOrThrow(Biomes.ERODED_BADLANDS),
                    biomes.getOrThrow(Biomes.WOODED_BADLANDS),
                    biomes.getOrThrow(Biomes.MANGROVE_SWAMP),

                    biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA),
                    biomes.getOrThrow(Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                    biomes.getOrThrow(Biomes.JUNGLE),
                    biomes.getOrThrow(Biomes.SPARSE_JUNGLE),
                    biomes.getOrThrow(Biomes.BAMBOO_JUNGLE),
                    biomes.getOrThrow(Biomes.WINDSWEPT_HILLS),
                    biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS),
                    biomes.getOrThrow(Biomes.WINDSWEPT_FOREST),
                    biomes.getOrThrow(Biomes.PLAINS),
                    biomes.getOrThrow(Biomes.SNOWY_PLAINS),
                    biomes.getOrThrow(Biomes.SUNFLOWER_PLAINS),
                    biomes.getOrThrow(Biomes.ICE_SPIKES),
                    biomes.getOrThrow(Biomes.SAVANNA),
                    biomes.getOrThrow(Biomes.SAVANNA_PLATEAU),
                    biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA),
                    biomes.getOrThrow(Biomes.WARM_OCEAN),
                    biomes.getOrThrow(Biomes.LUKEWARM_OCEAN),
                    biomes.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN),
                    biomes.getOrThrow(Biomes.OCEAN),
                    biomes.getOrThrow(Biomes.DEEP_OCEAN),
                    biomes.getOrThrow(Biomes.COLD_OCEAN),
                    biomes.getOrThrow(Biomes.DEEP_COLD_OCEAN),
                    biomes.getOrThrow(Biomes.FROZEN_OCEAN),
                    biomes.getOrThrow(Biomes.DEEP_FROZEN_OCEAN),
                    biomes.getOrThrow(Biomes.FOREST),
                    biomes.getOrThrow(Biomes.FLOWER_FOREST),
                    biomes.getOrThrow(Biomes.BIRCH_FOREST),
                    biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST),
                    biomes.getOrThrow(Biomes.TAIGA),
                    biomes.getOrThrow(Biomes.SNOWY_TAIGA),
                    biomes.getOrThrow(Biomes.DARK_FOREST),
                    biomes.getOrThrow(Biomes.RIVER),
                    biomes.getOrThrow(Biomes.FROZEN_RIVER),
                    biomes.getOrThrow(Biomes.BEACH),
                    biomes.getOrThrow(Biomes.SNOWY_BEACH),
                    biomes.getOrThrow(Biomes.STONY_SHORE),

                    biomes.getOrThrow(ModBiomes.PALE_GARDEN),
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_FIREFLY_BUSH_NEAR_WATER)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("add_patch_firefly_bush_swamp"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.SWAMP),
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_FIREFLY_BUSH_SWAMP)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("add_patch_firefly_bush_near_water_swamp"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.SWAMP),
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )

        context.register(
            key("add_patch_dry_grass_desert"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DESERT)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_DRY_GRASS_DESERT)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )
        context.register(
            key("add_patch_dry_grass_badlands"), BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(Biomes.BADLANDS),
                    biomes.getOrThrow(Biomes.ERODED_BADLANDS),
                    biomes.getOrThrow(Biomes.WOODED_BADLANDS),
                ),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PATCH_DRY_GRASS_BADLANDS)),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        )

    }

}