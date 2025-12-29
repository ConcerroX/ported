package concerrox.ported.content.springtolife.test

import com.mojang.serialization.MapCodec
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.BlockItemStateProperties
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.GameMasterBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class TestBlock(properties: Properties) : BaseEntityBlock(properties), GameMasterBlock {

    override fun codec(): MapCodec<TestBlock> = simpleCodec(::TestBlock)
    override fun getRenderShape(state: BlockState) = RenderShape.MODEL
    override fun newBlockEntity(pos: BlockPos, state: BlockState) = TestBlockEntity(pos, state)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        val stateData = context.itemInHand.get(DataComponents.BLOCK_STATE)
        var state = defaultBlockState()
        if (stateData != null) {
            val mode = stateData.get(MODE)
            if (mode != null) state = state.setValue(MODE, mode)
        }
        return state
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(MODE)
    }

    override fun useWithoutItem(
        state: BlockState, level: Level, p_397100_: BlockPos, player: Player, p_397202_: BlockHitResult
    ): InteractionResult {
        val be = level.getBlockEntity(p_397100_)
        if (be is TestBlockEntity) {
            if (!player.canUseGameMasterBlocks()) {
                return InteractionResult.PASS
            } else {
                if (level.isClientSide()) Minecraft.getInstance().setScreen(TestBlockEditScreen(be))
                return InteractionResult.SUCCESS
            }
        } else {
            return InteractionResult.PASS
        }
    }

    override fun tick(p_397712_: BlockState, p_397112_: ServerLevel, p_397466_: BlockPos, p_397531_: RandomSource) {
        getServerTestBlockEntity(p_397112_, p_397466_)?.reset()
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean
    ) {
        val be = getServerTestBlockEntity(level, pos)
        if (be != null && be.getMode() !== TestBlockMode.START) {
            val flag = level.hasNeighborSignal(pos)
            val flag1 = be.isPowered
            if (flag && !flag1) {
                be.isPowered = true
                be.trigger()
            } else if (!flag && flag1) {
                be.isPowered = false
            }
        }
    }

    public override fun getSignal(
        p_397637_: BlockState, p_397297_: BlockGetter, p_397948_: BlockPos, p_397493_: Direction
    ): Int {
        if (p_397637_.getValue(MODE) != TestBlockMode.START) {
            return 0
        } else {
            val var6 = p_397297_.getBlockEntity(p_397948_)
            return if (var6 is TestBlockEntity) {
                if (var6.isPowered) 15 else 0
            } else {
                0
            }
        }
    }

    override fun getCloneItemStack(
        state: BlockState, target: HitResult, level: LevelReader, pos: BlockPos, player: Player
    ): ItemStack {
        val stack = super.getCloneItemStack(state, target, level, pos, player)
        return setModeOnStack(stack, state.getValue(MODE))
    }

    companion object {

        val MODE: EnumProperty<TestBlockMode> = EnumProperty.create("mode", TestBlockMode::class.java)

        private fun getServerTestBlockEntity(level: Level, pos: BlockPos): TestBlockEntity? {
            val be: TestBlockEntity?
            if (level is ServerLevel) {
                val var4 = level.getBlockEntity(pos)
                if (var4 is TestBlockEntity) {
                    be = var4
                    return be
                }
            }
            be = null
            return be
        }

        fun setModeOnStack(stack: ItemStack, mode: TestBlockMode): ItemStack {
            stack.set(
                DataComponents.BLOCK_STATE,
                stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).with(MODE, mode)
            )
            return stack
        }

    }

}