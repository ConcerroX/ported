package concerrox.ported.content.springtolife.wolf

import concerrox.ported.registry.ModRegistries
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object WolfSoundVariants {

    val CLASSIC = createKey(SoundSet.CLASSIC)
    val PUGLIN = createKey(SoundSet.PUGLIN)
    val SAD = createKey(SoundSet.SAD)
    val ANGRY = createKey(SoundSet.ANGRY)
    val GRUMPY = createKey(SoundSet.GRUMPY)
    val BIG = createKey(SoundSet.BIG)
    val CUTE = createKey(SoundSet.CUTE)

    private fun createKey(soundSet: SoundSet): ResourceKey<WolfSoundVariant> {
        return ResourceKey.create(
            ModRegistries.WOLF_SOUND_VARIANT, ResourceLocation.withDefaultNamespace(soundSet.identifier)
        )
    }

    fun bootstrap(context: BootstrapContext<WolfSoundVariant>) {
        register(context, CLASSIC, SoundSet.CLASSIC)
        register(context, PUGLIN, SoundSet.PUGLIN)
        register(context, SAD, SoundSet.SAD)
        register(context, ANGRY, SoundSet.ANGRY)
        register(context, GRUMPY, SoundSet.GRUMPY)
        register(context, BIG, SoundSet.BIG)
        register(context, CUTE, SoundSet.CUTE)
    }

    private fun register(
        context: BootstrapContext<WolfSoundVariant>, key: ResourceKey<WolfSoundVariant>, soundSet: SoundSet
    ) {
        context.register(key, ModSoundEvents.WOLF_SOUNDS[soundSet]!!)
    }

    fun pickRandomSoundVariant(registryAccess: RegistryAccess): Holder<WolfSoundVariant> {
        return registryAccess.lookupOrThrow(ModRegistries.WOLF_SOUND_VARIANT).listElements().toList().random()
    }

    enum class SoundSet(val identifier: String, val soundEventSuffix: String) {
        CLASSIC("classic", ""), PUGLIN("puglin", "_puglin"), SAD("sad", "_sad"), ANGRY(
            "angry",
            "_angry"
        ),
        GRUMPY("grumpy", "_grumpy"), BIG("big", "_big"), CUTE("cute", "_cute");
    }

}