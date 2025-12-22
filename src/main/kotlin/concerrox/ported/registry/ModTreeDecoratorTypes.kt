package concerrox.ported.registry

import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartDecorator
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossDecorator
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.neoforged.neoforge.registries.DeferredRegister

object ModTreeDecoratorTypes {

    val TREE_DECORATOR_TYPES: DeferredRegister<TreeDecoratorType<*>> =
        DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

    val PALE_MOSS = TREE_DECORATOR_TYPES.new("pale_moss") {
        TreeDecoratorType(PaleMossDecorator.CODEC)
    }
    val CREAKING_HEART = TREE_DECORATOR_TYPES.new("creaking_heart") {
        TreeDecoratorType(CreakingHeartDecorator.CODEC)
    }

}