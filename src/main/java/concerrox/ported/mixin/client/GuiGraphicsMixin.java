package concerrox.ported.mixin.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.ported.content.bundlesofbravery.bundle.BundleItemUtils;
import concerrox.ported.event.ClientModEventHandler;
import concerrox.ported.registry.ModItemTags;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.HashMap;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    public abstract void flush();

    @Shadow
    public abstract MultiBufferSource.BufferSource bufferSource();

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void renderItemWithBundle(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed,
                                     int guiOffset, CallbackInfo ci) {
        if (stack == null || stack.isEmpty()) return;
        if (stack.is(ModItemTags.INSTANCE.getBUNDLES())) {
            BakedModel bakedmodel = minecraft.getItemRenderer().getModel(stack, level, entity, seed);
            pose.pushPose();
            pose.translate(x + 8f, y + 8f, 150f + (bakedmodel.isGui3d() ? guiOffset : 0));
            try {
                pose.scale(16f, -16f, 16f);
                boolean useBlockLight = !bakedmodel.usesBlockLight();
                if (useBlockLight) Lighting.setupForFlatItems();
                ported$renderBundleItem(minecraft.getItemRenderer(), stack, ItemDisplayContext.GUI, false, pose, bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel, level, entity, seed);
                flush();
                if (useBlockLight) Lighting.setupFor3DItems();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory category = crashreport.addCategory("Item being rendered");
                category.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                category.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                category.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                throw new ReportedException(crashreport);
            }
            pose.popPose();
            ci.cancel();
        }
    }

    @Unique
    private static boolean ported$shouldRenderItemFlat(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED;
    }

    @Unique
    private BakedModel ported$resolveModelOverride(BakedModel model, ItemStack stack, @Nullable Level level,
                                                   @Nullable LivingEntity entity, int seed) {
        ClientLevel clientlevel = level instanceof ClientLevel ? (ClientLevel) level : null;
        BakedModel bakedmodel = model.getOverrides().resolve(model, stack, clientlevel, entity, seed);
        return bakedmodel == null ? model : bakedmodel;
    }

    @Unique
    private void ported$renderItemModelRaw(ItemRenderer renderer, ItemStack p_371318_,
                                           ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack,
                                           MultiBufferSource p_371946_, int combinedLight, int p_371508_,
                                           BakedModel model, boolean p_371718_, float p_371782_) {
        poseStack.pushPose();
        model = ClientHooks.handleCameraTransforms(poseStack, model, displayContext, leftHand);
        poseStack.translate(0f, 0f, p_371782_);
        renderer.render(p_371318_, displayContext, leftHand, poseStack, p_371946_, combinedLight, p_371508_, model);
        poseStack.popPose();
    }

    @Unique
    private static final HashMap<BundleItem, ModelResourceLocation> PORTED_$_BUNDLE_BACK_MODELS = HashMap.newHashMap(17);
    @Unique
    private static final HashMap<BundleItem, ModelResourceLocation> PORTED_$_BUNDLE_FRONT_MODELS = HashMap.newHashMap(17);

    @Unique
    public BakedModel ported$getItemModel(ItemModelShaper shaper, ModelResourceLocation location) {
        return shaper.getModelManager().getModel(location);
    }

    @Unique
    public void ported$renderBundleItem(ItemRenderer renderer, ItemStack stack, ItemDisplayContext displayContext,
                                        boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource,
                                        int combinedLight, int combineOverlay, BakedModel model, @Nullable Level level,
                                        @Nullable LivingEntity entity, int seed) {
        Item item = stack.getItem();
        if (item instanceof BundleItem bundleItem) {
            if (BundleItemUtils.INSTANCE.hasSelectedItem(stack)) {
                boolean isFlat = ported$shouldRenderItemFlat(displayContext);

                var backModelRes = PORTED_$_BUNDLE_BACK_MODELS.computeIfAbsent(bundleItem, ClientModEventHandler.INSTANCE::createBundleOpenBackModelResourceLocation);
                BakedModel backModel = ported$resolveModelOverride(ported$getItemModel(renderer.getItemModelShaper(), backModelRes), stack, level, entity, seed);
                ported$renderItemModelRaw(renderer, stack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combineOverlay, backModel, isFlat, -1.5F);

                ItemStack content = BundleItemUtils.INSTANCE.getSelectedItemStack(stack);
                BakedModel contentModel = renderer.getModel(content, level, entity, seed);
                renderer.render(content, displayContext, leftHand, poseStack, bufferSource, combinedLight, combineOverlay, contentModel);

                var frontModelRes = PORTED_$_BUNDLE_FRONT_MODELS.computeIfAbsent(bundleItem, ClientModEventHandler.INSTANCE::createBundleOpenFrontModelResourceLocation);
                BakedModel frontModel = ported$resolveModelOverride(ported$getItemModel(renderer.getItemModelShaper(), frontModelRes), stack, level, entity, seed);
                ported$renderItemModelRaw(renderer, stack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combineOverlay, frontModel, isFlat, 0.5F);
            } else {
                renderer.render(stack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combineOverlay, model);
            }
        }
    }

}
