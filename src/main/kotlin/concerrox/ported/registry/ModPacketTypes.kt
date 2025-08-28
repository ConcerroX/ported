package concerrox.ported.registry

import concerrox.ported.network.ServerboundSelectBundleItemPayload
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketTypes {

    fun register(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.playToServer(
            ServerboundSelectBundleItemPayload.TYPE,
            ServerboundSelectBundleItemPayload.STREAM_CODEC,
            ServerboundSelectBundleItemPayload::handle
        )
    }

}