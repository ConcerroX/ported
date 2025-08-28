package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModItemTags {

    val BUNDLES = create("bundles")

    private fun create(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name))
    }

}