package concerrox.ported.util

import net.minecraft.world.inventory.AbstractContainerMenu

fun AbstractContainerMenu.setSelectedBundleItemIndex(slotIndex: Int, bundleItemIndex: Int) {
    if (slotIndex >= 0 && slotIndex < slots.size) {
        val stack = this.slots[slotIndex].item
        BundleItemUtils.toggleSelectedItem(stack, bundleItemIndex)
    }
}