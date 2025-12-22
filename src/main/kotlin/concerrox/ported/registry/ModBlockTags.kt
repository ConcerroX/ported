package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ModBlockTags {

    val PALE_OAK_LOGS = create("pale_oak_logs")

    private fun create(path: String): TagKey<Block> =
        TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(path))

}