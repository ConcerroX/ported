package concerrox.ported.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

object ModSoundEvents {

    val SPAWNER_BREAK = variableRange("block.spawner.break")
    val SPAWNER_FALL = variableRange("block.spawner.fall")
    val SPAWNER_HIT = variableRange("block.spawner.hit")
    val SPAWNER_PLACE = variableRange("block.spawner.place")
    val SPAWNER_STEP = variableRange("block.spawner.step")

    val BUBBLE_POP = variableRange("ui.hud.bubble_pop")

    private fun variableRange(name: String): SoundEvent =
        SoundEvent.createVariableRangeEvent(ResourceLocation.withDefaultNamespace(name))

}