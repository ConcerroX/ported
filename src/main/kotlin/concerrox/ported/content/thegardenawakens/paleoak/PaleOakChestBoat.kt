package concerrox.ported.content.thegardenawakens.paleoak

import concerrox.ported.registry.ModEntityTypes
import concerrox.ported.registry.ModItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.vehicle.ChestBoat
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class PaleOakChestBoat(entityType: EntityType<PaleOakChestBoat>, level: Level) : ChestBoat(entityType, level) {

    constructor(level: Level, x: Double, y: Double, z: Double) : this(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), level) {
        setPos(x, y, z)
        xo = x
        yo = y
        zo = z
    }

    override fun getDropItem(): Item {
        return ModItems.PALE_OAK_CHEST_BOAT.get()
    }

}