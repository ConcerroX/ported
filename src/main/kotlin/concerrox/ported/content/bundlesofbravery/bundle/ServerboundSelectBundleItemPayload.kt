package concerrox.ported.content.bundlesofbravery.bundle

import concerrox.ported.id
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.neoforge.network.handling.IPayloadContext

private fun AbstractContainerMenu.setSelectedBundleItemIndex(slotIndex: Int, bundleItemIndex: Int) {
    if (slotIndex >= 0 && slotIndex < slots.size) {
        val stack = this.slots[slotIndex].item
        BundleItemUtils.toggleSelectedItem(stack, bundleItemIndex)
    }
}

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
        val TYPE = CustomPacketPayload.Type<ServerboundSelectBundleItemPayload>(id("bundle_item_selected"))

        fun handle(data: ServerboundSelectBundleItemPayload, context: IPayloadContext) {
            context.player().containerMenu.setSelectedBundleItemIndex(data.slotId, data.selectedItemIndex)
        }
    }

//    constructor(buffer: FriendlyByteBuf) : this(buffer.readVarInt(), buffer.readVarInt()) {
//        require(!(selectedItemIndex < 0 && selectedItemIndex != -1)) { "Invalid selectedItemIndex: $selectedItemIndex" }
//    }

    override fun type() = TYPE

}