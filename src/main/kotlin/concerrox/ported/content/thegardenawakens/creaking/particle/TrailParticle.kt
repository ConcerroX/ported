package concerrox.ported.content.thegardenawakens.creaking.particle

import concerrox.ported.util.ARGB
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TrailParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    target: Vec3,
    color: Int
) : TextureSheetParticle(level, x, y, z, xSpeed, ySpeed, zSpeed) {
    private val target: Vec3

    init {
        var color = ARGB.scaleRGB(
            color,
            0.875f + random.nextFloat() * 0.25f,
            0.875f + random.nextFloat() * 0.25f,
            0.875f + random.nextFloat() * 0.25f
        )
        rCol = ARGB.red(color) / 255f
        gCol = ARGB.green(color) / 255f
        bCol = ARGB.blue(color) / 255f
        quadSize = 0.26f
        this.target = target
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE

    override fun tick() {
        xo = x
        yo = y
        zo = z

        if (age++ >= lifetime) {
            remove()
        } else {
            val i = lifetime - age
            val d0 = 1.0 / i.toDouble()
            x = Mth.lerp(d0, x, target.x())
            y = Mth.lerp(d0, y, target.y())
            z = Mth.lerp(d0, z, target.z())
        }
    }

    public override fun getLightColor(p379977: Float): Int {
        return 15728880
    }

    @OnlyIn(Dist.CLIENT)
    class Provider(private val sprite: SpriteSet) : ParticleProvider<TrailParticleOption> {

        override fun createParticle(
            option: TrailParticleOption,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            val particle = TrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, option.target, option.color)
            particle.pickSprite(sprite)
            particle.setLifetime(option.duration)
            return particle
        }

    }

}