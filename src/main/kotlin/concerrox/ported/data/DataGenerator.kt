package concerrox.ported.data

import concerrox.ported.Ported
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = Ported.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object DataGenerator {

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val output = event.generator.packOutput
        val lookupProvider = event.lookupProvider
        event.addProvider(ModRecipeProvider(output, lookupProvider))
    }

}