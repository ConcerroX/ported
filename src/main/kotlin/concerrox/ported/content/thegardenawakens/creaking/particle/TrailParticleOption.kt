package concerrox.ported.content.thegardenawakens.creaking.particle

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModParticleTypes
import concerrox.ported.util.PortedCodecs
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.phys.Vec3

@JvmRecord
data class TrailParticleOption(val target: Vec3, val color: Int, val duration: Int) : ParticleOptions {

    override fun getType(): ParticleType<TrailParticleOption> = ModParticleTypes.TRAIL.get()

    companion object {

        val CODEC: MapCodec<TrailParticleOption> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target),
                PortedCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color),
                ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)
            ).apply(builder, ::TrailParticleOption)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> = StreamCodec.composite(
            PortedCodecs.VEC3_STREAM_CODEC,
            TrailParticleOption::target,
            ByteBufCodecs.INT,
            TrailParticleOption::color,
            ByteBufCodecs.VAR_INT,
            TrailParticleOption::duration,
            ::TrailParticleOption
        )

    }

}