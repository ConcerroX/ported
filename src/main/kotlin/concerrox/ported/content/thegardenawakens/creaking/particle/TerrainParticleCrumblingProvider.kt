package concerrox.ported.content.thegardenawakens.creaking.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.TerrainParticle
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.world.level.block.Blocks
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TerrainParticleCrumblingProvider : ParticleProvider<BlockParticleOption> {

    companion object {

        fun createTerrainParticle(
            type: BlockParticleOption,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): TerrainParticle? {
            val state = type.state
            return if (!state.isAir && !state.`is`(Blocks.MOVING_PISTON) && state.shouldSpawnTerrainParticles()) {
                TerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, state).updateSprite(state, type.pos)
            } else null
        }

    }

    override fun createParticle(
        option: BlockParticleOption,
        level: ClientLevel,
        x: Double,
        y: Double,
        z: Double,
        xSpeed: Double,
        ySpeed: Double,
        zSpeed: Double
    ): Particle? {
        val particle = createTerrainParticle(option, level, x, y, z, xSpeed, ySpeed, zSpeed)
        if (particle != null) {
            particle.setParticleSpeed(0.0, 0.0, 0.0)
            particle.setLifetime(level.random.nextInt(10) + 1)
        }
        return particle
    }

}