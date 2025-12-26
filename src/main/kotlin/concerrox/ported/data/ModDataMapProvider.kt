package concerrox.ported.data

import concerrox.ported.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
    packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

    override fun gather(provider: HolderLookup.Provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(ModItems.PALE_MOSS_CARPET, Compostable(0.3f), false)
            .add(ModItems.OPEN_EYEBLOSSOM, Compostable(0.65f), false)
            .add(ModItems.CLOSED_EYEBLOSSOM, Compostable(0.65f), false)
            .add(ModItems.LEAF_LITTER, Compostable(0.3f), false)
            .add(ModItems.WILDFLOWERS, Compostable(0.3f), false)
            .add(ModItems.BUSH, Compostable(0.3f), false)
            .add(ModItems.FIREFLY_BUSH, Compostable(0.3f), false)
            .add(ModItems.CACTUS_FLOWER, Compostable(0.3f), false)
            .add(ModItems.SHORT_DRY_GRASS, Compostable(0.3f), false)

        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(ModItems.LEAF_LITTER, FurnaceFuel(100), false)
            .add(ModItems.SHORT_DRY_GRASS, FurnaceFuel(100), false)

        // TODO: flammable
    }

}