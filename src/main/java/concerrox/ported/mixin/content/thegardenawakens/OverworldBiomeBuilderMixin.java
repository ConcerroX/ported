//package concerrox.ported.mixin.content.thegardenawakens;
//
//import concerrox.ported.registry.ModBiomes;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.biome.Biomes;
//import net.minecraft.world.level.biome.OverworldBiomeBuilder;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(OverworldBiomeBuilder.class)
//public class OverworldBiomeBuilderMixin {
//
//    @Mutable
//    @Shadow
//    @Final
//    private ResourceKey<Biome>[][] PLATEAU_BIOMES;
//
//    @Unique
//    private void ported$init(CallbackInfo ci) {
//        PLATEAU_BIOMES = new ResourceKey[][]{
//                {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
//                {Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
//                {Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, ModBiomes.INSTANCE.getPALE_GARDEN()},
//                {Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE},
//                {Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}
//        };
//    }
//
//}
