package concerrox.ported.event

import concerrox.ported.data.ModItemTagsProvider
import concerrox.ported.registry.ModItems
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object CommonModEventHandler {

    @SubscribeEvent
    fun onBuildCreativeModeTabContents(event: BuildCreativeModeTabContentsEvent) {
        when (event.tabKey) {
            CreativeModeTabs.INGREDIENTS -> {
                event.insertBefore(
                    Items.FLOWER_BANNER_PATTERN.defaultInstance,
                    ModItems.FIELD_MASONED_BANNER_PATTERN.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertBefore(
                    Items.FLOWER_BANNER_PATTERN.defaultInstance,
                    ModItems.BORDURE_INDENTED_BANNER_PATTERN.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
            }

            CreativeModeTabs.TOOLS_AND_UTILITIES -> {
                ModItemTagsProvider.BUNDLES.reversed().forEach {
                    event.insertAfter(
                        Items.LEAD.defaultInstance,
                        ItemStack(it),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    )
                }
            }
        }
    }

}