package concerrox.ported.util

import concerrox.ported.mixininterface.BundleContentsExtensions
import concerrox.ported.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.BundleContents
import kotlin.reflect.cast

object BundleItemUtils {

    val BundleContents.extensions get() = BundleContentsExtensions::class.cast(this)
    private fun ItemStack.tryGetBundleContents() = getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
    private fun ItemStack.getBundleContents() = get(DataComponents.BUNDLE_CONTENTS)

    fun getByColor(color: DyeColor): BundleItem {
        return when (color) {
            DyeColor.WHITE -> ModItems.WHITE_BUNDLE
            DyeColor.ORANGE -> ModItems.ORANGE_BUNDLE
            DyeColor.MAGENTA -> ModItems.MAGENTA_BUNDLE
            DyeColor.LIGHT_BLUE -> ModItems.LIGHT_BLUE_BUNDLE
            DyeColor.YELLOW -> ModItems.YELLOW_BUNDLE
            DyeColor.LIME -> ModItems.LIME_BUNDLE
            DyeColor.PINK -> ModItems.PINK_BUNDLE
            DyeColor.GRAY -> ModItems.GRAY_BUNDLE
            DyeColor.LIGHT_GRAY -> ModItems.LIGHT_GRAY_BUNDLE
            DyeColor.CYAN -> ModItems.CYAN_BUNDLE
            DyeColor.BLUE -> ModItems.BLUE_BUNDLE
            DyeColor.BROWN -> ModItems.BROWN_BUNDLE
            DyeColor.GREEN -> ModItems.GREEN_BUNDLE
            DyeColor.RED -> ModItems.RED_BUNDLE
            DyeColor.BLACK -> ModItems.BLACK_BUNDLE
            DyeColor.PURPLE -> ModItems.PURPLE_BUNDLE
        }.get()
    }

    fun getNumberOfItemsToShow(bundle: ItemStack) = bundle.tryGetBundleContents().extensions.getNumberOfItemsToShow()

    fun getSelectedItem(bundle: ItemStack) = bundle.tryGetBundleContents().extensions.selectedItem

    fun getSelectedItemStack(bundle: ItemStack): ItemStack {
        val contents = bundle.getBundleContents()
        val extensions = contents?.extensions
        return if (contents != null && extensions!!.selectedItem != -1) {
            contents.getItemUnsafe(extensions.selectedItem)
        } else ItemStack.EMPTY
    }

    fun toggleSelectedItem(bundle: ItemStack, selectedItem: Int) {
        val contents = bundle.getBundleContents()
        if (contents != null) {
            val mutable = BundleContents.Mutable(contents)
            mutable.toggleSelectedItem(selectedItem)
            bundle.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutableWithSelectedItem())
        }
    }

    fun hasSelectedItem(bundle: ItemStack): Boolean {
        return bundle.tryGetBundleContents().extensions.selectedItem != -1
    }

}