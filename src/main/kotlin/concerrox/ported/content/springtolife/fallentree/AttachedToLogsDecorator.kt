package concerrox.ported.content.springtolife.fallentree

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModTreeDecoratorTypes
import net.minecraft.Util
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class AttachedToLogsDecorator(
    private val probability: Float,
    private val blockProvider: BlockStateProvider,
    private val directions: MutableList<Direction>
) : TreeDecorator() {

    override fun place(context: Context) {
        val random = context.random()
        for (pos in Util.shuffledCopy(context.logs(), random)) {
            val direction = Util.getRandom(directions, random)
            val relPos = pos.relative(direction)
            if (random.nextFloat() <= probability && context.isAir(relPos)) {
                context.setBlock(relPos, blockProvider.getState(random, relPos))
            }
        }
    }

    override fun type(): TreeDecoratorType<*> {
        return ModTreeDecoratorTypes.ATTACHED_TO_LOGS.get()
    }

    companion object {
        val CODEC: MapCodec<AttachedToLogsDecorator> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.floatRange(0.0f, 1.0f).fieldOf("probability").forGetter { it.probability },
                BlockStateProvider.CODEC.fieldOf("block_provider").forGetter { it.blockProvider },
                ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter { it.directions })
                .apply(builder, ::AttachedToLogsDecorator)
        }
    }

}