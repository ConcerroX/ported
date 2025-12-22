package concerrox.ported.integration.emi

import concerrox.ported.Ported
import concerrox.ported.content.bundlesofbravery.transmute.TransmuteRecipe
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

@EmiEntrypoint
class PortedEmiPlugin : EmiPlugin {

    override fun register(registry: EmiRegistry) {
        for (recipe in getRecipes(registry, RecipeType.CRAFTING)) {
            if (recipe is TransmuteRecipe && recipe.canCraftInDimensions(3, 3)) {
                addRecipeSafe(registry, { EmiTransmuteRecipe(recipe) }, recipe)
            }
        }
    }

    private fun addRecipeSafe(registry: EmiRegistry, supplier: Supplier<EmiRecipe>, recipe: Recipe<*>) {
        try {
            registry.addRecipe(supplier.get())
        } catch (e: Throwable) {
            Ported.LOGGER.warn("Exception thrown when parsing recipe " + EmiPort.getId(recipe), e)
        }
    }

    private fun <C : RecipeInput, T : Recipe<C>> getRecipes(registry: EmiRegistry, type: RecipeType<T>) = Iterable {
        registry.recipeManager.getAllRecipesFor(type).map { it.value() }.iterator()
    }

}