package concerrox.ported.content.thegardenawakens.creakingheart

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModTreeDecoratorTypes
import net.minecraft.Util
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class CreakingHeartDecorator(private val probability: Float) : TreeDecorator() {

    override fun type(): TreeDecoratorType<*> = ModTreeDecoratorTypes.CREAKING_HEART.get()

    override fun place(context: Context) {
        val random = context.random()
        val logs = context.logs()
        if (!logs.isEmpty && !(random.nextFloat() >= this.probability)) {
            val list1 = ArrayList(logs)
            Util.shuffle(list1, random)
            val optional = list1.stream().filter { pos ->
                for (direction in Direction.entries) {
                    if (!context.level().isStateAtPosition(pos.relative(direction)) { it.`is`(BlockTags.LOGS) }) {
                        return@filter false
                    }
                }
                true
            }.findFirst()
            if (!optional.isEmpty) {
                context.setBlock(
                    optional.get(), ModBlocks.CREAKING_HEART.get().defaultBlockState().setValue(
                        CreakingHeartBlock.STATE, CreakingHeartState.DORMANT
                    ).setValue(CreakingHeartBlock.NATURAL, true)
                )
            }
        }
    }

    companion object {
        val CODEC: MapCodec<CreakingHeartDecorator> =
            Codec.floatRange(0.0f, 1.0f).fieldOf("probability").xmap(::CreakingHeartDecorator) { it.probability }
    }

}