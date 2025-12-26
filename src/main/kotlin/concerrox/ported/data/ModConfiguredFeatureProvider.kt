package concerrox.ported.data

import com.google.common.collect.ImmutableList
import concerrox.ported.content.springtolife.fallentree.AttachedToLogsDecorator
import concerrox.ported.content.springtolife.fallentree.FallenTreeConfiguration
import concerrox.ported.content.springtolife.leaflitter.LeafLitterBlock
import concerrox.ported.content.springtolife.leaflitter.PlaceOnGroundDecorator
import concerrox.ported.content.springtolife.wildflowers.FlowerBedBlock
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartDecorator
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossDecorator
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModConfiguredFeatures
import concerrox.ported.registry.ModFeatures
import concerrox.ported.registry.ModPlacedFeatures
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.features.TreeFeatures
import net.minecraft.data.worldgen.features.VegetationFeatures
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.TreePlacements
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer
import net.minecraft.world.level.levelgen.placement.CaveSurface
import java.util.*

object ModConfiguredFeatureProvider {

    fun bootstrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)
        val placedFeatures = context.lookup(Registries.PLACED_FEATURE)

        context.register(
            ModConfiguredFeatures.PALE_OAK, ConfiguredFeature(
                Feature.TREE, TreeConfigurationBuilder(
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
                Feature.TREE, TreeConfigurationBuilder(
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
                Feature.TREE, TreeConfigurationBuilder(
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

        context.register(
            ModConfiguredFeatures.PATCH_LEAF_LITTER, ConfiguredFeature(
                Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
                    32, PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        SimpleBlockConfiguration(WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        BlockPredicate.allOf(
                            BlockPredicate.ONLY_IN_AIR_PREDICATE,
                            BlockPredicate.matchesBlocks(Direction.DOWN.normal, Blocks.GRASS_BLOCK)
                        )
                    )
                )
            )
        )
        context.register(
            ModConfiguredFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BIRCH_TREE), 0.0025f
                        ), WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.BIRCH_BEES_0002_LEAF_LITTER), 0.2f
                        ), WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER), 0.1f
                        ), WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f
                        )
                    ), placedFeatures.getOrThrow(ModPlacedFeatures.OAK_BEES_0002_LEAF_LITTER)
                )
            )
        )
        context.register(
            VegetationFeatures.DARK_FOREST_VEGETATION, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)),
                            0.025f
                        ),
                        WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)),
                            0.05f
                        ),
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.DARK_OAK_LEAF_LITTER), 0.6666667f
                        ),
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BIRCH_TREE), 0.0025f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.BIRCH_LEAF_LITTER), 0.2f),
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f
                        ),
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FANCY_OAK_LEAF_LITTER), 0.1f
                        ),
                    ), placedFeatures.getOrThrow(ModPlacedFeatures.OAK_LEAF_LITTER)
                )
            )
        )
        context.register(
            ModConfiguredFeatures.TREES_BADLANDS, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f
                        ),
                    ), placedFeatures.getOrThrow(ModPlacedFeatures.OAK_LEAF_LITTER)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_SAVANNA, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.ACACIA_CHECKED), 0.8f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.OAK_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_WINDSWEPT_HILLS, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SPRUCE_TREE), 0.008325f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED), 0.666f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.OAK_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_PLAINS, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_BEES_005)),
                            0.33333334f
                        ), WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_OAK_TREE), 0.0125f)
                    ), PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.OAK_BEES_005))
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_FLOWER_FOREST, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BIRCH_TREE), 0.0025f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_002), 0.2f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_BEES_002), 0.1f)
                    ), placedFeatures.getOrThrow(TreePlacements.OAK_BEES_002)
                )
            )
        )
        context.register(
            ModConfiguredFeatures.TREES_BIRCH, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BIRCH_TREE), 0.0125f
                        )
                    ), placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED)
                )
            )
        )
        context.register(
            VegetationFeatures.BIRCH_TALL, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SUPER_BIRCH_TREE), 0.00625f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.SUPER_BIRCH_BEES_0002), 0.5f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BIRCH_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_SPARSE_JUNGLE, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.JUNGLE_BUSH), 0.5f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_JUNGLE_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.JUNGLE_TREE_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_JUNGLE, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.JUNGLE_BUSH), 0.5f),
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(TreePlacements.MEGA_JUNGLE_TREE_CHECKED), 0.33333334f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_JUNGLE_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.JUNGLE_TREE_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_TAIGA, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_CHECKED), 0.33333334f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SPRUCE_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                )
            )
        )
        context.register(
            ModConfiguredFeatures.TREES_SNOWY, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SPRUCE_TREE), 0.0125f
                        )
                    ), placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED), 0.33333334f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_CHECKED), 0.33333334f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SPRUCE_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                )
            )
        )
        context.register(
            VegetationFeatures.TREES_OLD_GROWTH_PINE_TAIGA, ConfiguredFeature(
                Feature.RANDOM_SELECTOR, RandomFeatureConfiguration(
                    listOf(
                        WeightedPlacedFeature(
                            placedFeatures.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED), 0.025641026f
                        ),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.MEGA_PINE_CHECKED), 0.30769232f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_CHECKED), 0.33333334f),
                        WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_SPRUCE_TREE), 0.0125f)
                    ), placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                )
            )
        )

        context.register(
            ModConfiguredFeatures.OAK_BEES_0002_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createOak().decorators(
                    listOf(
                        BeehiveDecorator(0.002F),
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.BIRCH_BEES_0002_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createBirch().decorators(
                    listOf(
                        BeehiveDecorator(0.002F),
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createFancyOak().decorators(
                    listOf(
                        BeehiveDecorator(0.002F),
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.OAK_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createOak().decorators(
                    listOf(
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.DARK_OAK_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, createDarkOak().ignoreVines().decorators(
                    listOf(
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.BIRCH_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createBirch().decorators(
                    listOf(
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )
        context.register(
            ModConfiguredFeatures.FANCY_OAK_LEAF_LITTER, ConfiguredFeature(
                Feature.TREE, TreeFeatures.createFancyOak().decorators(
                    listOf(
                        PlaceOnGroundDecorator(96, 4, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                        PlaceOnGroundDecorator(150, 2, 2, WeightedStateProvider(leafLitterPatchBuilder(1, 4))),
                    )
                ).build()
            )
        )

        context.register(
            ModConfiguredFeatures.FALLEN_OAK_TREE, ConfiguredFeature(
                ModFeatures.FALLEN_TREE.get(), createFallenOak().build()
            )
        )
        context.register(
            ModConfiguredFeatures.FALLEN_BIRCH_TREE, ConfiguredFeature(
                ModFeatures.FALLEN_TREE.get(), createFallenBirch(8).build()
            )
        )
        context.register(
            ModConfiguredFeatures.FALLEN_SUPER_BIRCH_TREE, ConfiguredFeature(
                ModFeatures.FALLEN_TREE.get(), createFallenBirch(15).build()
            )
        )
        context.register(
            ModConfiguredFeatures.FALLEN_JUNGLE_TREE, ConfiguredFeature(
                ModFeatures.FALLEN_TREE.get(), createFallenJungle().build()
            )
        )
        context.register(
            ModConfiguredFeatures.FALLEN_SPRUCE_TREE, ConfiguredFeature(
                ModFeatures.FALLEN_TREE.get(), createFallenSpruce().build()
            )
        )

        context.register(
            ModConfiguredFeatures.WILDFLOWERS_BIRCH_FOREST, ConfiguredFeature(
                Feature.FLOWER, RandomPatchConfiguration(
                    64, 6, 2, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(
                            WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))
                        )
                    )
                )
            )
        )
        context.register(
            ModConfiguredFeatures.WILDFLOWERS_MEADOW, ConfiguredFeature(
                Feature.FLOWER, RandomPatchConfiguration(
                    8, 6, 2, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(
                            WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))
                        )
                    )
                )
            )
        )

        context.register(
            ModConfiguredFeatures.PATCH_BUSH, ConfiguredFeature(
                Feature.RANDOM_PATCH, RandomPatchConfiguration(
                    24, 5, 3, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BUSH.get()))
                    )
                )
            )
        )
        context.register(
            ModConfiguredFeatures.PATCH_FIREFLY_BUSH, ConfiguredFeature(
                Feature.RANDOM_PATCH, RandomPatchConfiguration(
                    20, 4, 3, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK,
                        SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.FIREFLY_BUSH.get()))
                    )
                )
            )
        )

    }

    private fun createDarkOak(): TreeConfigurationBuilder {
        return TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
            DarkOakTrunkPlacer(6, 2, 1),
            BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
            DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
            ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
        )
    }

    private fun leafLitterPatchBuilder(minAmount: Int, maxAmount: Int): SimpleWeightedRandomList.Builder<BlockState> {
        return segmentedBlockPatchBuilder(
            ModBlocks.LEAF_LITTER.get(), minAmount, maxAmount, LeafLitterBlock.AMOUNT, LeafLitterBlock.FACING
        )
    }

    private fun flowerBedPatchBuilder(block: Block): SimpleWeightedRandomList.Builder<BlockState> {
        return segmentedBlockPatchBuilder(block, 1, 4, FlowerBedBlock.AMOUNT, FlowerBedBlock.FACING)
    }

    private fun segmentedBlockPatchBuilder(
        block: Block,
        minAmount: Int,
        maxAmount: Int,
        amountProperty: IntegerProperty,
        directionProperty: EnumProperty<Direction>
    ): SimpleWeightedRandomList.Builder<BlockState> {
        val builder = SimpleWeightedRandomList.builder<BlockState>()
        for (i in minAmount..maxAmount) {
            for (direction in Direction.Plane.HORIZONTAL) {
                builder.add(
                    block.defaultBlockState().setValue(amountProperty, i).setValue(directionProperty, direction), 1
                )
            }
        }
        return builder
    }

    private fun createFallenOak(): FallenTreeConfiguration.FallenTreeConfigurationBuilder {
        return createFallenTrees(
            Blocks.OAK_LOG, 4, 7
        ).stumpDecorators(ImmutableList.of(TrunkVineDecorator.INSTANCE))
    }

    private fun createFallenBirch(maxLength: Int): FallenTreeConfiguration.FallenTreeConfigurationBuilder {
        return createFallenTrees(Blocks.BIRCH_LOG, 5, maxLength)
    }

    private fun createFallenJungle(): FallenTreeConfiguration.FallenTreeConfigurationBuilder {
        return createFallenTrees(
            Blocks.JUNGLE_LOG, 4, 11
        ).stumpDecorators(ImmutableList.of(TrunkVineDecorator.INSTANCE))
    }

    private fun createFallenSpruce(): FallenTreeConfiguration.FallenTreeConfigurationBuilder {
        return createFallenTrees(Blocks.SPRUCE_LOG, 6, 10)
    }

    private fun createFallenTrees(
        logBlock: Block, minLength: Int, maxLength: Int
    ): FallenTreeConfiguration.FallenTreeConfigurationBuilder {
        return FallenTreeConfiguration.FallenTreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock), UniformInt.of(minLength, maxLength)
        ).logDecorators(
            ImmutableList.of(
                AttachedToLogsDecorator(
                    0.1f, WeightedStateProvider(
                        SimpleWeightedRandomList.builder<BlockState>().add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
                            .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1).build()
                    ), mutableListOf(Direction.UP)
                )
            )
        )
    }

}