package concerrox.ported.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerPatternItem.class)
public class BannerPatternItemMixin extends Item {

    public BannerPatternItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                 TooltipFlag tooltipFlag, CallbackInfo ci) {
        ci.cancel();
    }

}
