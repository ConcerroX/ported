package concerrox.ported.mixin.content.springtolife;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin {

    @Inject(method = "getViewDistance", at = @At("RETURN"), cancellable = true)
    private void getViewDistance(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Minecraft.getInstance().options.getEffectiveRenderDistance() * 16);
    }

}
