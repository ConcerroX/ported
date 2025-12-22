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

    val CREAKING_HEART = SoundType(
        1f,
        1f,
        ModSoundEvents.CREAKING_HEART_BREAK,
        ModSoundEvents.CREAKING_HEART_STEP,
        ModSoundEvents.CREAKING_HEART_PLACE,
        ModSoundEvents.CREAKING_HEART_HIT,
        ModSoundEvents.CREAKING_HEART_FALL
    )

    val RESIN = SoundType(
        1f,
        1f,
        ModSoundEvents.RESIN_BREAK,
        ModSoundEvents.RESIN_STEP,
        ModSoundEvents.RESIN_PLACE,
        SoundEvents.EMPTY,
        ModSoundEvents.RESIN_FALL
    )

    val RESIN_BRICKS = SoundType(
        1f,
        1f,
        ModSoundEvents.RESIN_BRICKS_BREAK,
        ModSoundEvents.RESIN_BRICKS_STEP,
        ModSoundEvents.RESIN_BRICKS_PLACE,
        ModSoundEvents.RESIN_BRICKS_HIT,
        ModSoundEvents.RESIN_BRICKS_FALL
    )


}