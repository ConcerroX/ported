package concerrox.ported.util

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.RecipeType

object DyeColorUtils {

    fun getMixedColor(level: ServerLevel, first: DyeColor, second: DyeColor): DyeColor? {
        val input = makeCraftColorInput(first, second)
        return level.recipeManager.getRecipeFor(RecipeType.CRAFTING, input, level)
            .map { it.value().assemble(input, level.registryAccess()).item }.filter { it is DyeItem }
            .map { (it as DyeItem).dyeColor }.orElseGet { if (level.random.nextBoolean()) first else second }
    }

    private fun makeCraftColorInput(first: DyeColor, second: DyeColor): CraftingInput {
        return CraftingInput.of(2, 1, listOf(ItemStack(DyeItem.byColor(first)), ItemStack(DyeItem.byColor(second))))
    }

}