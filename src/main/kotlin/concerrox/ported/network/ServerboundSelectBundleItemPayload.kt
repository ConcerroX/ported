package concerrox.ported.network

import concerrox.ported.res
import concerrox.ported.util.setSelectedBundleItemIndex
import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext


@JvmRecord
data class ServerboundSelectBundleItemPayload(val slotId: Int, val selectedItemIndex: Int) : CustomPacketPayload {

    companion object {

        val STREAM_CODEC: StreamCodec<ByteBuf, ServerboundSelectBundleItemPayload> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundSelectBundleItemPayload::slotId,
            ByteBufCodecs.VAR_INT,
            ServerboundSelectBundleItemPayload::selectedItemIndex,
            ::ServerboundSelectBundleItemPayload
        )
        val TYPE = CustomPacketPayload.Type<ServerboundSelectBundleItemPayload>(res("bundle_item_selected"))

        fun handle(data: ServerboundSelectBundleItemPayload, context: IPayloadContext) {
            context.player().containerMenu.setSelectedBundleItemIndex(data.slotId, data.selectedItemIndex)
        }
    }

    private constructor(buffer: FriendlyByteBuf) : this(buffer.readVarInt(), buffer.readVarInt()) {
        require(!(selectedItemIndex < 0 && selectedItemIndex != -1)) { "Invalid selectedItemIndex: $selectedItemIndex" }
    }

    override fun type() = TYPE

}