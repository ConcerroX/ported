package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.content.removedfeatures.netherreactorcore.ArmorStandWithArmsItem
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakBoatItem
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.*
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {

    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(ResourceLocation.DEFAULT_NAMESPACE)
    val PORTED_ITEMS: DeferredRegister.Items = DeferredRegister.createItems(Ported.MOD_ID)

    val FIELD_MASONED_BANNER_PATTERN = new("field_masoned_banner_pattern") {
        BannerPatternItem(ModBannerPatternTags.PATTERN_ITEM_FIELD_MASONED, newProperties { stacksTo(1) })
    }
    val BORDURE_INDENTED_BANNER_PATTERN = new("bordure_indented_banner_pattern") {
        BannerPatternItem(ModBannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED, newProperties { stacksTo(1) })
    }

    val WHITE_BUNDLE = new("white_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val ORANGE_BUNDLE = new("orange_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val MAGENTA_BUNDLE = new("magenta_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val LIGHT_BLUE_BUNDLE = new("light_blue_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val YELLOW_BUNDLE = new("yellow_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val LIME_BUNDLE = new("lime_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val PINK_BUNDLE = new("pink_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val GRAY_BUNDLE = new("gray_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val LIGHT_GRAY_BUNDLE = new("light_gray_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val CYAN_BUNDLE = new("cyan_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val PURPLE_BUNDLE = new("purple_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val BLUE_BUNDLE = new("blue_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val BROWN_BUNDLE = new("brown_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val GREEN_BUNDLE = new("green_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val RED_BUNDLE = new("red_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }
    val BLACK_BUNDLE = new("black_bundle") {
        BundleItem(newProperties {
            stacksTo(1)
            component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
        })
    }

    val CREAKING_HEART = blockItem(ModBlocks.CREAKING_HEART)
    val PALE_OAK_LOG = blockItem(ModBlocks.PALE_OAK_LOG)
    val PALE_OAK_WOOD = blockItem(ModBlocks.PALE_OAK_WOOD)
    val STRIPPED_PALE_OAK_LOG = blockItem(ModBlocks.STRIPPED_PALE_OAK_LOG)
    val STRIPPED_PALE_OAK_WOOD = blockItem(ModBlocks.STRIPPED_PALE_OAK_WOOD)
    val PALE_OAK_PLANKS = blockItem(ModBlocks.PALE_OAK_PLANKS)
    val PALE_OAK_STAIRS = blockItem(ModBlocks.PALE_OAK_STAIRS)
    val PALE_OAK_SLAB = blockItem(ModBlocks.PALE_OAK_SLAB)
    val PALE_OAK_FENCE = blockItem(ModBlocks.PALE_OAK_FENCE)
    val PALE_OAK_FENCE_GATE = blockItem(ModBlocks.PALE_OAK_FENCE_GATE)
    val PALE_OAK_DOOR = blockItem(ModBlocks.PALE_OAK_DOOR)
    val PALE_OAK_TRAPDOOR = blockItem(ModBlocks.PALE_OAK_TRAPDOOR)
    val PALE_OAK_PRESSURE_PLATE = blockItem(ModBlocks.PALE_OAK_PRESSURE_PLATE)
    val PALE_OAK_BUTTON = blockItem(ModBlocks.PALE_OAK_BUTTON)

    val PALE_MOSS_BLOCK = blockItem(ModBlocks.PALE_MOSS_BLOCK)
    val PALE_MOSS_CARPET = blockItem(ModBlocks.PALE_MOSS_CARPET)
    val PALE_HANGING_MOSS = blockItem(ModBlocks.PALE_HANGING_MOSS)
    val PALE_OAK_LEAVES = blockItem(ModBlocks.PALE_OAK_LEAVES)
    val PALE_OAK_SAPLING = blockItem(ModBlocks.PALE_OAK_SAPLING)

    val OPEN_EYEBLOSSOM = blockItem(ModBlocks.OPEN_EYEBLOSSOM)
    val CLOSED_EYEBLOSSOM = blockItem(ModBlocks.CLOSED_EYEBLOSSOM)

    val PALE_OAK_SIGN = new("pale_oak_sign") {
        SignItem(newProperties { stacksTo(16) }, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get())
    }
    val PALE_OAK_HANGING_SIGN = new("pale_oak_hanging_sign") {
        HangingSignItem(
            ModBlocks.PALE_OAK_HANGING_SIGN.get(),
            ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(),
            newProperties { stacksTo(16) },
        )
    }
    val PALE_OAK_BOAT = new("pale_oak_boat") {
        PaleOakBoatItem(false, newProperties { stacksTo(1) })
    }
    val PALE_OAK_CHEST_BOAT = new("pale_oak_chest_boat") {
        PaleOakBoatItem(true, newProperties { stacksTo(1) })
    }

    val RESIN_BLOCK = blockItem(ModBlocks.RESIN_BLOCK)
    val RESIN_BRICKS = blockItem(ModBlocks.RESIN_BRICKS)
    val RESIN_BRICK_STAIRS = blockItem(ModBlocks.RESIN_BRICK_STAIRS)
    val RESIN_BRICK_SLAB = blockItem(ModBlocks.RESIN_BRICK_SLAB)
    val RESIN_BRICK_WALL = blockItem(ModBlocks.RESIN_BRICK_WALL)
    val CHISELED_RESIN_BRICKS = blockItem(ModBlocks.CHISELED_RESIN_BRICKS)

    val RESIN_CLUMP = blockItem(ModBlocks.RESIN_CLUMP)
    val RESIN_BRICK = item("resin_brick")

    @Suppress("DEPRECATION")
    val CREAKING_SPAWN_EGG =
        new("creaking_spawn_egg") { SpawnEggItem(ModEntityTypes.CREAKING.get(), 6250335, 16545810, newProperties {}) }

    // 1.21.5
    val LEAF_LITTER = blockItem(ModBlocks.LEAF_LITTER)
    val WILDFLOWERS = blockItem(ModBlocks.WILDFLOWERS)

    val GLOWING_OBSIDIAN = portedBlockItem(ModBlocks.GLOWING_OBSIDIAN)
    val NETHER_REACTOR_CORE = portedBlockItem(ModBlocks.NETHER_REACTOR_CORE)
    val STONECUTTER = portedBlockItem(ModBlocks.STONECUTTER)
    val NETHER_PORTAL = portedBlockItem(
        BuiltInRegistries.BLOCK.getHolderOrThrow(
            BuiltInRegistries.BLOCK.getResourceKey(Blocks.NETHER_PORTAL).get()
        )
    )
    val END_GATEWAY = portedBlockItem(
        BuiltInRegistries.BLOCK.getHolderOrThrow(
            BuiltInRegistries.BLOCK.getResourceKey(Blocks.END_GATEWAY).get()
        )
    )
    val END_PORTAL = portedBlockItem(
        BuiltInRegistries.BLOCK.getHolderOrThrow(
            BuiltInRegistries.BLOCK.getResourceKey(Blocks.END_PORTAL).get()
        )
    )
    val AIR = portedBlockItem(
        BuiltInRegistries.BLOCK.getHolderOrThrow(
            BuiltInRegistries.BLOCK.getResourceKey(Blocks.AIR).get()
        )
    )
    val ARMOR_STAND_WITH_ARMS = portedNew("armor_stand_with_arms") {
        ArmorStandWithArmsItem(newProperties { stacksTo(16) })
    }

    private fun <R : Item> new(path: String, content: Supplier<R>): DeferredItem<R> = ITEMS.register(path, content)
    private fun <R : Item> portedNew(path: String, content: Supplier<R>): DeferredItem<R> =
        PORTED_ITEMS.register(path, content)

    private fun item(path: String): DeferredItem<Item> = ITEMS.registerSimpleItem(path)
    private fun blockItem(block: Holder<Block>): DeferredItem<BlockItem> = ITEMS.registerSimpleBlockItem(block)
    private fun portedBlockItem(block: Holder<Block>): DeferredItem<BlockItem> =
        PORTED_ITEMS.registerSimpleBlockItem(block)

    private fun newProperties(builder: Item.Properties.() -> Unit) = Item.Properties().apply(builder)

}