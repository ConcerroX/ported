package concerrox.ported.content.springtolife.firefly

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class FireflyParticle(
    level: ClientLevel,
    p_401068_: Double,
    p_401435_: Double,
    p_401238_: Double,
    p_401102_: Double,
    p_401330_: Double,
    p_401419_: Double,
) : TextureSheetParticle(level, p_401068_, p_401435_, p_401238_, p_401102_, p_401330_, p_401419_) {

    init {
        speedUpWhenYMotionIsBlocked = true
        friction = 0.96f
        quadSize *= 0.75f
        yd *= 0.8
        xd *= 0.8
        zd *= 0.8
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun getLightColor(p_401057_: Float): Int {
        return (255f * getFadeAmount(getLifetimeProgress(age.toFloat() + p_401057_), 0.1f, 0.3f)).toInt()
    }

    override fun tick() {
        super.tick()
        if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            this.remove()
        } else {
            this.setAlpha(getFadeAmount(this.getLifetimeProgress(this.age.toFloat()), 0.3f, 0.5f))
            if (this.random.nextFloat() > 0.95f || this.age == 1) {
                this.setParticleSpeed(
                    (-0.05f + 0.1f * this.random.nextFloat()).toDouble(),
                    (-0.05f + 0.1f * this.random.nextFloat()).toDouble(),
                    (-0.05f + 0.1f * this.random.nextFloat()).toDouble()
                )
            }
        }
    }

    private fun getLifetimeProgress(age: Float): Float {
        return Mth.clamp(age / lifetime.toFloat(), 0f, 1f)
    }

    @OnlyIn(Dist.CLIENT)
    class FireflyProvider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(
            option: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            val random = level.random
            val particle = FireflyParticle(
                level,
                x,
                y,
                z,
                0.5 - random.nextDouble(),
                if (random.nextBoolean()) ySpeed else -ySpeed,
                0.5 - random.nextDouble()
            )
            particle.setLifetime(random.nextIntBetweenInclusive(PARTICLE_MIN_LIFETIME, PARTICLE_MAX_LIFETIME))
            particle.scale(1.5f)
            particle.pickSprite(sprite)
            particle.setAlpha(0f)
            return particle
        }

    }

    companion object {
        private const val PARTICLE_FADE_OUT_LIGHT_TIME = 0.3f
        private const val PARTICLE_FADE_IN_LIGHT_TIME = 0.1f
        private const val PARTICLE_FADE_OUT_ALPHA_TIME = 0.5f
        private const val PARTICLE_FADE_IN_ALPHA_TIME = 0.3f
        private const val PARTICLE_MIN_LIFETIME = 200
        private const val PARTICLE_MAX_LIFETIME = 300

        private fun getFadeAmount(lifetimeProgress: Float, fadeIn: Float, fadeOut: Float): Float {
            return if (lifetimeProgress >= 1f - fadeIn) {
                (1f - lifetimeProgress) / fadeIn
            } else {
                if (lifetimeProgress <= fadeOut) lifetimeProgress / fadeOut else 1f
            }
        }
    }

}