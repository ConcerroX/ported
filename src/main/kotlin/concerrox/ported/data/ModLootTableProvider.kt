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
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction
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

    class ModChestLootSubProvider(private val registries: HolderLookup.Provider) : LootTableSubProvider {

        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            output.accept(
                BuiltInLootTables.RUINED_PORTAL, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(4.0f, 8.0f)).add(
                        LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.FLINT).setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(9.0f, 18.0f)))
                    ).add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(40))
                        .add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15)).add(
                            LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(15)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 24.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(15)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))
                        ).add(
                            LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 12.0f)))
                        ).add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(5))
                        .add(LootItem.lootTableItem(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5)).add(
                            LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 12.0f)))
                        ).add(LootItem.lootTableItem(Items.CLOCK).setWeight(5)).add(
                            LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 8.0f)))
                        ).add(LootItem.lootTableItem(Items.BELL).setWeight(1))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1)).add(
                            LootItem.lootTableItem(Items.GOLD_BLOCK).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                        .add(EmptyLootItem.emptyItem().setWeight(1)).add(
                            LootItem.lootTableItem(Items.LODESTONE).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                        )
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_WEAPONSMITH, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f)).add(
                        LootItem.lootTableItem(Items.DIAMOND).setWeight(3)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.BREAD).setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.APPLE).setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
                        .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(5))
//                        .add(LootItem.lootTableItem(Items.IRON_SPEAR).setWeight(5)) // TODO: spear
//                        .add(LootItem.lootTableItem(Items.COPPER_SPEAR).setWeight(7))
                        .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(5))
                        .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(5))
                        .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(5))
                        .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(5)).add(
                            LootItem.lootTableItem(Blocks.OBSIDIAN).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Blocks.OAK_SAPLING).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 7.0f)))
                        ).add(LootItem.lootTableItem(Items.SADDLE).setWeight(3))
                        //.add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR)) TODO: fix this
                        .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
                        .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
                        .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR))
                ).withPool(
                    LootPool.lootPool().setRolls(
                        ConstantValue.exactly(1.0f)
                    ).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(
                            SetItemCountFunction.setCount(
                                ConstantValue.exactly(1.0f)
                            )
                        )
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_CARTOGRAPHER, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(1.0f, 5.0f)).add(
                        LootItem.lootTableItem(Items.MAP).setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.PAPER).setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                    ).add(LootItem.lootTableItem(Items.COMPASS).setWeight(5)).add(
                        LootItem.lootTableItem(Items.BREAD).setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                    ).add(
                        LootItem.lootTableItem(Items.STICK).setWeight(5)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                    )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_TANNERY, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(1.0f, 5.0f)).add(
                        LootItem.lootTableItem(Items.LEATHER).setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(2))
                        .add(LootItem.lootTableItem(Items.LEATHER_HELMET).setWeight(2)).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(2))
                        .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1)).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_PLAINS_HOUSE, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f)).add(
                        LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(LootItem.lootTableItem(Items.DANDELION).setWeight(2))
                        .add(LootItem.lootTableItem(Items.POPPY).setWeight(1)).add(
                            LootItem.lootTableItem(Items.POTATO).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.APPLE).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        ).add(LootItem.lootTableItem(Items.BOOK).setWeight(1))
                        .add(LootItem.lootTableItem(Items.FEATHER).setWeight(1)).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Blocks.OAK_SAPLING).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_TAIGA_HOUSE, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f)).add(
                        LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                    ).add(LootItem.lootTableItem(Items.FERN).setWeight(2))
                        .add(LootItem.lootTableItem(Items.LARGE_FERN).setWeight(2)).add(
                            LootItem.lootTableItem(Items.POTATO).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        ).add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(1)).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Blocks.SPRUCE_SAPLING).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        ).add(LootItem.lootTableItem(Items.SPRUCE_SIGN).setWeight(1)).add(
                            LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f)).add(
                        LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                    ).add(LootItem.lootTableItem(Items.SHORT_GRASS).setWeight(5))
                        .add(LootItem.lootTableItem(Items.TALL_GRASS).setWeight(5)).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Blocks.ACACIA_SAPLING).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                        ).add(LootItem.lootTableItem(Items.SADDLE).setWeight(1)).add(
                            LootItem.lootTableItem(Blocks.TORCH).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                        ).add(LootItem.lootTableItem(Items.BUCKET).setWeight(1))
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_SNOWY_HOUSE, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))
                        .add(LootItem.lootTableItem(Blocks.BLUE_ICE).setWeight(1))
                        .add(LootItem.lootTableItem(Blocks.SNOW_BLOCK).setWeight(4)).add(
                            LootItem.lootTableItem(Items.POTATO).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        ).add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(1))
                        .add(LootItem.lootTableItem(Items.FURNACE).setWeight(1)).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.SNOWBALL).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.COAL).setWeight(5)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )
            output.accept(
                BuiltInLootTables.VILLAGE_DESERT_HOUSE, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))
                        .add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(1))
                        .add(LootItem.lootTableItem(Items.GREEN_DYE).setWeight(1)).add(
                            LootItem.lootTableItem(Blocks.CACTUS).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.WHEAT).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 7.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.BREAD).setWeight(10)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                        ).add(LootItem.lootTableItem(Items.BOOK).setWeight(1)).add(
                            LootItem.lootTableItem(Blocks.DEAD_BUSH).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                        ).add(
                            LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                        )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                        LootItem.lootTableItem(Items.BUNDLE).setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                    ).add(EmptyLootItem.emptyItem().setWeight(2))
                )
            )

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

            // 1.21.5
            add(ModBlocks.LEAF_LITTER.get(), ::createSegmentedBlockDrops)
            add(ModBlocks.WILDFLOWERS.get(), ::createSegmentedBlockDrops)
            add(ModBlocks.BUSH.get(), ::createShearsOrSilkTouchOnlyDrop)
            dropSelf(ModBlocks.FIREFLY_BUSH.get())
            dropSelf(ModBlocks.CACTUS_FLOWER.get())
            add(ModBlocks.SHORT_DRY_GRASS.get(), ::createShearsOrSilkTouchOnlyDrop)
            add(ModBlocks.TALL_DRY_GRASS.get(), ::createShearsOrSilkTouchOnlyDrop)

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

        private fun createShearsOrSilkTouchOnlyDrop(item: ItemLike): LootTable.Builder {
            return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).`when`(hasShearsOrSilkTouch())
                    .add(LootItem.lootTableItem(item))
            )
        }

    }

}