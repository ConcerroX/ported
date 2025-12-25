package concerrox.ported.event

import concerrox.ported.content.thegardenawakens.creaking.Creaking
import concerrox.ported.data.ModItemTagsProvider
import concerrox.ported.registry.ModCreativeModeTabs
import concerrox.ported.registry.ModEntityTypes
import concerrox.ported.registry.ModItems
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent

@EventBusSubscriber
object CommonModEventHandler {

    @SubscribeEvent
    fun onBuildCreativeModeTabContents(event: BuildCreativeModeTabContentsEvent) {
        when (event.tabKey) {

            //TODO:check
            CreativeModeTabs.INGREDIENTS -> {
                event.insertAfter(
                    Items.PHANTOM_MEMBRANE.defaultInstance,
                    ModItems.BORDURE_INDENTED_BANNER_PATTERN.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.PHANTOM_MEMBRANE.defaultInstance,
                    ModItems.FIELD_MASONED_BANNER_PATTERN.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.HONEYCOMB.defaultInstance,
                    ModItems.RESIN_CLUMP.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.NETHER_BRICK.defaultInstance,
                    ModItems.RESIN_BRICK.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
            }

            CreativeModeTabs.TOOLS_AND_UTILITIES -> {
                ModItemTagsProvider.BUNDLES.reversed().forEach {
                    event.insertAfter(
                        Items.LEAD.defaultInstance, ItemStack(it), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    )
                }
            }

            CreativeModeTabs.SPAWN_EGGS -> {
                event.insertAfter(
                    Items.TRIAL_SPAWNER.defaultInstance,
                    ModItems.CREAKING_HEART.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.COW_SPAWN_EGG.defaultInstance,
                    ModItems.CREAKING_SPAWN_EGG.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
            }

            CreativeModeTabs.BUILDING_BLOCKS -> {
                arrayOf(
                    ModItems.PALE_OAK_LOG,
                    ModItems.PALE_OAK_WOOD,
                    ModItems.STRIPPED_PALE_OAK_LOG,
                    ModItems.STRIPPED_PALE_OAK_WOOD,
                    ModItems.PALE_OAK_PLANKS,
                    ModItems.PALE_OAK_STAIRS,
                    ModItems.PALE_OAK_SLAB,
                    ModItems.PALE_OAK_FENCE,
                    ModItems.PALE_OAK_FENCE_GATE,
                    ModItems.PALE_OAK_DOOR,
                    ModItems.PALE_OAK_TRAPDOOR,
                    ModItems.PALE_OAK_PRESSURE_PLATE,
                    ModItems.PALE_OAK_BUTTON
                ).reversed().forEach {
                    event.insertAfter(
                        Items.CHERRY_BUTTON.defaultInstance,
                        it.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    )
                }

                arrayOf(
                    ModItems.RESIN_BRICKS,
                    ModItems.RESIN_BRICK_STAIRS,
                    ModItems.RESIN_BRICK_SLAB,
                    ModItems.RESIN_BRICK_WALL,
                    ModItems.CHISELED_RESIN_BRICKS,
                ).reversed().forEach {
                    event.insertAfter(
                        Items.MUD_BRICK_WALL.defaultInstance,
                        it.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    )
                }
            }

            CreativeModeTabs.NATURAL_BLOCKS -> {
                arrayOf(
                    ModItems.PALE_MOSS_BLOCK,
                    ModItems.PALE_MOSS_CARPET,
                    ModItems.PALE_HANGING_MOSS,
                ).reversed().forEach {
                    event.insertAfter(
                        Items.MOSS_CARPET.defaultInstance,
                        it.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    )
                }

                event.insertAfter(
                    Items.CHERRY_LEAVES.defaultInstance,
                    ModItems.PALE_OAK_LEAVES.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.CHERRY_SAPLING.defaultInstance,
                    ModItems.PALE_OAK_SAPLING.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.HONEY_BLOCK.defaultInstance,
                    ModItems.RESIN_BLOCK.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
//TODO:POT
                event.insertBefore(
                    Items.WITHER_ROSE.defaultInstance,
                    ModItems.CLOSED_EYEBLOSSOM.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertBefore(
                    Items.WITHER_ROSE.defaultInstance,
                    ModItems.OPEN_EYEBLOSSOM.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )

                event.insertAfter(
                    Items.PINK_PETALS.defaultInstance,
                    ModItems.LEAF_LITTER.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
                event.insertAfter(
                    Items.PINK_PETALS.defaultInstance,
                    ModItems.WILDFLOWERS.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )

            }

            ModCreativeModeTabs.BACKPORTED.key -> {
                event.insertBefore(
                    ModItems.WHITE_BUNDLE.toStack(),
                    ItemStack(Items.BUNDLE),
                    CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                )
            }
        }
    }

    @SubscribeEvent
    fun onCreateEntityAttribute(event: EntityAttributeCreationEvent) {
        event.put(ModEntityTypes.CREAKING.get(), Creaking.createAttributes().build())
    }

}