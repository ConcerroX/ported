package concerrox.ported.content.bundlesofbravery.transmute

import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

class TransmuteRecipeBuilder private constructor(
    private val category: RecipeCategory,
    private val result: Holder<Item>,
    private val input: Ingredient,
    private val material: Ingredient
) : RecipeBuilder {
    private val criteria = LinkedHashMap<String, Criterion<*>>()
    private var group: String? = null

    override fun unlockedBy(string: String, criterion: Criterion<*>): TransmuteRecipeBuilder {
        criteria[string] = criterion
        return this
    }

    override fun group(string: String?): TransmuteRecipeBuilder {
        this.group = string
        return this
    }

    override fun getResult(): Item {
        return result.value()
    }

    override fun save(output: RecipeOutput, location: ResourceLocation) {
        ensureValid(location)
        val advancementBuilder =
            output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location)).requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach { (key, criterion) -> advancementBuilder.addCriterion(key, criterion) }
        val recipe = TransmuteRecipe(
            group ?: "", RecipeBuilder.determineBookCategory(category), input, material, TransmuteResult(result.value())
        )
        output.accept(
            location, recipe, advancementBuilder.build(location.withPrefix("recipes/" + category.folderName + "/"))
        )
    }

    private fun ensureValid(location: ResourceLocation) {
        check(!criteria.isEmpty()) { "No way of obtaining recipe $location" }
    }

    companion object {
        @Suppress("DEPRECATION")
        fun transmute(
            category: RecipeCategory, input: Ingredient, material: Ingredient, result: Item
        ): TransmuteRecipeBuilder {
            return TransmuteRecipeBuilder(category, result.builtInRegistryHolder(), input, material)
        }
    }
}