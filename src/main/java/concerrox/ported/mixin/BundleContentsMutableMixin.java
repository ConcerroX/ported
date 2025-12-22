package concerrox.ported.mixin;

import concerrox.ported.content.bundlesofbravery.bundle.BundleContentsMutableExtensionsKt;
import concerrox.ported.mixininterface.BundleContentsExtensions;
import concerrox.ported.mixininterface.BundleContentsMutableExtensions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.Mutable.class)
public class BundleContentsMutableMixin implements BundleContentsMutableExtensions {

    @Shadow
    @Final
    private List<ItemStack> items;

    @Shadow
    private Fraction weight;
    @Unique
    private int ported$selectedItem;

    @Unique
    @Override
    public int getSelectedItem() {
        return ported$selectedItem;
    }

    @Unique
    @Override
    public void setSelectedItem(int i) {
        ported$selectedItem = i;
    }

    @Unique
    @Override
    public boolean indexIsOutsideAllowedBounds(int index) {
        return index < 0 || index >= items.size();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(BundleContents contents, CallbackInfo ci) {
        var extensions = BundleContentsExtensions.class.cast(contents);
        ported$selectedItem = extensions.getSelectedItem();
    }

    @Inject(method = "clearItems", at = @At("TAIL"))
    private void clearItems(CallbackInfoReturnable<BundleContents.Mutable> cir) {
        ported$selectedItem = -1;
    }

    @Inject(method = "removeOne", at = @At("HEAD"), cancellable = true)
    private void removeOne(CallbackInfoReturnable<ItemStack> cir) {
        if (this.items.isEmpty()) {
            cir.setReturnValue(null);
        } else {
            int i = indexIsOutsideAllowedBounds(ported$selectedItem) ? 0 : ported$selectedItem;
            ItemStack stack = items.remove(i).copy();
            weight = weight.subtract(BundleContents.getWeight(stack)
                    .multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
            BundleContentsMutableExtensionsKt.toggleSelectedItem(BundleContents.Mutable.class.cast(this), -1);
            cir.setReturnValue(stack);
        }
    }

}
