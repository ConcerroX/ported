package concerrox.ported.content.thegardenawakens.paleoak

import concerrox.ported.registry.ModEntityTypes
import concerrox.ported.registry.ModItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class PaleOakBoat(entityType: EntityType<PaleOakBoat>, level: Level) : Boat(entityType, level) {

    constructor(level: Level, x: Double, y: Double, z: Double) : this(ModEntityTypes.PALE_OAK_BOAT.get(), level) {
        setPos(x, y, z);
        xo = x
        yo = y
        zo = z
    }

    override fun getDropItem(): Item {
        return ModItems.PALE_OAK_BOAT.get()
    }

}