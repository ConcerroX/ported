package concerrox.ported.content.thegardenawakens.eyeblossom

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.content.thegardenawakens.creaking.particle.TrailParticleOption
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlock
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.Difficulty
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

class EyeblossomBlock : FlowerBlock {

    private val type: Type

    override fun codec() = CODEC

    constructor(type: Type, properties: Properties) : super(type.effect, type.effectDuration, properties) {
        this.type = type
    }

    constructor(open: Boolean, properties: Properties) : super(
        Type.fromBoolean(open).effect, Type.fromBoolean(open).effectDuration, properties
    ) {
        type = Type.fromBoolean(open)
    }

    override fun animateTick(state: BlockState, p_382933_: Level, p_382838_: BlockPos, p_383190_: RandomSource) {
        if (this.type.emitSounds() && p_383190_.nextInt(700) == 0) {
            val blockstate = p_382933_.getBlockState(p_382838_.below())
            if (blockstate.`is`(ModBlocks.PALE_MOSS_BLOCK)) {
                p_382933_.playLocalSound(
                    p_382838_.getX().toDouble(),
                    p_382838_.getY().toDouble(),
                    p_382838_.getZ().toDouble(),
                    ModSoundEvents.EYEBLOSSOM_IDLE,
                    SoundSource.AMBIENT,
                    1.0f,
                    1.0f,
                    false
                )
            }
        }
    }

    override fun randomTick(
        p_382824_: BlockState, p_382831_: ServerLevel, p_382957_: BlockPos, p_382888_: RandomSource
    ) {
        if (this.tryChangingState(p_382824_, p_382831_, p_382957_, p_382888_)) {
            p_382831_.playSound(
                null as Entity?, p_382957_, this.type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0f, 1.0f
            )
        }

        super.randomTick(p_382824_, p_382831_, p_382957_, p_382888_)
    }

    override fun tick(p_382808_: BlockState, p_383005_: ServerLevel, p_383211_: BlockPos, p_383088_: RandomSource) {
        if (this.tryChangingState(p_382808_, p_383005_, p_383211_, p_383088_)) {
            p_383005_.playSound(
                null as Entity?, p_383211_, type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0f, 1.0f
            )
        }

        super.tick(p_382808_, p_383005_, p_383211_, p_383088_)
    }

    private fun tryChangingState(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource): Boolean {
        if (!level.dimensionType().natural()) {
            return false
        } else if (CreakingHeartBlock.isNaturalNight(level) == type.open) {
            return false
        } else {
            val eyeblossomBlockType = type.transform()
            level.setBlock(pos, eyeblossomBlockType.state(), 3)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state))
            eyeblossomBlockType.spawnTransformParticle(level, pos, random)
            BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach {
                val blockstate = level.getBlockState(it)
                if (blockstate === state) {
                    val d0 = sqrt(pos.distSqr(it))
                    val i = random.nextIntBetweenInclusive((d0 * 5.0).toInt(), (d0 * 10.0).toInt())
                    level.scheduleTick(it, state.block, i)
                }
            }
            return true
        }
    }

    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if (!level.isClientSide() && level.difficulty != Difficulty.PEACEFUL && entity is Bee) {
            if (attractsBees(state) && !entity.hasEffect(MobEffects.POISON)) {
                entity.addEffect(beeInteractionEffect)
            }
        }
    }

    enum class Type(
        val open: Boolean,
        val effect: Holder<MobEffect>,
        val effectDuration: Float,
        val longSwitchSound: SoundEvent,
        val shortSwitchSound: SoundEvent,
        private val particleColor: Int
    ) {
        OPEN(
            true,
            MobEffects.BLINDNESS,
            11.0f,
            ModSoundEvents.EYEBLOSSOM_OPEN_LONG,
            ModSoundEvents.EYEBLOSSOM_OPEN,
            16545810
        ),
        CLOSED(
            false,
            MobEffects.CONFUSION,
            7.0f,
            ModSoundEvents.EYEBLOSSOM_CLOSE_LONG,
            ModSoundEvents.EYEBLOSSOM_CLOSE,
            6250335
        );

        fun block(): EyeblossomBlock = if (open) ModBlocks.OPEN_EYEBLOSSOM.get() else ModBlocks.CLOSED_EYEBLOSSOM.get()
        fun state(): BlockState = block().defaultBlockState()
        fun transform() = fromBoolean(!open)
        fun emitSounds() = open

        fun spawnTransformParticle(level: ServerLevel, pos: BlockPos, random: RandomSource) {
            val vec3 = pos.center
            val d0 = 0.5 + random.nextDouble()
            val vec31 = Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5)
            val vec32 = vec3.add(vec31.scale(d0))
            val particle = TrailParticleOption(vec32, this.particleColor, (20.0 * d0).toInt())
            level.sendParticles(particle, vec3.x, vec3.y, vec3.z, 1, 0.0, 0.0, 0.0, 0.0)
        }

        companion object {
            fun fromBoolean(open: Boolean) = if (open) OPEN else CLOSED
        }

    }

    companion object {

        val beeInteractionEffect: MobEffectInstance
            get() = MobEffectInstance(MobEffects.POISON, 25)

        private fun attractsBees(state: BlockState): Boolean {
            return if (state.`is`(BlockTags.FLOWERS)) {
                if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
                    false
                } else {
                    if (state.`is`(Blocks.SUNFLOWER)) state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER else true
                }
            } else false
        }

        val CODEC: MapCodec<EyeblossomBlock> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.fieldOf("open").forGetter { block -> block!!.type.open }, propertiesCodec()
            ).apply(it, ::EyeblossomBlock)
        }

        private const val EYEBLOSSOM_XZ_RANGE = 3
        private const val EYEBLOSSOM_Y_RANGE = 2

    }
}