package concerrox.ported.content.bundlesofbravery.bundle

import concerrox.ported.content.bundlesofbravery.bundle.BundleItemUtils.extensions
import concerrox.ported.mixininterface.BundleContentsMutableExtensions
import net.minecraft.world.item.component.BundleContents
import kotlin.reflect.cast

fun BundleContents.Mutable.toggleSelectedItem(toSelectedItem: Int) {
    val mutableExtensions = BundleContentsMutableExtensions::class.cast(this)
    mutableExtensions.selectedItem =
        if (mutableExtensions.selectedItem != toSelectedItem && !mutableExtensions.indexIsOutsideAllowedBounds(toSelectedItem)) toSelectedItem else -1
}

fun BundleContents.Mutable.toImmutableWithSelectedItem(): BundleContents {
    val mutableExtensions = BundleContentsMutableExtensions::class.cast(this)
    val selectedItem = mutableExtensions.selectedItem
    return toImmutable().apply {
        extensions.selectedItem = selectedItem
    }
}