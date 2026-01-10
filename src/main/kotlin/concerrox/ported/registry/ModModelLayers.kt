package concerrox.ported.registry

import concerrox.ported.content.thegardenawakens.paleoak.PaleOakBoatRenderer
import concerrox.ported.id
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation

object ModModelLayers {

    val CREAKING = new("creaking")
    val PALE_OAK_BOAT = PaleOakBoatRenderer.createBoatModelName()
    val PALE_OAK_CHEST_BOAT = PaleOakBoatRenderer.createChestBoatModelName()

    val COLD_CHICKEN = new("cold_chicken")
    val COLD_PIG = new("cold_pig")
    val COW_NEW = ModelLayerLocation(id("cow_new"), "main")
    val COLD_COW = new("cold_cow")
    val MOOSHROOM_NEW = ModelLayerLocation(id("mooshroom_new"), "main")

    private fun new(path: String) = ModelLayerLocation(ResourceLocation.withDefaultNamespace(path), "main")

}