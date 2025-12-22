package concerrox.ported.content.thegardenawakens.paleoak

import com.mojang.datafixers.util.Pair
import net.minecraft.client.model.*
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.BoatRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.vehicle.Boat

class PaleOakBoatRenderer(private val context: EntityRendererProvider.Context, private val chestBoat: Boolean) :
    BoatRenderer(
        context, chestBoat
    ) {

    companion object {
        fun createBoatModelName(): ModelLayerLocation {
            val location = ResourceLocation.parse("pale_oak")
            return ModelLayerLocation(location.withPrefix("boat/"), "main")
        }

        fun createChestBoatModelName(): ModelLayerLocation {
            val location = ResourceLocation.parse("pale_oak")
            return ModelLayerLocation(location.withPrefix("chest_boat/"), "main")
        }
    }

    private val textureToModel = Pair.of(
        getPaleOakTextureLocation(chestBoat), createBoatModel(context, chestBoat)
    )

    override fun getModelWithLocation(boat: Boat): Pair<ResourceLocation, ListModel<Boat>> {
        return textureToModel
    }

    private fun createBoatModel(
        context: EntityRendererProvider.Context, chestBoat: Boolean
    ): ListModel<Boat> {
        val modelLayerLocation =
            if (chestBoat) createChestBoatModelName() else createBoatModelName()
        val modelPart = context.bakeLayer(modelLayerLocation)
        return (if (chestBoat) ChestBoatModel(modelPart) else BoatModel(modelPart))
    }

    private fun getPaleOakTextureLocation(chestBoat: Boolean): ResourceLocation {
        return if (chestBoat) ResourceLocation.parse("pale_oak").withPrefix("textures/entity/chest_boat/")
            .withSuffix(".png") else ResourceLocation.parse("pale_oak").withPrefix("textures/entity/boat/")
            .withSuffix(".png")
    }

}