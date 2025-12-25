package concerrox.ported.data

import concerrox.ported.content.bundlesofbravery.bundle.BundleItemUtils
import concerrox.ported.content.bundlesofbravery.transmute.TransmuteRecipeBuilder
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModItemTags
import concerrox.ported.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.BlockFamily
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlags
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

        shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART).define('R', ModItems.RESIN_BLOCK)
            .define('L', ModBlocks.PALE_OAK_LOG).pattern(" L ").pattern(" R ").pattern(" L ")
            .unlockedBy("has_resin_block", has(ModBlocks.RESIN_BLOCK)).save(recipeOutput)
        carpet(recipeOutput, ModBlocks.PALE_MOSS_CARPET, ModBlocks.PALE_MOSS_BLOCK)
        woodFromLogs(recipeOutput, ModBlocks.PALE_OAK_WOOD, ModBlocks.PALE_OAK_LOG)
        woodFromLogs(recipeOutput, ModBlocks.STRIPPED_PALE_OAK_WOOD, ModBlocks.STRIPPED_PALE_OAK_LOG)
        planksFromLog(recipeOutput, ModBlocks.PALE_OAK_PLANKS, ModItemTags.PALE_OAK_LOGS, 4)
        generateRecipes(
            recipeOutput,
            BlockFamily.Builder(ModBlocks.PALE_OAK_PLANKS.get()).stairs(ModBlocks.PALE_OAK_STAIRS.get())
                .slab(ModBlocks.PALE_OAK_SLAB.get()).fence(ModBlocks.PALE_OAK_FENCE.get())
                .fenceGate(ModBlocks.PALE_OAK_FENCE_GATE.get()).door(ModBlocks.PALE_OAK_DOOR.get())
                .trapdoor(ModBlocks.PALE_OAK_TRAPDOOR.get()).pressurePlate(ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
                .button(ModBlocks.PALE_OAK_BUTTON.get())
                .sign(ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()).family,
            FeatureFlags.VANILLA_SET
        )
        hangingSign(recipeOutput, ModItems.PALE_OAK_HANGING_SIGN, ModBlocks.STRIPPED_PALE_OAK_LOG)
        woodenBoat(recipeOutput, ModItems.PALE_OAK_BOAT, ModBlocks.PALE_OAK_PLANKS)
        chestBoat(recipeOutput, ModItems.PALE_OAK_CHEST_BOAT, ModItems.PALE_OAK_BOAT)

        nineBlockStorageRecipes(
            recipeOutput,
            RecipeCategory.MISC,
            ModItems.RESIN_CLUMP,
            RecipeCategory.BUILDING_BLOCKS,
            ModBlocks.RESIN_BLOCK
        )
        twoByTwoPacker(
            recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICKS, ModItems.RESIN_BRICK
        )
        generateRecipes(
            recipeOutput,
            BlockFamily.Builder(ModBlocks.RESIN_BLOCK.get()).stairs(ModBlocks.RESIN_BRICK_STAIRS.get())
                .slab(ModBlocks.RESIN_BRICK_SLAB.get()).wall(ModBlocks.RESIN_BRICK_WALL.get())
                .chiseled(ModBlocks.CHISELED_RESIN_BRICKS.get()).family,
            FeatureFlags.VANILLA_SET
        )
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(ModItems.RESIN_CLUMP), RecipeCategory.MISC, ModItems.RESIN_BRICK, 0.1f, 200
        ).unlockedBy("has_resin_clump", has(ModItems.RESIN_CLUMP)).save(recipeOutput)

        stonecutterResultFromBase(
            recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_SLAB, ModBlocks.RESIN_BRICKS, 2
        )
        stonecutterResultFromBase(
            recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_STAIRS, ModBlocks.RESIN_BRICKS
        )
        stonecutterResultFromBase(
            recipeOutput, RecipeCategory.DECORATIONS, ModBlocks.RESIN_BRICK_WALL, ModBlocks.RESIN_BRICKS
        )
        stonecutterResultFromBase(
            recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RESIN_BRICKS, ModBlocks.RESIN_BRICKS
        )

        oneToOneConversionRecipe(recipeOutput, Items.ORANGE_DYE, ModBlocks.OPEN_EYEBLOSSOM, "orange_dye")
        oneToOneConversionRecipe(recipeOutput, Items.GRAY_DYE, ModBlocks.CLOSED_EYEBLOSSOM, "gray_dye")

        SimpleCookingRecipeBuilder.smelting(
            tag(ItemTags.LEAVES), RecipeCategory.MISC, ModBlocks.LEAF_LITTER, 0.1f, 200
        ).unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(recipeOutput)


        // Ported
        shaped(RecipeCategory.MISC, ModItems.NETHER_REACTOR_CORE).pattern("IDI").pattern("IDI").pattern("IDI")
            .define('I', Items.IRON_INGOT).define('D', Items.DIAMOND).unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(recipeOutput)
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