package concerrox.ported.content.thegardenawakens.palemoss

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModConfiguredFeatures
import concerrox.ported.registry.ModTreeDecoratorTypes
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import org.apache.commons.lang3.mutable.MutableObject

class PaleMossDecorator(
    private val leavesProbability: Float, private val trunkProbability: Float, private val groundProbability: Float
) : TreeDecorator() {

    override fun type(): TreeDecoratorType<*> = ModTreeDecoratorTypes.PALE_MOSS.get()

    override fun place(context: Context) {
        val random = context.random()
        val level = context.level() as WorldGenLevel
        val logPositions = Util.shuffledCopy(context.logs(), random)
        if (logPositions.isEmpty()) return

        val mutable = MutableObject(logPositions.first())
        logPositions.forEach {
            if (it.y < mutable.getValue().y) mutable.value = it
        }
        val pos = mutable.getValue()
        if (random.nextFloat() < groundProbability) {
            level.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap {
                it.get(ModConfiguredFeatures.PALE_MOSS_PATCH)
            }.ifPresent {
                it.value().place(level, level.level.chunkSource.generator, random, pos.above())
            }
        }
        context.logs().forEach {
            if (random.nextFloat() < trunkProbability) {
                val pos = it.below()
                if (context.isAir(pos)) addMossHanger(pos, context)
            }
        }
        context.leaves().forEach {
            if (random.nextFloat() < leavesProbability) {
                val pos = it.below()
                if (context.isAir(pos)) addMossHanger(pos, context)
            }
        }
    }

    companion object {

        val CODEC: MapCodec<PaleMossDecorator> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.floatRange(0.0f, 1.0f).fieldOf("leaves_probability").forGetter { it.leavesProbability },
                Codec.floatRange(0.0f, 1.0f).fieldOf("trunk_probability").forGetter { it.trunkProbability },
                Codec.floatRange(0.0f, 1.0f).fieldOf("ground_probability").forGetter { it.groundProbability })
                .apply(builder, ::PaleMossDecorator)
        }

        private fun addMossHanger(pos: BlockPos, context: Context) {
            var pos = pos
            while (context.isAir(pos.below()) && !(context.random().nextFloat().toDouble() < 0.5)) {
                context.setBlock(
                    pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(PaleHangingMossBlock.TIP, false)
                )
                pos = pos.below()
            }
            context.setBlock(
                pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(PaleHangingMossBlock.TIP, true)
            )
        }

    }

}