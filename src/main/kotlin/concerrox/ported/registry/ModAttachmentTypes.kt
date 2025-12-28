package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.content.bundlesofbravery.salmon.SalmonVariant
import concerrox.ported.content.springtolife.mobvariant.MobVariant
import concerrox.ported.content.springtolife.wolf.WolfSoundVariant
import concerrox.ported.util.new
import net.minecraft.core.Holder
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

    val ATTACHMENT_TYPES_PORTED: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ported.MOD_ID)

    val SALMON_SIZE = ATTACHMENT_TYPES_PORTED.new("salmon/size") {
        AttachmentType.builder(SalmonVariant::DEFAULT).serialize(SalmonVariant.CODEC).sync(SalmonVariant.STREAM_CODEC)
            .build()
    }
    val MOB_VARIANT = ATTACHMENT_TYPES_PORTED.new("mob_variant") {
        AttachmentType.builder(Supplier<MobVariant<*>?> { null }).serialize(MobVariant.CODEC)
            .sync(MobVariant.STREAM_CODEC).build()
    }

    val WOLF_SOUND_VARIANT = ATTACHMENT_TYPES_PORTED.new("wolf/sound_variant") {
        AttachmentType.builder(Supplier<Holder<WolfSoundVariant>> { null!! }).serialize(WolfSoundVariant.CODEC)
            .sync(WolfSoundVariant.STREAM_CODEC).build()
    }

}