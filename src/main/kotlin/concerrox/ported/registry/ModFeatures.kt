package concerrox.ported.registry

import concerrox.ported.content.springtolife.fallentree.FallenTreeConfiguration
import concerrox.ported.content.springtolife.fallentree.FallenTreeFeature
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.Feature
import net.neoforged.neoforge.registries.DeferredRegister

object ModFeatures {

    val FEATURES: DeferredRegister<Feature<*>> =
        DeferredRegister.create(Registries.FEATURE, ResourceLocation.DEFAULT_NAMESPACE)

    val FALLEN_TREE = FEATURES.new("fallen_tree") {
        FallenTreeFeature(FallenTreeConfiguration.CODEC)
    }

}