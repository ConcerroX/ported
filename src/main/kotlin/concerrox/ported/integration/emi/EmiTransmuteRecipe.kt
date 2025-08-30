package concerrox.ported.integration.emi

import concerrox.ported.recipe.TransmuteRecipe
import dev.emi.emi.EmiPort
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.recipe.EmiShapedRecipe

class EmiTransmuteRecipe(recipe: TransmuteRecipe) : EmiCraftingRecipe(
    arrayOf(recipe.input, recipe.material).map(EmiIngredient::of),
    EmiStack.of(EmiPort.getOutput(recipe)),
    EmiPort.getId(recipe)
) {

    init {
        EmiShapedRecipe.setRemainders(input, recipe)
    }

    override fun canFit(width: Int, height: Int): Boolean {
        return input.size <= width * height
    }

}
