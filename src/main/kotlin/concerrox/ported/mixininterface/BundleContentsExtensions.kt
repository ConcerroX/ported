package concerrox.ported.mixininterface

interface BundleContentsExtensions {

    var selectedItem: Int
    fun hasSelectedItem(): Boolean
    fun getNumberOfItemsToShow(): Int

}