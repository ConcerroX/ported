package concerrox.ported.mixininterface

import concerrox.ported.client.gui.ItemSlotMouseAction
import net.minecraft.world.inventory.Slot

interface AbstractContainerScreenExtensions {

    var itemSlotMouseActions: List<ItemSlotMouseAction>
    val lastHoveredSlot: Slot

}