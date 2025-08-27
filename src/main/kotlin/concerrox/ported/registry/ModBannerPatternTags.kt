package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.entity.BannerPattern

object ModBannerPatternTags {

    val PATTERN_ITEM_FIELD_MASONED = create("pattern_item/field_masoned")
    val PATTERN_ITEM_BORDURE_INDENTED = create("pattern_item/bordure_indented")

    private fun create(name: String): TagKey<BannerPattern> {
        return TagKey.create(Registries.BANNER_PATTERN, ResourceLocation.withDefaultNamespace(name))
    }

}