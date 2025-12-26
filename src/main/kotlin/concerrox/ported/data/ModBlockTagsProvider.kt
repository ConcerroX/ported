package concerrox.ported.data

import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper
) : BlockTagsProvider(output, lookupProvider, ResourceLocation.DEFAULT_NAMESPACE, existingFileHelper) {

    override fun addTags(provider: HolderLookup.Provider) {
        tag(BlockTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.get())
        tag(BlockTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.get())
        tag(BlockTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.get())

        tag(BlockTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.get())
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.get())
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.get())
        tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.get())
        tag(BlockTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.get())
        tag(BlockTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.get())
        tag(BlockTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.get())
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
        tag(BlockTags.SAPLINGS).add(ModBlocks.PALE_OAK_SAPLING.get())
        tag(BlockTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.get())
        tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.PALE_OAK_TRAPDOOR.get())
        tag(BlockTags.STANDING_SIGNS).add(ModBlocks.PALE_OAK_SIGN.get())
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.PALE_OAK_HANGING_SIGN.get())
        tag(ModBlockTags.PALE_OAK_LOGS).add(
            ModBlocks.PALE_OAK_LOG.get(),
            ModBlocks.PALE_OAK_WOOD.get(),
            ModBlocks.STRIPPED_PALE_OAK_LOG.get(),
            ModBlocks.STRIPPED_PALE_OAK_WOOD.get()
        )
        tag(BlockTags.LOGS_THAT_BURN).addTags(ModBlockTags.PALE_OAK_LOGS)

        tag(BlockTags.DIRT).add(ModBlocks.PALE_MOSS_BLOCK.get())

        tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(ModBlocks.PALE_OAK_LOG.get())

        tag(BlockTags.REPLACEABLE).add(
            ModBlocks.RESIN_CLUMP.get(),
            ModBlocks.LEAF_LITTER.get(),
        )
        tag(BlockTags.REPLACEABLE_BY_TREES).add(
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.LEAF_LITTER.get(),
            ModBlocks.BUSH.get(),
            ModBlocks.FIREFLY_BUSH.get(),
        )
        tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(
            ModBlocks.LEAF_LITTER.get(),
            ModBlocks.WILDFLOWERS.get(),
        )
        tag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER).add(ModBlocks.LEAF_LITTER.get())
        tag(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(ModBlocks.PALE_MOSS_CARPET.get())

        tag(BlockTags.FLOWERS).add(ModBlocks.WILDFLOWERS.get())

        tag(BlockTags.FLOWER_POTS).add(ModBlocks.POTTED_PALE_OAK_SAPLING.get())
        tag(BlockTags.WALL_SIGNS).add(ModBlocks.PALE_OAK_WALL_SIGN.get())
        tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get())
        tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.RESIN_CLUMP.get(),
        )
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            ModBlocks.RESIN_BRICKS.get(),
            ModBlocks.RESIN_BRICK_SLAB.get(),
            ModBlocks.RESIN_BRICK_WALL.get(),
            ModBlocks.RESIN_BRICK_STAIRS.get(),
            ModBlocks.CHISELED_RESIN_BRICKS.get(),
            ModBlocks.GLOWING_OBSIDIAN.get(),
            ModBlocks.NETHER_REACTOR_CORE.get(),
            ModBlocks.STONECUTTER.get()
        )
        tag(BlockTags.MINEABLE_WITH_HOE).add(
            ModBlocks.PALE_MOSS_BLOCK.get(), ModBlocks.PALE_MOSS_CARPET.get()
        )
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.CREAKING_HEART.get())

        tag(BlockTags.FLOWER_POTS).add(
            ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), ModBlocks.POTTED_OPEN_EYEBLOSSOM.get()
        )
        tag(BlockTags.SMALL_FLOWERS).add(
            ModBlocks.OPEN_EYEBLOSSOM.get(),
            ModBlocks.CLOSED_EYEBLOSSOM.get(),
        )

        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ModBlocks.GLOWING_OBSIDIAN.get())
        tag(BlockTags.DRAGON_IMMUNE).add(ModBlocks.GLOWING_OBSIDIAN.get())

        tag(BlockTags.DEAD_BUSH_MAY_PLACE_ON).addTag(Tags.Blocks.VILLAGER_FARMLANDS)
    }

}