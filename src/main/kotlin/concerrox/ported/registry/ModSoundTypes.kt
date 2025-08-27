package concerrox.ported.registry

import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.SoundType

@Suppress("DEPRECATION")
object ModSoundTypes {

    val SPAWNER = SoundType(
        1f,
        1f,
        ModSoundEvents.SPAWNER_BREAK,
        ModSoundEvents.SPAWNER_STEP,
        ModSoundEvents.SPAWNER_PLACE,
        ModSoundEvents.SPAWNER_HIT,
        ModSoundEvents.SPAWNER_FALL
    )

}