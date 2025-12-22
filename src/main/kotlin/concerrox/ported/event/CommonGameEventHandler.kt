package concerrox.ported.event

import net.minecraft.world.effect.MobEffectUtil
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent

@EventBusSubscriber
object CommonGameEventHandler {

    @SubscribeEvent
    fun onLivingBreathe(event: LivingBreatheEvent) {
        if (MobEffectUtil.hasWaterBreathing(event.entity)) event.refillAirAmount = 4 // TODO: add nautilus
    }

}