package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

object ModConfiguredFeatures {

    val PALE_OAK = create("pale_oak")
    val PALE_OAK_CREAKING = create("pale_oak_creaking")
    val PALE_OAK_BONEMEAL = create("pale_oak_bonemeal")
    val PALE_MOSS_VEGETATION = create("pale_moss_vegetation")
    val PALE_MOSS_PATCH_BONEMEAL = create("pale_moss_patch_bonemeal")
    val PALE_GARDEN_VEGETATION = create("pale_garden_vegetation")
    val PALE_MOSS_PATCH = create("pale_moss_patch")
    val PALE_FOREST_FLOWERS = create("pale_forest_flowers")
    val FLOWER_PALE_GARDEN = create("flower_pale_garden")

    val OAK_BEES_0002_LEAF_LITTER = create("oak_bees_0002_leaf_litter")
    val BIRCH_BEES_0002_LEAF_LITTER = create("birch_bees_0002_leaf_litter")
    val FANCY_OAK_BEES_0002_LEAF_LITTER = create("fancy_oak_bees_0002_leaf_litter")
    val OAK_LEAF_LITTER = create("oak_leaf_litter")
    val DARK_OAK_LEAF_LITTER = create("dark_oak_leaf_litter")
    val BIRCH_LEAF_LITTER = create("birch_leaf_litter")
    val FANCY_OAK_LEAF_LITTER = create("fancy_oak_leaf_litter")

    val PATCH_LEAF_LITTER = create("patch_leaf_litter")
    val TREES_BIRCH_AND_OAK_LEAF_LITTER = create("trees_birch_and_oak_leaf_litter")
    val TREES_BADLANDS = create("trees_badlands")

    val FALLEN_OAK_TREE = create("fallen_oak_tree")
    val FALLEN_JUNGLE_TREE = create("fallen_jungle_tree")
    val FALLEN_SPRUCE_TREE = create("fallen_spruce_tree")
    val FALLEN_BIRCH_TREE = create("fallen_birch_tree")
    val FALLEN_SUPER_BIRCH_TREE = create("fallen_super_birch_tree")

    val TREES_BIRCH = create("trees_birch")
    val TREES_SNOWY = create("trees_snowy")

    val WILDFLOWERS_BIRCH_FOREST = create("wildflowers_birch_forest")
    val WILDFLOWERS_MEADOW = create("wildflowers_meadow")

    val PATCH_BUSH = create("patch_bush")
    val PATCH_FIREFLY_BUSH = create("patch_firefly_bush")

    val PATCH_DRY_GRASS = create("patch_dry_grass")

    private fun create(path: String): ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace(path))

}