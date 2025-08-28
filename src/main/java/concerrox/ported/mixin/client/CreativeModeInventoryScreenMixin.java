package concerrox.ported.mixin.client;

import concerrox.ported.client.gui.ItemSlotMouseAction;
import concerrox.ported.mixininterface.AbstractContainerScreenExtensions;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    public void mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY,
                              CallbackInfoReturnable<Boolean> cir) {
        var extensions = AbstractContainerScreenExtensions.class.cast(this);
        var slot = extensions.getLastHoveredSlot();
        if (slot != null && slot.hasItem()) {
            for (ItemSlotMouseAction mouseAction : extensions.getItemSlotMouseActions()) {
                if (mouseAction.matches(slot) && mouseAction.onMouseScrolled(scrollX, scrollY, slot.index, slot.getItem())) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

}
