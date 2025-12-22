@file:JvmName("ItemStackUtils")

package concerrox.ported.util

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

internal fun ItemStack.getStyledHoverName(): Component {
    val component = Component.empty().append(hoverName).withStyle(rarity.styleModifier)
    if (has(DataComponents.CUSTOM_NAME)) component.withStyle(ChatFormatting.ITALIC)
    return component
}