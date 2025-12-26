package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ModBlockTags {

    val PALE_OAK_LOGS = create("pale_oak_logs")
    val DRY_VEGETATION_MAY_PLACE_ON = create("dry_vegetation_may_place_on")
    val TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS =
        create("triggers_ambient_desert_dry_vegetation_block_sounds")
    val EDIBLE_FOR_SHEEP = create("edible_for_sheep")

    private fun create(path: String): TagKey<Block> =
        TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(path))

}