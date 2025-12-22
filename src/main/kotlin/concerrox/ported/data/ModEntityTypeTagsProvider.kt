package concerrox.ported.data

import concerrox.ported.registry.ModEntityTypes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : EntityTypeTagsProvider(output, lookupProvider, ResourceLocation.DEFAULT_NAMESPACE, existingFileHelper) {

    override fun addTags(provider: HolderLookup.Provider) {
        tag(Tags.EntityTypes.BOATS).add(ModEntityTypes.PALE_OAK_BOAT.get())
        tag(Tags.EntityTypes.BOATS).add(ModEntityTypes.PALE_OAK_CHEST_BOAT.get())
    }

}