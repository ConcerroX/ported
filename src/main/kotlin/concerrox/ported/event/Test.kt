package concerrox.ported.event

import concerrox.ported.registry.ModItems
import net.minecraft.world.entity.npc.VillagerTrades
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.village.WandererTradesEvent

//@EventBusSubscriber()
class Test {

    //TODO:fix the trades
    @SubscribeEvent
    fun onWandererTrades(event: WandererTradesEvent) {
        event.genericTrades += VillagerTrades.ItemsForEmeralds(ModItems.PALE_HANGING_MOSS.get(), 1, 3, 4, 1)
        event.genericTrades += VillagerTrades.ItemsForEmeralds(ModItems.PALE_MOSS_BLOCK.get(), 1, 2, 5, 1)
        event.genericTrades += VillagerTrades.ItemsForEmeralds(ModItems.PALE_OAK_LOG.get(), 1, 8, 4, 1)
        event.genericTrades += VillagerTrades.ItemsForEmeralds(ModItems.PALE_OAK_SAPLING.get(), 5, 1, 8, 1)

        event.genericTrades += VillagerTrades.ItemsForEmeralds(ModItems.OPEN_EYEBLOSSOM.get(), 1, 1, 7, 1)
    }

}