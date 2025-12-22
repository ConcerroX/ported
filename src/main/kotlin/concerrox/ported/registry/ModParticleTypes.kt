package concerrox.ported.registry

import concerrox.ported.content.thegardenawakens.creaking.particle.TrailParticleOption
import concerrox.ported.util.new
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister

object ModParticleTypes {

    val PARTICLE_TYPES: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(Registries.PARTICLE_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

    val BLOCK_CRUMBLE = PARTICLE_TYPES.new("block_crumble") {
        object : ParticleType<BlockParticleOption>(false) {
            override fun codec() = BlockParticleOption.codec(this)
            override fun streamCodec() = BlockParticleOption.streamCodec(this)
        }
    }

    val TRAIL = PARTICLE_TYPES.new("trail") {
        object : ParticleType<TrailParticleOption>(false) {
            override fun codec() = TrailParticleOption.CODEC
            override fun streamCodec() = TrailParticleOption.STREAM_CODEC
        }
    }

}