package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModItemTags {

    val BUNDLES = create("bundles")
    val GAZE_DISGUISE_EQUIPMENT = create("gaze_disguise_equipment")

    val PALE_OAK_LOGS = create("pale_oak_logs")

    private fun create(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name))
    }

}