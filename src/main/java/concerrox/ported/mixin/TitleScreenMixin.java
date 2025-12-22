package concerrox.ported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.Ported;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @WrapOperation(method = "lambda$render$13", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"))
    public int injectBranding(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        if (text.startsWith("Minecraft")) text += " - Ported to " + Ported.PORTED_TARGET_VERSION;
        return original.call(instance, font, text, x, y, color);
    }

}
