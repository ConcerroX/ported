package concerrox.ported.data

import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModItemTags
import concerrox.ported.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
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
        tag(ItemTags.BOATS).add(ModItems.PALE_OAK_BOAT.get())
        tag(ItemTags.CHEST_BOATS).add(ModItems.PALE_OAK_CHEST_BOAT.get())
        tag(ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK.get())

        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES)
        copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS)
        copy(BlockTags.LEAVES, ItemTags.LEAVES)
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN)
        copy(ModBlockTags.PALE_OAK_LOGS, ModItemTags.PALE_OAK_LOGS)
        copy(BlockTags.PLANKS, ItemTags.PLANKS)
        copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS)
        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS)
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS)
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES)
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES)
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS)
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS)
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS)

        copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS)

        copy(BlockTags.FLOWERS, ItemTags.FLOWERS)
    }

}