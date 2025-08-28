package concerrox.ported.data

import concerrox.ported.recipe.TransmuteRecipeBuilder
import concerrox.ported.registry.ModItemTags
import concerrox.ported.registry.ModItems
import concerrox.ported.util.BundleItemUtils
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    private val output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries) {

    override fun buildRecipes(recipeOutput: RecipeOutput) {
        shapeless(RecipeCategory.MISC, ModItems.FIELD_MASONED_BANNER_PATTERN).requires(Items.PAPER)
            .requires(Blocks.BRICKS).unlockedBy("has_bricks", has(Blocks.BRICKS)).save(recipeOutput)
        shapeless(RecipeCategory.MISC, ModItems.BORDURE_INDENTED_BANNER_PATTERN).requires(Items.PAPER)
            .requires(Blocks.VINE).unlockedBy("has_vines", has(Blocks.VINE)).save(recipeOutput)
        shaped(RecipeCategory.TOOLS, Items.BUNDLE).define('-', Items.STRING).define('#', Items.LEATHER).pattern("-")
            .pattern("#").unlockedBy("has_string", has(Items.STRING)).save(recipeOutput)
        bundleRecipes(recipeOutput)
    }

    private fun shaped(category: RecipeCategory, result: ItemLike): ShapedRecipeBuilder {
        return ShapedRecipeBuilder.shaped(category, result)
    }

    private fun shapeless(category: RecipeCategory, result: ItemLike): ShapelessRecipeBuilder {
        return ShapelessRecipeBuilder.shapeless(category, result)
    }

    private fun tag(tag: TagKey<Item>): Ingredient {
        return Ingredient.of(tag)
    }

    private fun bundleRecipes(recipeOutput: RecipeOutput) {
        val ingredient = tag(ModItemTags.BUNDLES)
        for (dyeColor in DyeColor.entries) {
            val dyeItem = DyeItem.byColor(dyeColor)
            TransmuteRecipeBuilder.transmute(
                RecipeCategory.TOOLS, ingredient, Ingredient.of(dyeItem), BundleItemUtils.getByColor(dyeColor)
            ).group("bundle_dye").unlockedBy(getHasName(dyeItem), has(dyeItem)).save(recipeOutput)
        }
    }

}