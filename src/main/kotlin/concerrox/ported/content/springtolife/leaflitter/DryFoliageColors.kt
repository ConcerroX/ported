package concerrox.ported.content.springtolife.leaflitter

import concerrox.ported.registry.ModBiomes
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes

object DryFoliageColors {

    private val biomeColors = mapOf(
        Biomes.BADLANDS to 0xFF9E814D.toInt(),
        Biomes.ERODED_BADLANDS to 0xFF9E814D.toInt(),
        Biomes.WOODED_BADLANDS to 0xFF9E814D.toInt(),

        Biomes.DESERT to 0xFFA38046.toInt(),
        Biomes.SAVANNA to 0xFFA38046.toInt(),
        Biomes.SAVANNA_PLATEAU to 0xFFA38046.toInt(),
        Biomes.WINDSWEPT_SAVANNA to 0xFFA38046.toInt(),
        Biomes.NETHER_WASTES to 0xFFA38046.toInt(),
        Biomes.SOUL_SAND_VALLEY to 0xFFA38046.toInt(),
        Biomes.CRIMSON_FOREST to 0xFFA38046.toInt(),
        Biomes.WARPED_FOREST to 0xFFA38046.toInt(),
        Biomes.BASALT_DELTAS to 0xFFA38046.toInt(),

        Biomes.STONY_PEAKS to 0xFF927957.toInt(),

        Biomes.JUNGLE to 0xFFA36346.toInt(),
        Biomes.BAMBOO_JUNGLE to 0xFFA36346.toInt(),
        Biomes.SPARSE_JUNGLE to 0xFFA36346.toInt(),

        Biomes.MUSHROOM_FIELDS to 0xFFA36246.toInt(),

        Biomes.PLAINS to 0xFFA37546.toInt(),
        Biomes.SUNFLOWER_PLAINS to 0xFFA37546.toInt(),
        Biomes.BEACH to 0xFFA37546.toInt(),
        Biomes.DEEP_DARK to 0xFFA37546.toInt(),

        Biomes.DARK_FOREST to 0xFF7B5334.toInt(),
        Biomes.SWAMP to 0xFF7B5334.toInt(),
        Biomes.MANGROVE_SWAMP to 0xFF7B5334.toInt(),

        Biomes.FOREST to 0xFFA36D46.toInt(),
        Biomes.FLOWER_FOREST to 0xFFA36D46.toInt(),

        ModBiomes.PALE_GARDEN to 0xFFA0A69C.toInt(),

        Biomes.BIRCH_FOREST to 0xFFA37246.toInt(),
        Biomes.OLD_GROWTH_BIRCH_FOREST to 0xFFA37246.toInt(),

        Biomes.OCEAN to 0xFFA17448.toInt(),
        Biomes.DEEP_OCEAN to 0xFFA17448.toInt(),
        Biomes.WARM_OCEAN to 0xFFA17448.toInt(),
        Biomes.LUKEWARM_OCEAN to 0xFFA17448.toInt(),
        Biomes.DEEP_LUKEWARM_OCEAN to 0xFFA17448.toInt(),
        Biomes.COLD_OCEAN to 0xFFA17448.toInt(),
        Biomes.DEEP_COLD_OCEAN to 0xFFA17448.toInt(),
        Biomes.DEEP_FROZEN_OCEAN to 0xFFA17448.toInt(),
        Biomes.RIVER to 0xFFA17448.toInt(),
        Biomes.LUSH_CAVES to 0xFFA17448.toInt(),
        Biomes.THE_END to 0xFFA17448.toInt(),
        Biomes.SMALL_END_ISLANDS to 0xFFA17448.toInt(),
        Biomes.END_BARRENS to 0xFFA17448.toInt(),
        Biomes.END_MIDLANDS to 0xFFA17448.toInt(),
        Biomes.END_HIGHLANDS to 0xFFA17448.toInt(),
        Biomes.THE_VOID to 0xFFA17448.toInt(),

        Biomes.CHERRY_GROVE to 0xFFA17148.toInt(),
        Biomes.MEADOW to 0xFFA17148.toInt(),

        Biomes.OLD_GROWTH_PINE_TAIGA to 0xFF9C754D.toInt(),

        Biomes.TAIGA to 0xFF9A764F.toInt(),
        Biomes.OLD_GROWTH_SPRUCE_TAIGA to 0xFF9A764F.toInt(),

        Biomes.WINDSWEPT_HILLS to 0xFF977752.toInt(),
        Biomes.WINDSWEPT_GRAVELLY_HILLS to 0xFF977752.toInt(),
        Biomes.WINDSWEPT_FOREST to 0xFF977752.toInt(),
        Biomes.STONY_SHORE to 0xFF977752.toInt(),

        Biomes.SNOWY_BEACH to 0xFF917958.toInt(),

        Biomes.SNOWY_PLAINS to 0xFF8F7A5A.toInt(),
        Biomes.ICE_SPIKES to 0xFF8F7A5A.toInt(),
        Biomes.SNOWY_TAIGA to 0xFF8F7A5A.toInt(),
        Biomes.FROZEN_OCEAN to 0xFF8F7A5A.toInt(),
        Biomes.FROZEN_RIVER to 0xFF8F7A5A.toInt(),
        Biomes.GROVE to 0xFF8F7A5A.toInt(),
        Biomes.SNOWY_SLOPES to 0xFF8F7A5A.toInt(),
        Biomes.FROZEN_PEAKS to 0xFF8F7A5A.toInt(),
        Biomes.JAGGED_PEAKS to 0xFF8F7A5A.toInt()
    )

    fun getDryFoliageColor(biome: Biome): Int {
        return biomeColors[Minecraft.getInstance().level?.registryAccess()?.registryOrThrow(Registries.BIOME)
            ?.getResourceKey(biome)?.get()] ?: 0xFFA37546.toInt()
    }

}