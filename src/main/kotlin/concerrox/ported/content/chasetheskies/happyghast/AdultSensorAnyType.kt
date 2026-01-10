package concerrox.ported.content.chasetheskies.happyghast

import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities
import net.minecraft.world.entity.ai.sensing.AdultSensor

class AdultSensorAnyType : AdultSensor() {

//    private fun setNearestVisibleAdult(entity: LivingEntity, nearestVisibleLivingEntities: NearestVisibleLivingEntities) {
//        val optional = nearestVisibleLivingEntities.findClosest {
//            it.type.`is`(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !it.isBaby
//        }
//        entity.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional)
//    }

}