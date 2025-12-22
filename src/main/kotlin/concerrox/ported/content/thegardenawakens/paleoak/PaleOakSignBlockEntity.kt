package concerrox.ported.content.thegardenawakens.paleoak

import concerrox.ported.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState

class PaleOakSignBlockEntity(pos: BlockPos, state: BlockState) :
    SignBlockEntity(ModBlockEntityTypes.SIGN.get(), pos, state) {

    override fun getType(): BlockEntityType<*> {
        return ModBlockEntityTypes.SIGN.get()
    }

}