package concerrox.ported.content.springtolife.sheep

import concerrox.ported.registry.ModBiomeTags
import net.minecraft.core.Holder
import net.minecraft.util.RandomSource
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.biome.Biome

object SheepColorSpawnRules {

    private val TEMPERATE_SPAWN_CONFIGURATION = SheepColorSpawnConfiguration(
        weighted(
            builder().add(single(DyeColor.BLACK), 5).add(single(DyeColor.GRAY), 5).add(single(DyeColor.LIGHT_GRAY), 5)
                .add(single(DyeColor.BROWN), 3).add(commonColors(DyeColor.WHITE), 82).build()
        )
    )
    private val WARM_SPAWN_CONFIGURATION = SheepColorSpawnConfiguration(
        weighted(
            builder().add(single(DyeColor.GRAY), 5).add(single(DyeColor.LIGHT_GRAY), 5).add(single(DyeColor.WHITE), 5)
                .add(single(DyeColor.BLACK), 3).add(commonColors(DyeColor.BROWN), 82).build()
        )
    )
    private val COLD_SPAWN_CONFIGURATION = SheepColorSpawnConfiguration(
        weighted(
            builder().add(single(DyeColor.LIGHT_GRAY), 5).add(single(DyeColor.GRAY), 5).add(single(DyeColor.WHITE), 5)
                .add(single(DyeColor.BROWN), 3).add(commonColors(DyeColor.BLACK), 82).build()
        )
    )

    private fun commonColors(mainColor: DyeColor): SheepColorProvider {
        return weighted(builder().add(single(mainColor), 499).add(single(DyeColor.PINK), 1).build())
    }

    fun getSheepColor(biome: Holder<Biome>, random: RandomSource): DyeColor {
        return getSheepColorConfiguration(biome).colors.get(random)
    }

    private fun getSheepColorConfiguration(biome: Holder<Biome>): SheepColorSpawnConfiguration {
        return if (biome.`is`(ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)) {
            WARM_SPAWN_CONFIGURATION
        } else if (biome.`is`(ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)) {
            COLD_SPAWN_CONFIGURATION
        } else {
            TEMPERATE_SPAWN_CONFIGURATION
        }
    }

    private fun weighted(colors: SimpleWeightedRandomList<SheepColorProvider>): SheepColorProvider {
        require(!colors.isEmpty) { "List must be non-empty" }
        return SheepColorProvider { random ->
            colors.getRandomValue(random).get().get(random)
        }
    }

    private fun single(color: DyeColor): SheepColorProvider {
        return SheepColorProvider { color }
    }

    private fun builder(): SimpleWeightedRandomList.Builder<SheepColorProvider> {
        return SimpleWeightedRandomList.builder()
    }

    @JvmRecord
    internal data class SheepColorSpawnConfiguration(val colors: SheepColorProvider)

    internal fun interface SheepColorProvider {
        fun get(random: RandomSource): DyeColor
    }

}