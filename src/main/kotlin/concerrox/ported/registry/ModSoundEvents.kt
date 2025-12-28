package concerrox.ported.registry

import concerrox.ported.content.springtolife.wolf.WolfSoundVariant
import concerrox.ported.content.springtolife.wolf.WolfSoundVariants
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

object ModSoundEvents {

    val SPAWNER_BREAK = variableRange("block.spawner.break")
    val SPAWNER_FALL = variableRange("block.spawner.fall")
    val SPAWNER_HIT = variableRange("block.spawner.hit")
    val SPAWNER_PLACE = variableRange("block.spawner.place")
    val SPAWNER_STEP = variableRange("block.spawner.step")

    val BUBBLE_POP = variableRange("ui.hud.bubble_pop")

    val BUNDLE_INSERT_FAIL = variableRange("item.bundle.insert_fail")

    val CREAKING_AMBIENT = variableRange("entity.creaking.ambient")
    val CREAKING_ACTIVATE = variableRange("entity.creaking.activate")
    val CREAKING_DEACTIVATE = variableRange("entity.creaking.deactivate")
    val CREAKING_ATTACK = variableRange("entity.creaking.attack")
    val CREAKING_DEATH = variableRange("entity.creaking.death")
    val CREAKING_STEP = variableRange("entity.creaking.step")
    val CREAKING_FREEZE = variableRange("entity.creaking.freeze")
    val CREAKING_UNFREEZE = variableRange("entity.creaking.unfreeze")
    val CREAKING_SPAWN = variableRange("entity.creaking.spawn")
    val CREAKING_SWAY = variableRange("entity.creaking.sway")
    val CREAKING_TWITCH = variableRange("entity.creaking.twitch")
    val CREAKING_HEART_BREAK = variableRange("block.creaking_heart.break")
    val CREAKING_HEART_FALL = variableRange("block.creaking_heart.fall")
    val CREAKING_HEART_HIT = variableRange("block.creaking_heart.hit")
    val CREAKING_HEART_HURT = variableRange("block.creaking_heart.hurt")
    val CREAKING_HEART_PLACE = variableRange("block.creaking_heart.place")
    val CREAKING_HEART_STEP = variableRange("block.creaking_heart.step")
    val CREAKING_HEART_IDLE = variableRange("block.creaking_heart.idle")
    val CREAKING_HEART_SPAWN = variableRange("block.creaking_heart.spawn")

    val RESIN_BREAK = variableRange("block.resin.break")
    val RESIN_FALL = variableRange("block.resin.fall")
    val RESIN_PLACE = variableRange("block.resin.place")
    val RESIN_STEP = variableRange("block.resin.step")
    val RESIN_BRICKS_BREAK = variableRange("block.resin_bricks.break")
    val RESIN_BRICKS_FALL = variableRange("block.resin_bricks.fall")
    val RESIN_BRICKS_HIT = variableRange("block.resin_bricks.hit")
    val RESIN_BRICKS_PLACE = variableRange("block.resin_bricks.place")
    val RESIN_BRICKS_STEP = variableRange("block.resin_bricks.step")

    val PALE_HANGING_MOSS_IDLE = variableRange("block.pale_hanging_moss.idle")

    val EYEBLOSSOM_OPEN_LONG = variableRange("block.eyeblossom.open_long")
    val EYEBLOSSOM_OPEN = variableRange("block.eyeblossom.open")
    val EYEBLOSSOM_CLOSE_LONG = variableRange("block.eyeblossom.close_long")
    val EYEBLOSSOM_CLOSE = variableRange("block.eyeblossom.close")
    val EYEBLOSSOM_IDLE = variableRange("block.eyeblossom.idle")

    val LEAF_LITTER_BREAK = variableRange("block.leaf_litter.break")
    val LEAF_LITTER_STEP = variableRange("block.leaf_litter.step")
    val LEAF_LITTER_PLACE = variableRange("block.leaf_litter.place")
    val LEAF_LITTER_HIT = variableRange("block.leaf_litter.hit")
    val LEAF_LITTER_FALL = variableRange("block.leaf_litter.fall")

    val FIREFLY_BUSH_IDLE = variableRange("block.firefly_bush.idle")

    val CACTUS_FLOWER_BREAK = variableRange("block.cactus_flower.break")
    val CACTUS_FLOWER_PLACE = variableRange("block.cactus_flower.place")

    val DRY_GRASS = variableRange("block.dry_grass.ambient")
    val DEAD_BUSH_IDLE = variableRange("block.deadbush.idle")

    val WOLF_SOUNDS = registerWolfSoundVariants()

    fun registerWolfSoundVariants(): Map<WolfSoundVariants.SoundSet, WolfSoundVariant> {
        return WolfSoundVariants.SoundSet.entries.associateWith {
            val s = it.soundEventSuffix
            WolfSoundVariant(
                registerForHolder("entity.wolf$s.ambient"),
                registerForHolder("entity.wolf$s.death"),
                registerForHolder("entity.wolf$s.growl"),
                registerForHolder("entity.wolf$s.hurt"),
                registerForHolder("entity.wolf$s.pant"),
                registerForHolder("entity.wolf$s.whine")
            )
        }
    }

    private fun registerForHolder(name: String): Holder.Reference<SoundEvent> {
        val location = ResourceLocation.withDefaultNamespace(name)
        return Registry.registerForHolder(
            BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location)
        )
    }

    private fun variableRange(name: String): SoundEvent =
        SoundEvent.createVariableRangeEvent(ResourceLocation.withDefaultNamespace(name))

}