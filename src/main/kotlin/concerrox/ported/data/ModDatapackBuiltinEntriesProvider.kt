package concerrox.ported.data

import concerrox.ported.Ported
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.concurrent.CompletableFuture

class ModDatapackBuiltinEntriesProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DatapackBuiltinEntriesProvider(
        output, lookupProvider,
        RegistrySetBuilder().add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifierProvider::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatureProvider::bootstrap)
            .add(Registries.BIOME, ModBiomeProvider::bootstrap),
        setOf(ResourceLocation.DEFAULT_NAMESPACE, Ported.MOD_ID),
    )