package concerrox.ported.data

import concerrox.ported.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries) {

    override fun buildRecipes(recipeOutput: RecipeOutput) {
        shapeless(RecipeCategory.MISC, ModItems.FIELD_MASONED_BANNER_PATTERN).requires(Items.PAPER)
            .requires(Blocks.BRICKS).unlockedBy("has_bricks", has(Blocks.BRICKS)).save(recipeOutput)
        shapeless(RecipeCategory.MISC, ModItems.BORDURE_INDENTED_BANNER_PATTERN).requires(Items.PAPER)
            .requires(Blocks.VINE).unlockedBy("has_vines", has(Blocks.VINE)).save(recipeOutput)
    }

    private fun shapeless(category: RecipeCategory, result: ItemLike): ShapelessRecipeBuilder {
        return ShapelessRecipeBuilder.shapeless(category, result)
    }

}