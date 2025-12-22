package concerrox.ported.content.thegardenawakens.creaking

import concerrox.ported.registry.ModModelLayers
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CreakingRenderer(context: EntityRendererProvider.Context) : MobRenderer<Creaking, CreakingModel>(
    context, CreakingModel(context.bakeLayer(ModModelLayers.CREAKING)), 0.6f
) {

    companion object {
        private val TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking.png")
    }

    init {
        addLayer(CreakingEyesLayer(this))
    }

    override fun getTextureLocation(creaking: Creaking): ResourceLocation = TEXTURE_LOCATION

    private class CreakingEyesLayer(renderer: RenderLayerParent<Creaking, CreakingModel>) :
        EyesLayer<Creaking, CreakingModel>(renderer) {

        companion object {
            val CREAKING_EYES: RenderType =
                RenderType.eyes(ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png"))
        }

        override fun renderType() = CREAKING_EYES

    }

}