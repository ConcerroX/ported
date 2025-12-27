package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.mobvariant.model.CowModel;
import concerrox.ported.content.springtolife.mobvariant.model.MushroomCowMushroomLayer;
import concerrox.ported.registry.ModModelLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRenderer {

    @Unique
    private EntityModel<?> ported$newModel;

    @SuppressWarnings("unchecked")
    public MushroomCowRendererMixin(EntityRendererProvider.Context context, EntityModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onInit(EntityRendererProvider.Context p_174324_, CallbackInfo ci) {
        model = new CowModel(p_174324_.bakeLayer(ModModelLayers.INSTANCE.getMOOSHROOM_NEW()));
        layers.clear();
        addLayer(new MushroomCowMushroomLayer(this, p_174324_.getBlockRenderDispatcher()));
//        ported$newModel = model;
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"))
    private void onRender(Entity par1, CallbackInfoReturnable<ResourceLocation> cir) {
//        model = ported$newModel;
    }

}
