package concerrox.ported.content.springtolife.wolf

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModRegistries
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.sounds.SoundEvent

@JvmRecord
data class WolfSoundVariant(
    val ambientSound: Holder<SoundEvent>,
    val deathSound: Holder<SoundEvent>,
    val growlSound: Holder<SoundEvent>,
    val hurtSound: Holder<SoundEvent>,
    val pantSound: Holder<SoundEvent>,
    val whineSound: Holder<SoundEvent>
) {

    companion object {

        private val wolfSoundVariantCodec = RecordCodecBuilder.create { builder ->
            builder.group(
                SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(WolfSoundVariant::ambientSound),
                SoundEvent.CODEC.fieldOf("death_sound").forGetter(WolfSoundVariant::deathSound),
                SoundEvent.CODEC.fieldOf("growl_sound").forGetter(WolfSoundVariant::growlSound),
                SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(WolfSoundVariant::hurtSound),
                SoundEvent.CODEC.fieldOf("pant_sound").forGetter(WolfSoundVariant::pantSound),
                SoundEvent.CODEC.fieldOf("whine_sound").forGetter(WolfSoundVariant::whineSound)
            ).apply(builder, ::WolfSoundVariant)
        }

        val DIRECT_CODEC: Codec<WolfSoundVariant> = wolfSoundVariantCodec
        val NETWORK_CODEC: Codec<WolfSoundVariant> = wolfSoundVariantCodec
        val CODEC: Codec<Holder<WolfSoundVariant>> = RegistryFixedCodec.create(ModRegistries.WOLF_SOUND_VARIANT)
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<WolfSoundVariant>> =
            ByteBufCodecs.holderRegistry(ModRegistries.WOLF_SOUND_VARIANT)

    }

}