package concerrox.ported.content.thegardenawakens.palemoss

import com.mojang.serialization.MapCodec
import concerrox.ported.registry.ModBlockTags
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class PaleHangingMossBlock(properties: Properties) : Block(properties), BonemealableBlock {

    override fun codec(): MapCodec<PaleHangingMossBlock> = simpleCodec(::PaleHangingMossBlock)

    override fun getShape(
        p_379697_: BlockState, p_380282_: BlockGetter, p_379821_: BlockPos, p_379644_: CollisionContext
    ): VoxelShape {
        return if (p_379697_.getValue(TIP)) SHAPE_TIP else SHAPE_BASE
    }

    override fun animateTick(p_379410_: BlockState, p_379865_: Level, p_379365_: BlockPos, p_380130_: RandomSource) {
        if (p_380130_.nextInt(500) == 0) {
            val blockstate = p_379865_.getBlockState(p_379365_.above())
            if (blockstate.`is`(ModBlockTags.PALE_OAK_LOGS) || blockstate.`is`(ModBlocks.PALE_OAK_LEAVES)) {
                p_379865_.playLocalSound(
                    p_379365_.x.toDouble(),
                    p_379365_.y.toDouble(),
                    p_379365_.z.toDouble(),
                    ModSoundEvents.PALE_HANGING_MOSS_IDLE,
                    SoundSource.AMBIENT,
                    1.0f,
                    1.0f,
                    false
                )
            }
        }
    }

    override fun propagatesSkylightDown(p_380235_: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return true
    }

    override fun canSurvive(p_380096_: BlockState, p_379969_: LevelReader, p_380283_: BlockPos): Boolean {
        return this.canStayAtPosition(p_379969_, p_380283_)
    }

    private fun canStayAtPosition(level: BlockGetter, pos: BlockPos): Boolean {
        val blockpos = pos.relative(Direction.UP)
        val blockstate = level.getBlockState(blockpos)
        return MultifaceBlock.canAttachTo(
            level, Direction.UP, blockpos, blockstate
        ) || blockstate.`is`(ModBlocks.PALE_HANGING_MOSS)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (!this.canStayAtPosition(level, pos)) {
            level.scheduleTick(pos, this, 1)
        }
        return state.setValue(TIP, !level.getBlockState(pos.below()).`is`(this))
    }

    override fun tick(p_381085_: BlockState, p_381014_: ServerLevel, p_381010_: BlockPos, p_380962_: RandomSource) {
        if (!this.canStayAtPosition(p_381014_, p_381010_)) {
            p_381014_.destroyBlock(p_381010_, true)
        }
    }

    override fun createBlockStateDefinition(p_379416_: StateDefinition.Builder<Block?, BlockState?>) {
        p_379416_.add(*arrayOf<Property<*>>(TIP))
    }

    override fun isValidBonemealTarget(p_379509_: LevelReader, p_379596_: BlockPos, p_380331_: BlockState): Boolean {
        return this.canGrowInto(p_379509_.getBlockState(this.getTip(p_379509_, p_379596_).below()))
    }

    private fun canGrowInto(state: BlockState): Boolean {
        return state.isAir
    }

    fun getTip(level: BlockGetter, pos: BlockPos): BlockPos {
        val `blockpos$mutableblockpos` = pos.mutable()

        var blockstate: BlockState?
        do {
            `blockpos$mutableblockpos`.move(Direction.DOWN)
            blockstate = level.getBlockState(`blockpos$mutableblockpos`)
        } while (blockstate.`is`(this))

        return `blockpos$mutableblockpos`.relative(Direction.UP).immutable()
    }

    override fun isBonemealSuccess(
        p_380206_: Level, p_380151_: RandomSource, p_379719_: BlockPos, p_379567_: BlockState
    ): Boolean {
        return true
    }

    override fun performBonemeal(
        p_379337_: ServerLevel, p_379974_: RandomSource, p_379496_: BlockPos, p_379559_: BlockState
    ) {
        val blockpos = this.getTip(p_379337_, p_379496_).below()
        if (this.canGrowInto(p_379337_.getBlockState(blockpos))) {
            p_379337_.setBlockAndUpdate(blockpos, p_379559_.setValue<Boolean?, Boolean?>(TIP, true) as BlockState)
        }
    }

    init {
        this.registerDefaultState(
            (this.stateDefinition.any() as BlockState).setValue<Boolean?, Boolean?>(
                TIP, true
            ) as BlockState
        )
    }

    companion object {
        fun column(size: Double, y1: Double, y2: Double): VoxelShape {
            return column(size, size, y1, y2)
        }

        fun column(xSize: Double, zSize: Double, y1: Double, y2: Double): VoxelShape {
            val d0 = xSize / 2.0
            val d1 = zSize / 2.0
            return box(8.0 - d0, y1, 8.0 - d1, 8.0 + d0, y2, 8.0 + d1)
        }

        private val SHAPE_BASE: VoxelShape = column(14.0, 0.0, 16.0)
        private val SHAPE_TIP: VoxelShape = column(14.0, 2.0, 16.0)
        val TIP: BooleanProperty = BooleanProperty.create("tip")
    }
}