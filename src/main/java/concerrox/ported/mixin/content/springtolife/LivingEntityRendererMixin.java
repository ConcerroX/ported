package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.ported.content.springtolife.mobvariant.MobVariant;
import concerrox.ported.content.springtolife.mobvariant.MobVariantManager;
import concerrox.ported.registry.ModAttachmentTypes;
import kotlin.jvm.functions.Function1;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Unique
    private static final Map<ModelLayerLocation, EntityModel<?>> ported$bakedModels = new HashMap<>();
    @Unique
    private static final Map<MobVariant<?>, MobVariantManager.AdultAndBabyModelPair<?>> ported$variantToBakedModels = new HashMap<>();
    @Unique
    private static boolean ported$isVariantModelsBaked = false;

    @Unique
    private M ported$originalModel;
    @Shadow
    protected M model;

    private LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, M model, float shadowRadius, CallbackInfo ci) {
        if (!ported$isVariantModelsBaked) {
            MobVariantManager.INSTANCE.getKeyToVariant$ported_neoforge_1_21_1().forEach((key, variant) -> {
                Objects.requireNonNull(variant);
                var modelData = variant.getTextureModelData();
                var constructor = modelData.entityModelConstructor();
                var adultModel = ported$bakeOrGetCachedBakedModel(context, constructor, modelData.adultModel());
                var babyModel = ported$bakeOrGetCachedBakedModel(context, constructor, modelData.babyModel());
                ported$variantToBakedModels.put(variant, new MobVariantManager.AdultAndBabyModelPair<>(adultModel, babyModel));
            });
            ported$isVariantModelsBaked = true;
        }
        ported$originalModel = this.model;
    }

    @Unique
    private EntityModel<?> ported$bakeOrGetCachedBakedModel(EntityRendererProvider.Context context,
                                                            Function1<@NotNull ModelPart, ? extends @NotNull EntityModel<?>> constructor,
                                                            ModelLayerLocation location) {
        if (location == null) return null;
        return ported$bakedModels.computeIfAbsent(location, (loc) -> constructor.invoke(context.bakeLayer(location)));
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"))
    private void beforeRender(T entity, float entityYaw, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        var variantAttachment = ModAttachmentTypes.INSTANCE.getMOB_VARIANT();
        if (entity.hasData(variantAttachment)) {
            model = (M) ported$variantToBakedModels.get(entity.getData(variantAttachment)).getModel(entity.isBaby());
        } else if (MobVariantManager.INSTANCE.getKeyToVariant$ported_neoforge_1_21_1().containsKey(entity.getType())) {
            model = ported$originalModel;
        }
    }

    @WrapOperation(
            method = "getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation ported$replaceTextureLocation(LivingEntityRenderer<?, ?> instance, Entity entity,
                                                           Operation<ResourceLocation> original) {
        var variantAttachment = ModAttachmentTypes.INSTANCE.getMOB_VARIANT();
        if (entity instanceof LivingEntity living && living.hasData(variantAttachment)) {
            MobVariant<?> variant = living.getData(variantAttachment);
            return variant.getTextureModelData().textureLocation();
        } else {
            return original.call(instance, entity);
        }
    }

}
