package concerrox.ported.registry

import concerrox.ported.content.springtolife.wolf.WolfSoundVariant
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object ModRegistries {

    val WOLF_SOUND_VARIANT: ResourceKey<Registry<WolfSoundVariant>> =
        ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("wolf_sound_variant"))

}