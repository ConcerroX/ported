package concerrox.ported.util

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.FastColor
import net.minecraft.world.phys.Vec3

object PortedCodecs {

    val RGB_COLOR_CODEC: Codec<Int> = Codec.withAlternative(Codec.INT, ExtraCodecs.VECTOR3F) {
        FastColor.ARGB32.colorFromFloat(1f, it.x, it.y, it.z)
    }

    val VEC3_STREAM_CODEC = object : StreamCodec<ByteBuf, Vec3> {
        override fun decode(buffer: ByteBuf) = readVec3(buffer)
        override fun encode(buffer: ByteBuf, vec3: Vec3) {
            writeVec3(buffer, vec3)
        }
    }

    private fun readVec3(buffer: ByteBuf): Vec3 {
        return Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
    }

    private fun writeVec3(buffer: ByteBuf, vec3: Vec3) {
        buffer.writeDouble(vec3.x())
        buffer.writeDouble(vec3.y())
        buffer.writeDouble(vec3.z())
    }

}