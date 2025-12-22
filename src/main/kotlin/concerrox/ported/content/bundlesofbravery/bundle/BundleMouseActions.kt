package concerrox.ported.content.bundlesofbravery.bundle

import concerrox.ported.gui.ItemSlotMouseAction
import concerrox.ported.gui.ScrollWheelHandler
import concerrox.ported.registry.ModItemTags
import net.minecraft.client.Minecraft
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.network.PacketDistributor

@OnlyIn(Dist.CLIENT)
class BundleMouseActions(private val minecraft: Minecraft) : ItemSlotMouseAction {

    private val scrollWheelHandler: ScrollWheelHandler = ScrollWheelHandler()

    override fun matches(slot: Slot): Boolean {
        return slot.item.`is`(ModItemTags.BUNDLES)
    }

    override fun onMouseScrolled(scrollX: Double, scrollY: Double, var5: Int, stack: ItemStack): Boolean {
        val numberOfItemsToShow = BundleItemUtils.getNumberOfItemsToShow(stack)
        if (numberOfItemsToShow == 0) return false

        val vector2i = scrollWheelHandler.onMouseScroll(scrollX, scrollY)
        val j = if (vector2i.y == 0) -vector2i.x else vector2i.y
        if (j != 0) {
            val selected = BundleItemUtils.getSelectedItem(stack)
            val nextSelected =
                ScrollWheelHandler.Companion.getNextScrollWheelSelection(j.toDouble(), selected, numberOfItemsToShow)
            if (selected != nextSelected) {
                toggleSelectedBundleItem(stack, var5, nextSelected)
            }
        }
        return true
    }

    override fun onStopHovering(slot: Slot) {
        unselectedBundleItem(slot.item, slot.index)
    }

    override fun onSlotClicked(slot: Slot, clickType: ClickType) {
        if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.SWAP) {
            unselectedBundleItem(slot.item, slot.index)
        }
    }

    private fun toggleSelectedBundleItem(stack: ItemStack, index: Int, nextIndex: Int) {
        if (minecraft.connection != null && nextIndex < BundleItemUtils.getNumberOfItemsToShow(stack)) {
            BundleItemUtils.toggleSelectedItem(stack, nextIndex)
            PacketDistributor.sendToServer(ServerboundSelectBundleItemPayload(index, nextIndex))
        }
    }

    fun unselectedBundleItem(bundle: ItemStack, slotIndex: Int) {
        toggleSelectedBundleItem(bundle, slotIndex, -1)
    }

}