package concerrox.ported.data

import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartDecorator
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossDecorator
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModConfiguredFeatures
import concerrox.ported.registry.ModPlacedFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.*
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer
import net.minecraft.world.level.levelgen.placement.CaveSurface
import java.util.*

object ModConfiguredFeatureProvider {

    fun bootstrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = context.lookup(Registries.PLACED_FEATURE)

        context.register(
            ModConfiguredFeatures.PALE_OAK, ConfiguredFeature(
                Feature.TREE, TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                    DarkOakTrunkPlacer(6, 2, 1),
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                    DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).decorators(listOf(PaleMossDecorator(0.15F, 0.4F, 0.8F))).ignoreVines().build()
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_OAK_CREAKING, ConfiguredFeature(
                Feature.TREE, TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                    DarkOakTrunkPlacer(6, 2, 1),
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                    DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).decorators(listOf(PaleMossDecorator(0.15F, 0.4F, 0.8F), CreakingHeartDecorator(1f))).ignoreVines()
                    .build()
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_OAK_BONEMEAL, ConfiguredFeature(
                Feature.TREE, TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                    DarkOakTrunkPlacer(6, 2, 1),
                    BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                    DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).ignoreVines().build()
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_MOSS_VEGETATION, ConfiguredFeature(
                Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(
                    WeightedStateProvider(
                        SimpleWeightedRandomList.builder<BlockState>()
                            .add(ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState(), 25)
                            .add(Blocks.SHORT_GRASS.defaultBlockState(), 25)
                            .add(Blocks.TALL_GRASS.defaultBlockState(), 10)
                    )
                )
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_MOSS_PATCH_BONEMEAL, ConfiguredFeature(
                Feature.VEGETATION_PATCH, VegetationPatchConfiguration(
                    BlockTags.MOSS_REPLACEABLE,
                    BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
                    PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_MOSS_VEGETATION)),
                    CaveSurface.FLOOR, ConstantInt.of(1),
                    0f, 5, 0.6f, UniformInt.of(1, 2), 0.75f,
                )
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_GARDEN_VEGETATION, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.PALE_OAK_CREAKING_CHECKED), 0.1f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.PALE_OAK_CHECKED), 0.9f),
                    ), placedFeatures.getOrThrow(ModPlacedFeatures.PALE_OAK_CHECKED)
                )
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_MOSS_PATCH, ConfiguredFeature(
                Feature.VEGETATION_PATCH, VegetationPatchConfiguration(
                    BlockTags.MOSS_REPLACEABLE,
                    BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
                    PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_MOSS_VEGETATION)),
                    CaveSurface.FLOOR, ConstantInt.of(1), 0f, 5, 0.3f, UniformInt.of(2, 4), 0.75f,
                )
            )
        )
        context.register(
            ModConfiguredFeatures.PALE_FOREST_FLOWERS, ConfiguredFeature(
                Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
                    Feature.SIMPLE_BLOCK,
                    SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()))
                )
            )
        )
        context.register(
            ModConfiguredFeatures.FLOWER_PALE_GARDEN, ConfiguredFeature(
                Feature.FLOWER, RandomPatchConfiguration(
                    1, 0, 0, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(
                            BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()),
                        )
                    )
                )
            )
        )

    }

}