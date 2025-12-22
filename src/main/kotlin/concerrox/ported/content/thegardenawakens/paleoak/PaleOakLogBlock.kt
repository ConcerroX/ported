package concerrox.ported.content.thegardenawakens.paleoak

import net.minecraft.core.Holder
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility
import java.util.function.Supplier

class PaleOakLogBlock(private val strippedBlock: Supplier<RotatedPillarBlock>, properties: Properties) :
    RotatedPillarBlock(properties) {

    override fun getToolModifiedState(
        state: BlockState, context: UseOnContext, itemAbility: ItemAbility, simulate: Boolean
    ): BlockState? {
        if (itemAbility == ItemAbilities.AXE_STRIP) {
            return strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS))
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate)
    }

}
