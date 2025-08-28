package concerrox.ported.mixin;

import concerrox.ported.registry.ModSoundEvents;
import concerrox.ported.util.BundleContentsMutableExtensionsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @Shadow
    protected abstract void playInsertSound(Entity entity);

    @Shadow
    protected abstract void playRemoveOneSound(Entity entity);

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                 TooltipFlag tooltipFlag, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    private void ported$broadcastChangesOnContainerMenu(Player player) {
        var menu = player.containerMenu;
        if (menu != null) menu.slotsChanged(player.getInventory());
    }

    @Unique
    private static void ported$playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.INSTANCE.getBUNDLE_INSERT_FAIL(), 1.0F, 1.0F);
    }

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player,
                                        CallbackInfoReturnable<Boolean> cir) {
        var contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null && stack.getCount() == 1) {
            ItemStack itemstack = slot.getItem();
            BundleContents.Mutable bundlecontents$mutable = new BundleContents.Mutable(contents);
            if (action == ClickAction.PRIMARY && !itemstack.isEmpty()) {
                if (bundlecontents$mutable.tryTransfer(slot, player) > 0) {
                    playInsertSound(player);
                } else {
                    ported$playInsertFailSound(player);
                }
                stack.set(DataComponents.BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                ported$broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else if (action == ClickAction.SECONDARY && itemstack.isEmpty()) {
                ItemStack removed = bundlecontents$mutable.removeOne();
                if (removed != null) {
                    ItemStack itemstack2 = slot.safeInsert(removed);
                    if (itemstack2.getCount() > 0) {
                        bundlecontents$mutable.tryInsert(itemstack2);
                    } else {
                        playRemoveOneSound(player);
                    }
                }
                stack.set(DataComponents.BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                ported$broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }

    @Unique
    public void ported$toggleSelectedItem(ItemStack bundle, int selectedItem) {
        BundleContents bundlecontents = bundle.get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            var mutable = new BundleContents.Mutable(bundlecontents);
            BundleContentsMutableExtensionsKt.toggleSelectedItem(mutable, selectedItem);
            bundle.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player,
                                         SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getCount() != 1) {
            cir.setReturnValue(false);
        } else if (action == ClickAction.PRIMARY && other.isEmpty()) {
            ported$toggleSelectedItem(stack, -1);
            cir.setReturnValue(false);
        } else {
            BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (contents == null) {
                cir.setReturnValue(false);
            } else {
                var mutable = new BundleContents.Mutable(contents);
                if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                    if (slot.allowModification(player) && mutable.tryInsert(other) > 0) {
                        playInsertSound(player);
                    } else {
                        ported$playInsertFailSound(player);
                    }
                    stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
                    ported$broadcastChangesOnContainerMenu(player);
                    cir.setReturnValue(true);
                } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack itemstack = mutable.removeOne();
                        if (itemstack != null) {
                            playRemoveOneSound(player);
                            access.set(itemstack);
                        }
                    }
                    stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
                    ported$broadcastChangesOnContainerMenu(player);
                    cir.setReturnValue(true);
                } else {
                    ported$toggleSelectedItem(stack, -1);
                    cir.setReturnValue(false);
                }
            }
        }
    }

}
