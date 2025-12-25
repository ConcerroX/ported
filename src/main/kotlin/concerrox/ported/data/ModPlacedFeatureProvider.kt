package concerrox.ported.data

import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModConfiguredFeatures
import concerrox.ported.registry.ModPlacedFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.*

object ModPlacedFeatureProvider {

    private val HEIGHTMAP_NO_LEAVES = HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)

    fun bootstrap(context: BootstrapContext<PlacedFeature>) {
        val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

        fun register(
            key: ResourceKey<PlacedFeature>,
            configuredFeatureKey: ResourceKey<ConfiguredFeature<*, *>>,
            vararg placement: PlacementModifier
        ) {
            context.register(
                key, PlacedFeature(configuredFeatures.getOrThrow(configuredFeatureKey), placement.toList())
            )
        }

        register(
            ModPlacedFeatures.PALE_GARDEN_VEGETATION,
            ModConfiguredFeatures.PALE_GARDEN_VEGETATION,
            CountPlacement.of(16),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome()
        )
        register(
            ModPlacedFeatures.PALE_MOSS_PATCH,
            ModConfiguredFeatures.PALE_MOSS_PATCH,
            CountPlacement.of(1),
            InSquarePlacement.spread(),
            HEIGHTMAP_NO_LEAVES,
            BiomeFilter.biome()
        )
        register(
            ModPlacedFeatures.PALE_GARDEN_FLOWERS,
            ModConfiguredFeatures.PALE_FOREST_FLOWERS,
            RarityFilter.onAverageOnceEvery(8),
            InSquarePlacement.spread(),
            HEIGHTMAP_NO_LEAVES,
            BiomeFilter.biome()
        )
        register(
            ModPlacedFeatures.FLOWER_PALE_GARDEN,
            ModConfiguredFeatures.FLOWER_PALE_GARDEN,
            RarityFilter.onAverageOnceEvery(32),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        )
        register(
            ModPlacedFeatures.PALE_OAK_CREAKING_CHECKED,
            ModConfiguredFeatures.PALE_OAK_CREAKING,
            PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())
        )
        register(
            ModPlacedFeatures.PALE_OAK_CHECKED,
            ModConfiguredFeatures.PALE_OAK,
            PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())
        )

        register(
            ModPlacedFeatures.OAK_BEES_0002_LEAF_LITTER,
            ModConfiguredFeatures.OAK_BEES_0002_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        register(
            ModPlacedFeatures.BIRCH_BEES_0002_LEAF_LITTER,
            ModConfiguredFeatures.BIRCH_BEES_0002_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        )
        register(
            ModPlacedFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER,
            ModConfiguredFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        register(
            ModPlacedFeatures.OAK_LEAF_LITTER,
            ModConfiguredFeatures.OAK_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        register(
            ModPlacedFeatures.DARK_OAK_LEAF_LITTER,
            ModConfiguredFeatures.DARK_OAK_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
        )
        register(
            ModPlacedFeatures.BIRCH_LEAF_LITTER,
            ModConfiguredFeatures.BIRCH_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        )
        register(
            ModPlacedFeatures.FANCY_OAK_LEAF_LITTER,
            ModConfiguredFeatures.FANCY_OAK_LEAF_LITTER,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )

        register(
            ModPlacedFeatures.PATCH_LEAF_LITTER,
            ModConfiguredFeatures.PATCH_LEAF_LITTER,
            *VegetationPlacements.worldSurfaceSquaredWithCount(2).toTypedArray()
        )
        register(
            ModPlacedFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER,
            ModConfiguredFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER,
            *VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)).toTypedArray()
        )
        register(
            VegetationPlacements.TREES_BADLANDS,
            ModConfiguredFeatures.TREES_BADLANDS,
            *VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(5, 0.1f, 1),
                Blocks.OAK_SAPLING,
            ).toTypedArray()
        )

        register(
            ModPlacedFeatures.FALLEN_OAK_TREE,
            ModConfiguredFeatures.FALLEN_OAK_TREE,
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        )
        register(
            ModPlacedFeatures.FALLEN_BIRCH_TREE,
            ModConfiguredFeatures.FALLEN_BIRCH_TREE,
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        )
        register(
            ModPlacedFeatures.FALLEN_SUPER_BIRCH_TREE,
            ModConfiguredFeatures.FALLEN_SUPER_BIRCH_TREE,
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        )
        register(
            ModPlacedFeatures.FALLEN_SPRUCE_TREE,
            ModConfiguredFeatures.FALLEN_SPRUCE_TREE,
            PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING)
        )
        register(
            ModPlacedFeatures.FALLEN_JUNGLE_TREE,
            ModConfiguredFeatures.FALLEN_JUNGLE_TREE,
            PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING)
        )

        register(
            VegetationPlacements.TREES_BIRCH,
            ModConfiguredFeatures.TREES_BIRCH,
            *(VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.BIRCH_SAPLING)
                .toTypedArray())
        )
        register(
            VegetationPlacements.TREES_SNOWY,
            ModConfiguredFeatures.TREES_SNOWY,
            *(VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), Blocks.SPRUCE_SAPLING)
                .toTypedArray())
        )

        register(
            ModPlacedFeatures.WILDFLOWERS_BIRCH_FOREST,
            ModConfiguredFeatures.WILDFLOWERS_BIRCH_FOREST,
            CountPlacement.of(3),
            RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        )
        register(
            ModPlacedFeatures.WILDFLOWERS_MEADOW,
            ModConfiguredFeatures.WILDFLOWERS_MEADOW,
            NoiseThresholdCountPlacement.of(-0.8, 5, 10),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        )

    }

}