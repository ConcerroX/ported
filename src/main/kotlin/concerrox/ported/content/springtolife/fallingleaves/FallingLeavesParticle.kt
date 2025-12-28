package concerrox.ported.content.springtolife.fallingleaves

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.SimpleParticleType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

@OnlyIn(Dist.CLIENT)
class FallingLeavesParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    sprites: SpriteSet,
    gravityMultiplier: Float,
    windBig: Float,
    swirl: Boolean,
    flowAway: Boolean,
    size: Float,
    ySpeed: Float
) : TextureSheetParticle(level, x, y, z) {

    private var rotSpeed: Float
    private val particleRandom: Float
    private val spinAcceleration: Float
    private val windBig: Float
    private val swirl: Boolean
    private val flowAway: Boolean
    private val xaFlowScale: Double
    private val zaFlowScale: Double
    private val swirlPeriod: Double

    init {
        this.setSprite(sprites.get(this.random.nextInt(12), 12))
        this.rotSpeed = Math.toRadians(if (this.random.nextBoolean()) -30.0 else 30.0).toFloat()
        this.particleRandom = this.random.nextFloat()
        this.spinAcceleration = Math.toRadians(if (this.random.nextBoolean()) -5.0 else 5.0).toFloat()
        this.windBig = windBig
        this.swirl = swirl
        this.flowAway = flowAway
        this.lifetime = 300
        this.gravity = gravityMultiplier * 1.2f * 0.0025f
        val f = size * (if (this.random.nextBoolean()) 0.05f else 0.075f)
        this.quadSize = f
        this.setSize(f, f)
        this.friction = 1.0f
        this.yd = (-ySpeed).toDouble()
        this.xaFlowScale = cos(Math.toRadians((this.particleRandom * 60.0f).toDouble())) * this.windBig.toDouble()
        this.zaFlowScale = sin(Math.toRadians((this.particleRandom * 60.0f).toDouble())) * this.windBig.toDouble()
        this.swirlPeriod = Math.toRadians((1000.0f + this.particleRandom * 3000.0f).toDouble())
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.lifetime-- <= 0) {
            this.remove()
        }

        if (!this.removed) {
            val f = (300 - this.lifetime).toFloat()
            val f1 = min(f / 300.0f, 1.0f)
            var d0 = 0.0
            var d1 = 0.0
            if (this.flowAway) {
                d0 += this.xaFlowScale * f1.toDouble().pow(1.25)
                d1 += this.zaFlowScale * f1.toDouble().pow(1.25)
            }

            if (this.swirl) {
                d0 += f1.toDouble() * cos(f1.toDouble() * this.swirlPeriod) * this.windBig.toDouble()
                d1 += f1.toDouble() * sin(f1.toDouble() * this.swirlPeriod) * this.windBig.toDouble()
            }

            this.xd += d0 * 0.0025
            this.zd += d1 * 0.0025
            this.yd -= this.gravity.toDouble()
            this.rotSpeed += this.spinAcceleration / 20.0f
            this.oRoll = this.roll
            this.roll += this.rotSpeed / 20.0f
            this.move(this.xd, this.yd, this.zd)
            if (this.onGround || this.lifetime < 299 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove()
            }

            if (!this.removed) {
                this.xd *= this.friction.toDouble()
                this.yd *= this.friction.toDouble()
                this.zd *= this.friction.toDouble()
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    class PaleOakProvider(private val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            p_383203_: SimpleParticleType,
            p_383110_: ClientLevel,
            p_383063_: Double,
            p_382907_: Double,
            p_383062_: Double,
            p_382964_: Double,
            p_382864_: Double,
            p_382906_: Double
        ): Particle {
            return FallingLeavesParticle(
                p_383110_, p_383063_, p_382907_, p_383062_, this.sprites, 0.07f, 10.0f, true, false, 2.0f, 0.021f
            )
        }
    }

    @OnlyIn(Dist.CLIENT)
    class TintedLeavesProvider(private val sprites: SpriteSet) : ParticleProvider<ColorParticleOption> {
        override fun createParticle(
            p_400051_: ColorParticleOption,
            p_393524_: ClientLevel,
            p_393550_: Double,
            p_394425_: Double,
            p_394620_: Double,
            p_393739_: Double,
            p_394589_: Double,
            p_394199_: Double
        ): Particle {
            val particle: Particle = FallingLeavesParticle(
                p_393524_, p_393550_, p_394425_, p_394620_, this.sprites, 0.07f, 10.0f, true, false, 2.0f, 0.021f
            )
            particle.setColor(p_400051_.getRed(), p_400051_.getGreen(), p_400051_.getBlue())
            return particle
        }
    }

    companion object {
        private const val ACCELERATION_SCALE = 0.0025f
        private const val INITIAL_LIFETIME = 300
        private const val CURVE_ENDPOINT_TIME = 300
    }

}