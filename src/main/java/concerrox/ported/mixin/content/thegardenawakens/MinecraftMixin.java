package concerrox.ported.mixin.content.thegardenawakens;

import concerrox.ported.registry.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

//    @Inject(method = "getSituationalMusic", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
//    private void getSituationalMusic(CallbackInfoReturnable<Music> cir) {
//        if (player != null) {
//            Holder<Biome> biome = player.level().getBiome(this.player.blockPosition());
//            if (biome.value() == player.level().registryAccess().registryOrThrow(Registries.BIOME).get(ModBiomes.INSTANCE.getPALE_GARDEN())) {
//                cir.setReturnValue(Music(n));
//            }
//        }
//    }

}
