package concerrox.ported.registry

import concerrox.ported.recipe.TransmuteRecipe
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

    val RECIPE_SERIALIZERS: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, ResourceLocation.DEFAULT_NAMESPACE)

    val TRANSMUTE = RECIPE_SERIALIZERS.new("transmute", TransmuteRecipe::Serializer)

}