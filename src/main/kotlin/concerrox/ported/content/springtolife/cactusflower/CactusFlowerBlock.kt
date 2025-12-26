package concerrox.ported.content.springtolife.cactusflower

import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SupportType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class CactusFlowerBlock(properties: Properties) : VegetationBlock(properties) {

    override fun codec(): MapCodec<CactusFlowerBlock> = simpleCodec(::CactusFlowerBlock)
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        val blockstate = level.getBlockState(pos)
        return blockstate.`is`(Blocks.CACTUS) || blockstate.`is`(Blocks.FARMLAND) || blockstate.isFaceSturdy(
            level, pos, Direction.UP, SupportType.CENTER
        )
    }

    companion object {

        private val SHAPE = column(14.0, 0.0, 12.0)

        private fun column(size: Double, y1: Double, y2: Double): VoxelShape {
            return column(size, size, y1, y2)
        }

        private fun column(xSize: Double, zSize: Double, y1: Double, y2: Double): VoxelShape {
            val d0 = xSize / 2.0
            val d1 = zSize / 2.0
            return box(8.0 - d0, y1, 8.0 - d1, 8.0 + d0, y2, 8.0 + d1)
        }

    }

}