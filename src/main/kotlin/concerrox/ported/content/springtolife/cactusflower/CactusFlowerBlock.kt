package concerrox.ported.content.springtolife.cactusflower

import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import concerrox.ported.util.ShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SupportType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

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
        private val SHAPE = ShapeUtils.column(14.0, 0.0, 12.0)
    }

}