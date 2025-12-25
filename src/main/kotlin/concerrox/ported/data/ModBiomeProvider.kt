package concerrox.ported.data

import concerrox.ported.registry.ModBiomes
import concerrox.ported.registry.ModPlacedFeatures
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.biome.Biome.BiomeBuilder
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object ModBiomeProvider {

    fun bootstrap(context: BootstrapContext<Biome>) {
        val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
        val configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER)

        context.register(ModBiomes.PALE_GARDEN, paleGarden(placedFeatures, configuredCarvers))
    }

    private fun paleGarden(
        placedFeatures: HolderGetter<PlacedFeature>,
        worldCarvers: HolderGetter<ConfiguredWorldCarver<*>>,
    ): Biome {
        val generation = BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).apply {
            addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_VEGETATION)
            addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_MOSS_PATCH)
            addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_FLOWERS)
            addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FLOWER_PALE_GARDEN)
            // TODO: firefly bush addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER)
        }

        val mobSpawning = MobSpawnSettings.Builder()
        BiomeDefaultFeatures.commonSpawns(mobSpawning)
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation)
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation)
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation)
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation)
        BiomeDefaultFeatures.addDefaultSprings(generation)
        BiomeDefaultFeatures.addSurfaceFreezing(generation)
        BiomeDefaultFeatures.addDefaultOres(generation)
        BiomeDefaultFeatures.addDefaultSoftDisks(generation)
        BiomeDefaultFeatures.addForestGrass(generation)
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation)

        return BiomeBuilder().apply {
            hasPrecipitation(true)
            temperature(0.7f)
            downfall(0.8f)
            specialEffects(
                BiomeSpecialEffects.Builder().apply {
                    waterColor(7768221)
                    waterFogColor(5597568)
                    fogColor(8484720)
                    skyColor(12171705)
                    grassColorOverride(7832178)
                    foliageColorOverride(8883574)
                    // TODO: litter dryFoliageColorOverride(10528412)
                    ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    backgroundMusic(null)
                }.build()
            )
            mobSpawnSettings(mobSpawning.build())
            generationSettings(generation.build())
        }.build()
    }

}