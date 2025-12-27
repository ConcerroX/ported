package concerrox.ported.content.springtolife.mobvariant

import concerrox.ported.content.springtolife.mobvariant.MobVariantManager.TextureModelData
import concerrox.ported.content.springtolife.mobvariant.model.ColdChickenModel
import concerrox.ported.registry.ModBiomeTags
import concerrox.ported.registry.ModModelLayers
import net.minecraft.client.model.ChickenModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

object VanillaMobVariants {

    internal fun register() {
        MobVariantManager.addVariants(
            EntityType.CHICKEN,
            MobVariant(
                ResourceLocation.withDefaultNamespace("temperate_chicken"),
                TextureModelData(
                    ::ChickenModel,
                    "chicken",
                    ResourceLocation.withDefaultNamespace("textures/entity/chicken/temperate_chicken.png"),
                ),
                null,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("cold_chicken"),
                TextureModelData(
                    ::ColdChickenModel,
                    ModModelLayers.COLD_CHICKEN,
                    ModModelLayers.COLD_CHICKEN,
                    ResourceLocation.withDefaultNamespace("textures/entity/chicken/cold_chicken.png"),
                ),
                ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("warm_chicken"),
                TextureModelData(
                    ::ChickenModel,
                    "chicken",
                    ResourceLocation.withDefaultNamespace("textures/entity/chicken/warm_chicken.png"),
                ),
                ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS,
            ),
        )
    }

}