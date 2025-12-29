package concerrox.ported.registry

import concerrox.ported.content.bundlesofbravery.bundle.ServerboundSelectBundleItemPayload
import concerrox.ported.content.springtolife.test.ServerboundSetTestBlockPacket
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketTypes {

    fun register(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.playToServer(
            ServerboundSelectBundleItemPayload.TYPE,
            ServerboundSelectBundleItemPayload.STREAM_CODEC,
            ServerboundSelectBundleItemPayload::handle
        )
        registrar.playToServer(
            ServerboundSetTestBlockPacket.TYPE,
            ServerboundSetTestBlockPacket.STREAM_CODEC,
            ServerboundSetTestBlockPacket::handle
        )
    }

}