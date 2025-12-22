package concerrox.ported.data

import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModConfiguredFeatures
import concerrox.ported.registry.ModPlacedFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
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

    }

}