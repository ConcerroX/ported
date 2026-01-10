package concerrox.ported.content.springtolife.test

import concerrox.ported.id
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class ServerboundSetTestBlockPacket(val position: BlockPos, val mode: TestBlockMode, val message: String) :
    CustomPacketPayload {

    override fun type() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<ServerboundSetTestBlockPacket>(id("set_test_block"))

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ServerboundSetTestBlockPacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ServerboundSetTestBlockPacket::position,
            TestBlockMode.STREAM_CODEC,
            ServerboundSetTestBlockPacket::mode,
            ByteBufCodecs.STRING_UTF8,
            ServerboundSetTestBlockPacket::message,
            ::ServerboundSetTestBlockPacket
        )

        fun handle(data: ServerboundSetTestBlockPacket, context: IPayloadContext) {
            val player = Minecraft.getInstance().player!!
            if (player.canUseGameMasterBlocks()) {
                val pos = data.position
                val state = player.level().getBlockState(pos)
                val be = player.level().getBlockEntity(pos)
                if (be is TestBlockEntity) {
                    be.setMode(data.mode)
                    be.message = data.message
                    be.setChanged()
                    player.level().sendBlockUpdated(pos, state, be.blockState, 3)
                }
            }
        }

    }

}