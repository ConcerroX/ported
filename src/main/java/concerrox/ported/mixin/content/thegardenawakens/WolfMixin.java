package concerrox.ported.mixin.content.thegardenawakens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import concerrox.ported.util.DyeColorUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Wolf.class)
public abstract class WolfMixin {

    @Shadow
    public abstract DyeColor getCollarColor();

    @WrapOperation(
            method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Wolf;setCollarColor(Lnet/minecraft/world/item/DyeColor;)V"))
    public void swapCollarColor(Wolf instance, DyeColor collarColor, Operation<Void> original,
                                @Local(argsOnly = true) ServerLevel level,
                                @Local(argsOnly = true) AgeableMob otherParent) {
        original.call(instance, DyeColorUtils.INSTANCE.getMixedColor(level, getCollarColor(), ((Wolf) otherParent).getCollarColor()));
    }

}
