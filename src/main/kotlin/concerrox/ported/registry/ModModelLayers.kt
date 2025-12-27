package concerrox.ported.registry

import concerrox.ported.content.thegardenawakens.paleoak.PaleOakBoatRenderer
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation

object ModModelLayers {

    val CREAKING = new("creaking")
    val PALE_OAK_BOAT = PaleOakBoatRenderer.createBoatModelName()
    val PALE_OAK_CHEST_BOAT = PaleOakBoatRenderer.createChestBoatModelName()

    val COLD_CHICKEN = new("cold_chicken")

    private fun new(path: String) = ModelLayerLocation(ResourceLocation.withDefaultNamespace(path), "main")

}