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

    private fun create(path: String): ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace(path))

}