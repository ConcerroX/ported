@file:JvmName("ItemStackUtils")

package concerrox.ported.util

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

fun ItemStack.getStyledHoverName(): Component {
    val mutableComponent = Component.empty().append(getHoverName()).withStyle(getRarity().styleModifier)
    if (has(DataComponents.CUSTOM_NAME)) {
        mutableComponent.withStyle(ChatFormatting.ITALIC)
    }
    return mutableComponent
}