package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object ModPlacedFeatures {

    val PALE_GARDEN_VEGETATION = create("pale_garden_vegetation")
    val PALE_MOSS_PATCH = create("pale_moss_patch")
    val PALE_GARDEN_FLOWERS = create("pale_garden_flowers")
    val FLOWER_PALE_GARDEN = create("flower_pale_garden")
    val PALE_OAK_CREAKING_CHECKED = create("pale_oak_creaking_checked")
    val PALE_OAK_CHECKED = create("pale_oak_checked")

    val OAK_BEES_0002_LEAF_LITTER = create("oak_bees_0002_leaf_litter")
    val BIRCH_BEES_0002_LEAF_LITTER = create("birch_bees_0002_leaf_litter")
    val FANCY_OAK_BEES_0002_LEAF_LITTER = create("fancy_oak_bees_0002_leaf_litter")
    val OAK_LEAF_LITTER = create("oak_leaf_litter")
    val DARK_OAK_LEAF_LITTER = create("dark_oak_leaf_litter")
    val BIRCH_LEAF_LITTER = create("birch_leaf_litter")
    val FANCY_OAK_LEAF_LITTER = create("fancy_oak_leaf_litter")

    val PATCH_LEAF_LITTER = create("patch_leaf_litter")
    val TREES_BIRCH_AND_OAK_LEAF_LITTER = create("trees_birch_and_oak_leaf_litter")

    val FALLEN_OAK_TREE = create("fallen_oak_tree")
    val FALLEN_JUNGLE_TREE = create("fallen_jungle_tree")
    val FALLEN_SPRUCE_TREE = create("fallen_spruce_tree")
    val FALLEN_BIRCH_TREE = create("fallen_birch_tree")
    val FALLEN_SUPER_BIRCH_TREE = create("fallen_super_birch_tree")

    private fun create(path: String): ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.withDefaultNamespace(path))

}