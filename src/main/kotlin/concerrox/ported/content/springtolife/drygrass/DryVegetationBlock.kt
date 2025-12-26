package concerrox.ported.content.springtolife.drygrass

import com.mojang.serialization.MapCodec
import concerrox.ported.content.springtolife.leaflitter.VegetationBlock
import concerrox.ported.registry.ModBlockTags
import concerrox.ported.util.ShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

open class DryVegetationBlock(properties: Properties) : VegetationBlock(properties) {

    override fun codec(): MapCodec<DryVegetationBlock> = simpleCodec(::DryVegetationBlock)
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return state.`is`(ModBlockTags.DRY_VEGETATION_MAY_PLACE_ON)
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        AmbientDesertBlockSoundsPlayer.playAmbientDeadBushSounds(level, pos, random)
    }

    companion object {
        private val SHAPE = ShapeUtils.column(12.0, 0.0, 13.0)
    }

}