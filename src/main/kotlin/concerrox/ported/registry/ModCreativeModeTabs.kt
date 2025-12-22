package concerrox.ported.registry

import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredRegister

object ModCreativeModeTabs {

    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.DEFAULT_NAMESPACE)

    val BACKPORTED = CREATIVE_MODE_TABS.new("backported") {
        CreativeModeTab.builder().title(Component.translatable("itemGroup.backported"))
            .withTabsBefore(CreativeModeTabs.COMBAT).icon { Items.BUNDLE.defaultInstance }.displayItems { _, output ->
                ModItems.ITEMS.entries.forEach { output.accept(it.get()) }
            }.build()
    }

    @Suppress("UNUSED")
    val PORTED = CREATIVE_MODE_TABS.new("ported") {
        CreativeModeTab.builder().title(Component.translatable("itemGroup.ported")).withTabsBefore(BACKPORTED.key)
            .icon { Items.BUNDLE.defaultInstance }.displayItems { _, output ->
                ModItems.PORTED_ITEMS.entries.forEach { output.accept(it.get()) }
            }.build()
    }

}