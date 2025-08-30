package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.entity.SalmonVariant
import concerrox.ported.util.new
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object ModAttachmentTypes {

    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ported.MOD_ID)

    val SALMON_SIZE = ATTACHMENT_TYPES.new("salmon/size") {
        AttachmentType.builder(SalmonVariant::DEFAULT).serialize(SalmonVariant.CODEC).sync(SalmonVariant.STREAM_CODEC)
            .build()
    }

}