package concerrox.ported.content.bundlesofbravery.salmon

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import java.util.function.IntFunction

enum class SalmonVariant(private val type: String, val id: Int, val boundingBoxScale: Float) : StringRepresentable {
    SMALL("small", 0, 0.5f), MEDIUM("medium", 1, 1f), LARGE("large", 2, 1.5f);

    override fun getSerializedName() = type

    companion object {
        @JvmField
        val DEFAULT = MEDIUM

        @Suppress("DEPRECATION")
        @JvmField
        val CODEC: StringRepresentable.EnumCodec<SalmonVariant> =
            StringRepresentable.fromEnum { entries.toTypedArray() }

        @JvmField
        val BY_ID: IntFunction<SalmonVariant> = ByIdMap.continuous(
            SalmonVariant::id, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.CLAMP
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, SalmonVariant> = ByteBufCodecs.idMapper(BY_ID, SalmonVariant::id)
    }
}