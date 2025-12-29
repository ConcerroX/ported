package concerrox.ported.data

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.data.event.GatherDataEvent

object DataGenerator {

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val output = event.generator.packOutput
        val lookupProvider = event.lookupProvider
        val existingFileHelper = event.existingFileHelper
        val blockTagsProvider = ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
        event.addProvider(blockTagsProvider)
        event.addProvider(ModItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter()))
        event.addProvider(ModRecipeProvider(output, lookupProvider))
        event.addProvider(ModDataMapProvider(output, lookupProvider))
        event.addProvider(ModLootTableProvider(output, lookupProvider))
//        event.addProvider(ModLootModifierProvider(output, lookupProvider))
        event.addProvider(ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper))
        event.addProvider(ModBiomeTagsProvider(output, lookupProvider, existingFileHelper))
        event.addProvider(ModDatapackBuiltinEntriesProvider(output, lookupProvider))
    }

}