package concerrox.ported.registry

import net.minecraft.world.level.block.state.properties.BlockSetType

object ModBlockSetTypes {

    val PALE_OAK = BlockSetType("pale_oak")

    fun register() {
        BlockSetType.register(PALE_OAK)
    }

}