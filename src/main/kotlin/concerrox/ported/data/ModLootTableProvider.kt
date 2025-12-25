package concerrox.ported.data

import concerrox.ported.content.springtolife.leaflitter.SegmentableBlock
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossCarpetBlock
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModItems
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.LimitCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.stream.IntStream

class ModLootTableProvider(
    output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>
) : LootTableProvider(
    output, setOf(), listOf(
        SubProviderEntry(::ModChestLootSubProvider, LootContextParamSets.CHEST),
        SubProviderEntry(::ModBlockLootSubProvider, LootContextParamSets.BLOCK)
    ), lookupProvider
) {

    class ModChestLootSubProvider(@Suppress("unused") lookupProvider: HolderLookup.Provider) : LootTableSubProvider {

        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
//            output.accept(
//                ModLootTables.WOODLAND_MANSION_MODIFIER, LootTable.lootTable().withPool(
//                    LootPool.lootPool().setRolls(UniformGenerator.between(1f, 4f)).add(
//                        LootItem.lootTableItem(ModItems.RESIN_CLUMP).setWeight(50)
//                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 4f)))
//                    )
//                )
//            )
        }

    }

    class ModBlockLootSubProvider(lookupProvider: HolderLookup.Provider) : BlockLootSubProvider(
        setOf(), FeatureFlags.DEFAULT_FLAGS, lookupProvider
    ) {

        override fun generate() {
            val enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT)

            add(ModBlocks.CREAKING_HEART.get()) {
                createSilkTouchDispatchTable(
                    it, applyExplosionDecay(
                        it,
                        LootItem.lootTableItem(ModItems.RESIN_CLUMP)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                            .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                            .apply(LimitCount.limitCount(IntRange.upperBound(9)))
                    )
                )
            }
            dropSelf(ModBlocks.PALE_OAK_LOG.get())
            dropSelf(ModBlocks.PALE_OAK_WOOD.get())
            dropSelf(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            dropSelf(ModBlocks.STRIPPED_PALE_OAK_WOOD.get())
            dropSelf(ModBlocks.PALE_OAK_PLANKS.get())
            dropSelf(ModBlocks.PALE_OAK_STAIRS.get())
            add(ModBlocks.PALE_OAK_SLAB.get()) {
                createSlabItemTable(it)
            }
            dropSelf(ModBlocks.PALE_OAK_FENCE.get())
            dropSelf(ModBlocks.PALE_OAK_FENCE_GATE.get())
            dropSelf(ModBlocks.PALE_OAK_DOOR.get())
            dropSelf(ModBlocks.PALE_OAK_TRAPDOOR.get())
            dropSelf(ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
            dropSelf(ModBlocks.PALE_OAK_BUTTON.get())

            dropSelf(ModBlocks.PALE_MOSS_BLOCK.get())
            add(ModBlocks.PALE_MOSS_CARPET.get()) {
                LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                        applyExplosionDecay(
                            it, LootItem.lootTableItem(it).`when`(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(it).setProperties(
                                    StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(PaleMossCarpetBlock.BASE, true)
                                )
                            )
                        )
                    )
                )
            }
            add(
                ModBlocks.PALE_HANGING_MOSS.get()
            ) {
                LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).`when`(hasShearsOrSilkTouch())
                        .add(LootItem.lootTableItem(it))
                )
            }

            add(ModBlocks.PALE_OAK_LEAVES.get()) {
                createLeavesDrops(it, ModBlocks.PALE_OAK_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES)
            }
            dropSelf(ModBlocks.PALE_OAK_SAPLING.get())
            dropPottedContents(ModBlocks.POTTED_PALE_OAK_SAPLING.get())
            dropSelf(ModBlocks.PALE_OAK_SIGN.get())
            dropSelf(ModBlocks.PALE_OAK_HANGING_SIGN.get())

            dropSelf(ModBlocks.RESIN_BLOCK.get())
            dropSelf(ModBlocks.RESIN_BRICKS.get())
            dropSelf(ModBlocks.RESIN_BRICK_STAIRS.get())
            add(ModBlocks.RESIN_BRICK_SLAB.get()) {
                createSlabItemTable(it)
            }
            dropSelf(ModBlocks.RESIN_BRICK_WALL.get())
            dropSelf(ModBlocks.CHISELED_RESIN_BRICKS.get())

            add(ModBlocks.RESIN_CLUMP.get()) {
                LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                        applyExplosionDecay(
                            it, LootItem.lootTableItem(it).apply(Direction.entries.toTypedArray()) { direction ->
                                SetItemCountFunction.setCount(ConstantValue.exactly(1.0f), true).`when`(
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(it).setProperties(
                                        StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(MultifaceBlock.getFaceProperty(direction), true)
                                    )
                                )
                            }.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1f), true))
                        )
                    )
                )
            }

            dropSelf(ModBlocks.CLOSED_EYEBLOSSOM.get())
            dropPottedContents(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get())
            dropSelf(ModBlocks.OPEN_EYEBLOSSOM.get())
            dropPottedContents(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get())

            add(Blocks.ENDER_CHEST) {
                createSingleItemTableWithSilkTouch(it, Blocks.OBSIDIAN, ConstantValue.exactly(8f))
            }

            add(ModBlocks.LEAF_LITTER.get(), ::createSegmentedBlockDrops)


            // Ported
            dropSelf(ModBlocks.GLOWING_OBSIDIAN.get())
            add(ModBlocks.NETHER_REACTOR_CORE.get()) {
                LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                        LootItem.lootTableItem(Items.IRON_INGOT).apply(
                            SetItemCountFunction.setCount(ConstantValue.exactly(6f))
                        )
                    ).add(
                        LootItem.lootTableItem(Items.DIAMOND).apply(
                            SetItemCountFunction.setCount(ConstantValue.exactly(3f))
                        )
                    )
                )
            }
            dropSelf(ModBlocks.STONECUTTER.get())

        }

        override fun getKnownBlocks() = ModBlocks.BLOCKS.entries.map { e -> e.value() }.toMutableList().apply {
            addAll(ModBlocks.PORTED_BLOCKS.entries.map { e -> e.value() })
            add(Blocks.ENDER_CHEST)
        }

        private fun hasShears(): LootItemCondition.Builder {
            return MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))
        }

        private fun hasShearsOrSilkTouch(): LootItemCondition.Builder {
            return hasShears().or(hasSilkTouch())
        }

        private fun createSegmentedBlockDrops(block: Block): LootTable.Builder {
            if (block !is SegmentableBlock) return noDrop()
            return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                    applyExplosionDecay(
                        block, LootItem.lootTableItem(block).apply(
                            IntStream.rangeClosed(1, 4).boxed().toList()
                        ) {
                            SetItemCountFunction.setCount(ConstantValue.exactly(it.toFloat())).`when`(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
                                    StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(block.getSegmentAmountProperty(), it)
                                )
                            )
                        })
                )
            )
        }

    }

}