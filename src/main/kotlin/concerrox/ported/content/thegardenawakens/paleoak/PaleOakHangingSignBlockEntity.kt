package concerrox.ported.content.thegardenawakens.paleoak

import concerrox.ported.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.HangingSignBlockEntity
import net.minecraft.world.level.block.state.BlockState

class PaleOakHangingSignBlockEntity(pos: BlockPos, state: BlockState) : HangingSignBlockEntity(pos,
    state
) {

    override fun getType(): BlockEntityType<*> {
        return ModBlockEntityTypes.HANGING_SIGN.get()
    }

    override fun isValidBlockState(state: BlockState): Boolean {
        return type.isValid(state)
    }

}