package concerrox.ported.content.springtolife.test

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.ByIdMap.OutOfBoundsStrategy
import net.minecraft.util.StringRepresentable
import java.util.function.IntFunction

enum class TestBlockMode(private val id: Int, private val key: String) : StringRepresentable {

    START(0, "start"), LOG(1, "log"), FAIL(2, "fail"), ACCEPT(3, "accept");

    val displayName: Component = Component.translatable("test_block.mode.$key")
    val detailedMessage: Component = Component.translatable("test_block.mode_info.$key")

    override fun getSerializedName() = key

    companion object {
        private val BY_ID: IntFunction<TestBlockMode> = ByIdMap.continuous(
            { it.id }, entries.toTypedArray(), OutOfBoundsStrategy.ZERO
        )
        val CODEC: Codec<TestBlockMode> = StringRepresentable.fromEnum<TestBlockMode> { entries.toTypedArray() }
        val STREAM_CODEC: StreamCodec<ByteBuf, TestBlockMode> = ByteBufCodecs.idMapper(BY_ID) { it.id }
    }

}