package concerrox.ported

import concerrox.ported.event.ClientGameEventHandler
import concerrox.ported.event.ClientModEventHandler
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge

@Mod(value = Ported.MOD_ID, dist = [Dist.CLIENT])
class PortedClient(modEventBus: IEventBus, container: ModContainer) {

    init {
        NeoForge.EVENT_BUS.register(ClientGameEventHandler)
        modEventBus.register(ClientModEventHandler)
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

}