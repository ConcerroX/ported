package concerrox.ported.content.springtolife.mobvariant

import concerrox.ported.content.springtolife.mobvariant.MobVariantManager.TextureModelData
import concerrox.ported.content.springtolife.mobvariant.model.ColdChickenModel
import concerrox.ported.content.springtolife.mobvariant.model.ColdCowModel
import concerrox.ported.content.springtolife.mobvariant.model.ColdPigModel
import concerrox.ported.content.springtolife.mobvariant.model.CowModel
import concerrox.ported.registry.ModBiomeTags
import concerrox.ported.registry.ModModelLayers
import net.minecraft.client.model.ChickenModel
import net.minecraft.client.model.PigModel
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

        MobVariantManager.addVariants(
            EntityType.PIG,
            MobVariant(
                ResourceLocation.withDefaultNamespace("temperate_pig"),
                TextureModelData(
                    ::PigModel,
                    "pig",
                    ResourceLocation.withDefaultNamespace("textures/entity/pig/temperate_pig.png"),
                ),
                null,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("cold_pig"),
                TextureModelData(
                    ::ColdPigModel,
                    ModModelLayers.COLD_PIG,
                    ModModelLayers.COLD_PIG,
                    ResourceLocation.withDefaultNamespace("textures/entity/pig/cold_pig.png"),
                ),
                ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("warm_pig"),
                TextureModelData(
                    ::PigModel,
                    "pig",
                    ResourceLocation.withDefaultNamespace("textures/entity/pig/warm_pig.png"),
                ),
                ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS,
            ),
        )

        MobVariantManager.addVariants(
            EntityType.COW,
            MobVariant(
                ResourceLocation.withDefaultNamespace("temperate_cow"),
                TextureModelData(
                    ::CowModel,
                    ModModelLayers.COW_NEW,
                    ModModelLayers.COW_NEW,
                    ResourceLocation.withDefaultNamespace("textures/entity/cow/temperate_cow.png"),
                ),
                null,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("cold_cow"),
                TextureModelData(
                    ::ColdCowModel,
                    ModModelLayers.COLD_COW,
                    ModModelLayers.COLD_COW,
                    ResourceLocation.withDefaultNamespace("textures/entity/cow/cold_cow.png"),
                ),
                ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS,
            ),
            MobVariant(
                ResourceLocation.withDefaultNamespace("warm_cow"),
                TextureModelData(
                    ::CowModel,
                    ModModelLayers.COW_NEW,
                    ModModelLayers.COW_NEW,
                    ResourceLocation.withDefaultNamespace("textures/entity/cow/warm_cow.png"),
                ),
                ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS,
            ),
        )
    }

}