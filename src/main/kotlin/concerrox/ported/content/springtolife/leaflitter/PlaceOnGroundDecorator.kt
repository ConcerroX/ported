package concerrox.ported.content.springtolife.leaflitter

import com.google.common.collect.Lists
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModTreeDecoratorTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.structure.BoundingBox
import kotlin.math.max
import kotlin.math.min

class PlaceOnGroundDecorator(
    private val tries: Int,
    private val radius: Int,
    private val height: Int,
    private val blockStateProvider: BlockStateProvider
) : TreeDecorator() {

    override fun type(): TreeDecoratorType<PlaceOnGroundDecorator> = ModTreeDecoratorTypes.PLACE_ON_GROUND.get()

    private fun getLowestTrunkOrRootOfTree(context: Context): MutableList<BlockPos> {
        val list: MutableList<BlockPos> = Lists.newArrayList()
        val list1: MutableList<BlockPos> = context.roots()
        val list2: MutableList<BlockPos> = context.logs()
        if (list1.isEmpty()) {
            list.addAll(list2)
        } else if (!list2.isEmpty() && (list1[0]).getY() == (list2[0]).getY()) {
            list.addAll(list2)
            list.addAll(list1)
        } else {
            list.addAll(list1)
        }
        return list
    }

    override fun place(p_394461_: Context) {
        val list: MutableList<BlockPos> = getLowestTrunkOrRootOfTree(p_394461_)
        if (!list.isEmpty()) {
            val blockpos = list.first()
            val i = blockpos.getY()
            var j = blockpos.getX()
            var k = blockpos.getX()
            var l = blockpos.getZ()
            var i1 = blockpos.getZ()

            for (blockpos1 in list) {
                if (blockpos1.getY() == i) {
                    j = min(j, blockpos1.getX())
                    k = max(k, blockpos1.getX())
                    l = min(l, blockpos1.getZ())
                    i1 = max(i1, blockpos1.getZ())
                }
            }

            val randomsource = p_394461_.random()
            val boundingbox = (BoundingBox(j, i, l, k, i, i1)).inflatedBy(this.radius, this.height, this.radius)
            val `blockpos$mutableblockpos` = MutableBlockPos()

            for (j1 in 0..<this.tries) {
                `blockpos$mutableblockpos`.set(
                    randomsource.nextIntBetweenInclusive(
                        boundingbox.minX(), boundingbox.maxX()
                    ),
                    randomsource.nextIntBetweenInclusive(boundingbox.minY(), boundingbox.maxY()),
                    randomsource.nextIntBetweenInclusive(boundingbox.minZ(), boundingbox.maxZ())
                )
                this.attemptToPlaceBlockAbove(p_394461_, `blockpos$mutableblockpos`)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun attemptToPlaceBlockAbove(context: Context, pos: BlockPos) {
        val above = pos.above()
        if (context.level().isStateAtPosition(above) { it.isAir || it.`is`(Blocks.VINE) }
            && context.level().isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isSolid)
            && context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).y <= above.y
        ) {
            context.setBlock(above, this.blockStateProvider.getState(context.random(), above))
        }
    }

    companion object {
        val CODEC: MapCodec<PlaceOnGroundDecorator> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter { it.tries },
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter { it.radius },
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter { it.height },
                BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter { it.blockStateProvider },
            ).apply(builder, ::PlaceOnGroundDecorator)
        }
    }
}