//package concerrox.ported.data
//
//import concerrox.ported.Ported
//import concerrox.ported.registry.ModLootTables
//import net.minecraft.core.HolderLookup
//import net.minecraft.data.PackOutput
//import net.minecraft.world.level.storage.loot.BuiltInLootTables
//import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
//import net.neoforged.neoforge.common.loot.AddTableLootModifier
//import net.neoforged.neoforge.common.loot.LootTableIdCondition
//import java.util.concurrent.CompletableFuture
//
//class ModLootModifierProvider(
//    output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>
//) : GlobalLootModifierProvider(output, registries, Ported.MOD_ID) {
//
//    override fun start() {
////        add(
////            "chests/woodland_mansion_modifier", AddTableLootModifier(
////                arrayOf(LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION.location()).build()),
////                ModLootTables.WOODLAND_MANSION_MODIFIER
////            ), listOf()
////        )
//    }
//
//}