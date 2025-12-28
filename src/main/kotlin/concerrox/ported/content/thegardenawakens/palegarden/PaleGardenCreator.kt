package concerrox.ported.content.thegardenawakens.palegarden

import com.mojang.datafixers.util.Pair
import concerrox.ported.registry.ModBiomes
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.biome.Climate.ParameterPoint
import java.util.function.Consumer

typealias Mapper = Consumer<Pair<ParameterPoint, ResourceKey<Biome>>>

object PaleGardenCreator {

    internal var isBiomeRegistered = false

    fun register(mapper: Consumer<Pair<ParameterPoint, ResourceKey<Biome>>>) {
        val erosion1 = Climate.Parameter.span(-0.78f, -0.375f)
        val erosion2 = Climate.Parameter.span(-0.375f, -0.2225f)
        val erosion3 = Climate.Parameter.span(-0.2225f, 0.05f)
        val peakVariant = Climate.Parameter.span(0.56666666f, 0.7666667f)
        val highSliceVariantAscending = Climate.Parameter.span(0.4f, 0.56666666f)
        val highSliceVariantDescending = Climate.Parameter.span(0.7666667f, 0.93333334f)
        val midSliceVariantAscending = Climate.Parameter.span(0.26666668f, 0.4f)
        val midSliceVariantDescending = Climate.Parameter.span(0.93333334f, 1.0f)
        val midInland = Climate.Parameter.span(0.03f, 0.3f)
        val farInland = Climate.Parameter.span(0.03f, 1f)
        val midToFarInland = Climate.Parameter.span(
            Climate.unquantizeCoord(midInland.min()), Climate.unquantizeCoord(farInland.max())
        )

        mapper.add(midToFarInland, erosion2, peakVariant)
        mapper.add(farInland, erosion3, peakVariant)
        mapper.add(midToFarInland, erosion2, highSliceVariantAscending)
        mapper.add(midToFarInland, erosion2, highSliceVariantDescending)
        mapper.add(farInland, erosion3, highSliceVariantAscending)
        mapper.add(farInland, erosion3, highSliceVariantDescending)
        mapper.add(farInland, erosion1, midSliceVariantAscending)
        mapper.add(farInland, erosion1, midSliceVariantDescending)
        mapper.add(farInland, erosion2, midSliceVariantAscending)
        mapper.add(farInland, erosion2, midSliceVariantDescending)
    }

    fun Mapper.add(
        continentalness: Climate.Parameter, erosion: Climate.Parameter, weirdness: Climate.Parameter
    ) {
        if (isBiomeRegistered) {
            add(continentalness, erosion, Climate.Parameter.point(0f), weirdness)
            add(continentalness, erosion, Climate.Parameter.point(1f), weirdness)
        }
    }

    fun Mapper.add(
        continentalness: Climate.Parameter,
        erosion: Climate.Parameter,
        depth: Climate.Parameter,
        weirdness: Climate.Parameter,
    ) {
        accept(
            Pair.of(
                Climate.parameters(
                    Climate.Parameter.span(-0.15f, 0.2f), Climate.Parameter.span(0.3f, 1f),
                    continentalness, erosion, depth, weirdness, Climate.quantizeCoord(0f).toFloat(),
                ), ModBiomes.PALE_GARDEN
            )
        )
    }

}