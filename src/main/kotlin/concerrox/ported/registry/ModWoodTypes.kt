package concerrox.ported.registry

import net.minecraft.world.level.block.state.properties.WoodType

object ModWoodTypes {

    val PALE_OAK = WoodType("pale_oak", ModBlockSetTypes.PALE_OAK)

    fun register() {
        WoodType.register(PALE_OAK)
    }

}