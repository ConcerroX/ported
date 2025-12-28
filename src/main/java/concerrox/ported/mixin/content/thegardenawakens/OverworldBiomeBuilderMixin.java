package concerrox.ported.mixin.content.thegardenawakens;

import com.mojang.datafixers.util.Pair;
import concerrox.ported.content.thegardenawakens.palegarden.PaleGardenCreator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public class OverworldBiomeBuilderMixin {

    @Inject(method = "addBiomes", at = @At("TAIL"))
    private void beforePickPlateauBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> key, CallbackInfo ci) {
        PaleGardenCreator.INSTANCE.register(key);
    }

}
