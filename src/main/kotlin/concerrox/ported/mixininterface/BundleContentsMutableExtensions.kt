package concerrox.ported.mixininterface

interface BundleContentsMutableExtensions {

    var selectedItem: Int
    fun indexIsOutsideAllowedBounds(index: Int): Boolean

}