package concerrox.ported.data

import concerrox.ported.registry.ModItemTags
import concerrox.ported.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>
) : ItemTagsProvider(output, lookupProvider, blockTags) {

    companion object {
        val BUNDLES by lazy {
            arrayOf(
                Items.BUNDLE,
                ModItems.WHITE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get(),
            )
        }
    }

    override fun addTags(provider: HolderLookup.Provider) {
        tag(ModItemTags.BUNDLES).add(*BUNDLES)
    }

}