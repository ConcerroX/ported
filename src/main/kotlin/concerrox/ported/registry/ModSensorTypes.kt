package concerrox.ported.registry

import concerrox.ported.content.chasetheskies.happyghast.AdultSensorAnyType
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.ai.sensing.TemptingSensor
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredRegister

object ModSensorTypes {

    internal val SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

//    val FOOD_TEMPTATIONS = SENSOR_TYPES.new("food_temptations") {
//        SensorType { TemptingSensor(Ingredient.of(ModItemTags.HAPPY_GHAST_FOOD)) }
//    }
    val NEAREST_ADULT_ANY_TYPE = SENSOR_TYPES.new("nearest_adult_any_type") {
        SensorType(::AdultSensorAnyType)
    }

}