package concerrox.ported.registry

import concerrox.ported.content.thegardenawakens.creaking.Creaking
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakBoat
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakChestBoat
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.entity.vehicle.ChestBoat
import net.neoforged.neoforge.registries.DeferredRegister

object ModEntityTypes {

    val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(Registries.ENTITY_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

    val CREAKING = ENTITY_TYPES.new("creaking") {
        EntityType.Builder.of(EntityType.EntityFactory(::Creaking), MobCategory.MONSTER).apply {
            sized(0.9f, 2.7f)
            eyeHeight(2.3f)
            clientTrackingRange(8)
        }.build(it.toString())
    }

    val PALE_OAK_BOAT = ENTITY_TYPES.new("pale_oak_boat") {
        EntityType.Builder.of(EntityType.EntityFactory(::PaleOakBoat), MobCategory.MISC).apply {
            sized(1.375f, 0.5625f)
            eyeHeight(0.5625f)
            clientTrackingRange(10)
        }.build(it.toString())
    }

    val PALE_OAK_CHEST_BOAT = ENTITY_TYPES.new("pale_oak_chest_boat") {
        EntityType.Builder.of(EntityType.EntityFactory(::PaleOakChestBoat), MobCategory.MISC).apply {
            sized(1.375f, 0.5625f)
            eyeHeight(0.5625f)
            clientTrackingRange(10)
        }.build(it.toString())
    }

}