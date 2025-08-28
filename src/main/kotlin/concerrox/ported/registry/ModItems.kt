package concerrox.ported.registry

import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BannerPatternItem
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.BundleContents
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

    private fun <R : Item> new(path: String, content: Supplier<R>): DeferredItem<R> = ITEMS.register(path, content)
    private fun newProperties(builder: Item.Properties.() -> Unit) = Item.Properties().apply(builder)

}