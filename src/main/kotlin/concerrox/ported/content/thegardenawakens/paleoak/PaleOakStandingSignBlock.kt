package concerrox.ported.content.thegardenawakens.paleoak

import concerrox.ported.registry.ModBlockEntityTypes
import concerrox.ported.registry.ModWoodTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState

class PaleOakStandingSignBlock(properties: Properties) : StandingSignBlock(ModWoodTypes.PALE_OAK, properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return PaleOakSignBlockEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        level: Level, state: BlockState, blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.SIGN.get(), SignBlockEntity::tick)
    }

}