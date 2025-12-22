package concerrox.ported.mixin.client;

import concerrox.ported.content.bundlesofbravery.bundle.BundleMouseActions;
import concerrox.ported.gui.ItemSlotMouseAction;
import concerrox.ported.mixininterface.AbstractContainerScreenExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements AbstractContainerScreenExtensions, ContainerEventHandler {

    @Unique
    public Slot ported$lastHoveredSlot = null;

    @Unique
    private List<ItemSlotMouseAction> ported$itemSlotMouseActions = new ArrayList<>();

    @Unique
    protected void addItemSlotMouseAction(ItemSlotMouseAction itemSlotMouseAction) {
        this.ported$itemSlotMouseActions.add(itemSlotMouseAction);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        ported$itemSlotMouseActions.clear();
        this.addItemSlotMouseAction(new BundleMouseActions(Minecraft.getInstance()));
    }

    @Override
    public @NotNull List<@NotNull ItemSlotMouseAction> getItemSlotMouseActions() {
        return ported$itemSlotMouseActions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setItemSlotMouseActions(@NotNull List<? extends @NotNull ItemSlotMouseAction> itemSlotMouseActions) {
        ported$itemSlotMouseActions = (List<ItemSlotMouseAction>) itemSlotMouseActions;
    }

    @Unique
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (ported$lastHoveredSlot != null && ported$lastHoveredSlot.hasItem()) {
            for (ItemSlotMouseAction mouseAction : getItemSlotMouseActions()) {
                if (mouseAction.matches(ported$lastHoveredSlot) && mouseAction.onMouseScrolled(scrollX, scrollY, ported$lastHoveredSlot.index, ported$lastHoveredSlot.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Unique
    private void onStopHovering(Slot slot) {
        if (slot == null) return;
        if (slot.hasItem()) {
            for (ItemSlotMouseAction itemslotmouseaction : ported$itemSlotMouseActions) {
                if (itemslotmouseaction.matches(slot)) {
                    itemslotmouseaction.onStopHovering(slot);
                }
            }
        }
    }

    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;IIF)V",
            at = @At("TAIL"))
    private void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick,
                                     CallbackInfo ci) {
        if (ported$lastHoveredSlot != slot) {
            onStopHovering(ported$lastHoveredSlot);
            ported$lastHoveredSlot = slot;
        }
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        onStopHovering(ported$lastHoveredSlot);
    }

    @Override
    @NotNull
    public Slot getLastHoveredSlot() {
        return ported$lastHoveredSlot;
    }

}
