package concerrox.ported.mixin;

import concerrox.ported.mixininterface.SalmonExtensions;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Salmon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements SalmonExtensions {

    @Inject(method = "getDefaultDimensions", at = @At("RETURN"), cancellable = true)
    private void getDefaultDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if ((Object) this instanceof Salmon)
            cir.setReturnValue(cir.getReturnValue().scale(ported$getVariant().getBoundingBoxScale()));
    }

    @Inject(method = "getScale", at = @At("RETURN"), cancellable = true)
    private void getScale(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof Salmon)
            cir.setReturnValue(ported$getVariant().getBoundingBoxScale());
    }

}
