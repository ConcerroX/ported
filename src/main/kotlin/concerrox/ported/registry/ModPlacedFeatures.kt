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

    private fun create(path: String): ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.withDefaultNamespace(path))

}