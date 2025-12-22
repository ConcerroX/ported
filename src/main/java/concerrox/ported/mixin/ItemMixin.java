package concerrox.ported.mixin;

import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getOrCreateDescriptionId", at = @At("RETURN"), cancellable = true)
    private void getOrCreateDescriptionId(CallbackInfoReturnable<String> cir) {
        if ((Item) (Object) this instanceof BannerPatternItem || (Object) this instanceof SmithingTemplateItem) {
            cir.setReturnValue(cir.getReturnValue() + ".new");
        }
    }

}
