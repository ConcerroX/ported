package concerrox.ported.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import concerrox.ported.mixininterface.BundleContentsExtensions;
import concerrox.ported.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
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

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ClientBundleTooltip.class)
public abstract class ClientBundleTooltipMixin {

    @Shadow
    @Final
    private BundleContents contents;

    @Shadow
    public abstract int getWidth(Font font);

    @Shadow
    public abstract int getHeight();

    @Unique
    private static Font ported$font = Minecraft.getInstance().font;
    @Unique
    private static final int PROGRESSBAR_FILL_MAX = 94;
    @Unique
    private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    @Unique
    private static final ResourceLocation PROGRESSBAR_BORDER_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_border");
    @Unique
    private static final ResourceLocation PROGRESSBAR_FILL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
    @Unique
    private static final ResourceLocation PROGRESSBAR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_full");
    @Unique
    private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_back");
    @Unique
    private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_front");
    @Unique
    private static final ResourceLocation SLOT_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_background");
    @Unique
    private static final Component BUNDLE_FULL_TEXT = Component.translatable("item.minecraft.bundle.full");
    @Unique
    private static final Component BUNDLE_EMPTY_TEXT = Component.translatable("item.minecraft.bundle.empty");

    @Unique
    private static void drawEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics guiGraphics) {
        guiGraphics.drawWordWrap(font, BUNDLE_EMPTY_DESCRIPTION, x, y, 96, -5592406);
    }

    @Unique
    private int getContentXOffset(int width) {
        return (width - 96) / 2;
    }

    @Unique
    private void drawProgressbar(int x, int y, Font font, GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(getProgressBarTexture(), x + 1, y, getProgressBarFill(), 13);
        guiGraphics.blitSprite(PROGRESSBAR_BORDER_SPRITE, x, y, 96, 13);
        Component component = getProgressBarFillText();
        if (component != null) {
            guiGraphics.drawCenteredString(font, component, x + 48, y + 3, -1);
        }
    }

    @Nullable
    private Component getProgressBarFillText() {
        if (contents.isEmpty()) {
            return BUNDLE_EMPTY_TEXT;
        } else {
            return contents.weight().compareTo(Fraction.ONE) >= 0 ? BUNDLE_FULL_TEXT : null;
        }
    }

    @Unique
    private int getProgressBarFill() {
        return Mth.clamp(Mth.mulAndTruncate(contents.weight(), PROGRESSBAR_FILL_MAX), 0, PROGRESSBAR_FILL_MAX);
    }

    @Unique
    private ResourceLocation getProgressBarTexture() {
        return contents.weight().compareTo(Fraction.ONE) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
    }

    @Unique
    private void renderEmptyBundleTooltip(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        drawEmptyBundleDescriptionText(x + getContentXOffset(width), y, font, guiGraphics);
        drawProgressbar(x + getContentXOffset(width), y + getEmptyBundleDescriptionTextHeight(font) + 4, font, guiGraphics);
    }

    @Unique
    private static int getEmptyBundleDescriptionTextHeight(Font font) {
        return font.split(BUNDLE_EMPTY_DESCRIPTION, 96).size() * 9;
    }

    @Inject(method = "getWidth", at = @At("RETURN"), cancellable = true)
    private void getWidth(Font font, CallbackInfoReturnable<Integer> cir) {
        if (ported$font != font) ported$font = font;
        cir.setReturnValue(96);
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    private void getHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(getHeight(ported$font));
    }

    @Unique
    public int getHeight(Font font) {
        return contents.isEmpty() ? getEmptyBundleBackgroundHeight(font) : backgroundHeight();
    }

    @Unique
    private static int getEmptyBundleBackgroundHeight(Font font) {
        return getEmptyBundleDescriptionTextHeight(font) + 13 + 8;
    }

    @Unique
    private int backgroundHeight() {
        return itemGridHeight() + 13 + 8;
    }

    @Unique
    private int itemGridHeight() {
        return gridSizeY() * 24;
    }

    @Unique
    private int gridSizeY() {
        return Mth.positiveCeilDiv(slotCount(), 4);
    }

    @Unique
    private int slotCount() {
        return Math.min(12, contents.size());
    }

    @Unique
    private List<ItemStack> getShownItems(int itemsToShow) {
        int i = Math.min(contents.size(), itemsToShow);
        return contents.itemCopyStream().toList().subList(0, i);
    }

    @Unique
    private static boolean shouldRenderSurplusText(boolean hasEnoughItems, int cellX, int cellY) {
        return hasEnoughItems && cellX * cellY == 1;
    }

    @Unique
    private static boolean shouldRenderItemSlot(List<ItemStack> shownItems, int slotIndex) {
        return shownItems.size() >= slotIndex;
    }

    @Unique
    private int getAmountOfHiddenItems(List<ItemStack> shownItems) {
        return contents.itemCopyStream().skip(shownItems.size()).mapToInt(ItemStack::getCount).sum();
    }

    @Unique
    private void renderSlot(int slotIndex, int x, int y, List<ItemStack> shownItems, int seed, Font font,
                            GuiGraphics guiGraphics) {
        int i = shownItems.size() - slotIndex;
        boolean isSelected = i == BundleContentsExtensions.class.cast(contents).getSelectedItem();
        ItemStack itemstack = shownItems.get(i);
        if (isSelected) {
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(SLOT_HIGHLIGHT_BACK_SPRITE, x, y, 24, 24);
            RenderSystem.disableBlend();
        } else {
            guiGraphics.blitSprite(SLOT_BACKGROUND_SPRITE, x, y, 24, 24);
        }

        guiGraphics.renderItem(itemstack, x + 4, y + 4, seed);
        guiGraphics.renderItemDecorations(font, itemstack, x + 4, y + 4);
        if (isSelected) {
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(SLOT_HIGHLIGHT_FRONT_SPRITE, x, y, 24, 24);
            RenderSystem.disableBlend();
        }
    }

    @Unique
    private static void renderCount(int slotX, int slotY, int count, Font font, GuiGraphics guiGraphics) {
        guiGraphics.drawCenteredString(font, "+" + count, slotX + 12, slotY + 10, -1);
    }

    @Unique
    private void drawSelectedItemTooltip(Font font, GuiGraphics guiGraphics, int x, int y, int width) {
        var contentsExt = BundleContentsExtensions.class.cast(contents);
        if (contentsExt.hasSelectedItem()) {
            ItemStack itemstack = contents.getItemUnsafe(contentsExt.getSelectedItem());
            Component component = ItemStackUtils.getStyledHoverName(itemstack);
            int i = font.width(component.getVisualOrderText());
            int j = x + width / 2 - 12;
            FormattedCharSequence clienttooltipcomponent = component.getVisualOrderText();
            guiGraphics.renderTooltip(font, List.of(clienttooltipcomponent), DefaultTooltipPositioner.INSTANCE, j - i / 2, y - 15);//, itemstack.get(DataComponents.TOOLTIP_STYLE));
        }
    }

    @Unique
    private void renderBundleWithItemsTooltip(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        boolean flag = contents.size() > 12;
        List<ItemStack> list = getShownItems(BundleContentsExtensions.class.cast(contents).getNumberOfItemsToShow());
        int i = x + getContentXOffset(width) + 96;
        int j = y + gridSizeY() * 24;
        int k = 1;

        for (int l = 1; l <= gridSizeY(); l++) {
            for (int i1 = 1; i1 <= 4; i1++) {
                int j1 = i - i1 * 24;
                int k1 = j - l * 24;
                if (shouldRenderSurplusText(flag, i1, l)) {
                    renderCount(j1, k1, getAmountOfHiddenItems(list), font, guiGraphics);
                } else if (shouldRenderItemSlot(list, k)) {
                    renderSlot(k, j1, k1, list, k, font, guiGraphics);
                    k++;
                }
            }
        }
        drawSelectedItemTooltip(font, guiGraphics, x, y, width);
        drawProgressbar(x + getContentXOffset(width), y + itemGridHeight() + 4, font, guiGraphics);
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void renderImage(Font font, int x, int y, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (contents.isEmpty()) {
            renderEmptyBundleTooltip(font, x, y, getWidth(font), getHeight(), guiGraphics);
        } else {
            renderBundleWithItemsTooltip(font, x, y, getWidth(font), getHeight(), guiGraphics);
        }
        ci.cancel();
    }

}
