package concerrox.ported.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BannerPatternItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {

    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(ResourceLocation.DEFAULT_NAMESPACE)

    // 1.21.2
    val FIELD_MASONED_BANNER_PATTERN = new("field_masoned_banner_pattern") {
        BannerPatternItem(ModBannerPatternTags.PATTERN_ITEM_FIELD_MASONED, newProperties { stacksTo(1) })
    }
    val BORDURE_INDENTED_BANNER_PATTERN = new("bordure_indented_banner_pattern") {
        BannerPatternItem(ModBannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED, newProperties { stacksTo(1) })
    }

    private fun <R : Item> new(path: String, content: Supplier<R>): DeferredItem<R> = ITEMS.register(path, content)
    private fun newProperties(builder: Item.Properties.() -> Unit) = Item.Properties().apply(builder)

}