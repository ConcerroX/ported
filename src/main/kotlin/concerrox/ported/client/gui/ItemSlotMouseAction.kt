package concerrox.ported.client.gui

import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface ItemSlotMouseAction {

    fun matches(slot: Slot): Boolean
    fun onMouseScrolled(scrollX: Double, scrollY: Double, var5: Int, stack: ItemStack): Boolean
    fun onStopHovering(slot: Slot)
    fun onSlotClicked(slot: Slot, clickType: ClickType)

}
