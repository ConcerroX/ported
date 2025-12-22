package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome

object ModBiomes {

    val PALE_GARDEN = create("pale_garden")

    private fun create(path: String): ResourceKey<Biome> =
        ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(path))

}