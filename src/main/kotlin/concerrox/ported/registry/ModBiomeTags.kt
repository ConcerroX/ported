package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

object ModBiomeTags {

    val SPAWNS_WARM_VARIANT_FARM_ANIMALS = create("spawns_warm_variant_farm_animals")
    val SPAWNS_COLD_VARIANT_FARM_ANIMALS = create("spawns_cold_variant_farm_animals")

    private fun create(path: String): TagKey<Biome> =
        TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(path))

}