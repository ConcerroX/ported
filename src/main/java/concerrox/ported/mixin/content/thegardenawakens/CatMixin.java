package concerrox.ported.mixin.content.thegardenawakens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import concerrox.ported.util.DyeColorUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Cat.class)
public abstract class CatMixin {

    @Shadow
    public abstract DyeColor getCollarColor();

    @WrapOperation(
            method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Cat;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Cat;setCollarColor(Lnet/minecraft/world/item/DyeColor;)V"))
    public void swapCollarColor(Cat instance, DyeColor color, Operation<Void> original,
                                @Local(argsOnly = true) ServerLevel level,
                                @Local(argsOnly = true) AgeableMob otherParent) {
        original.call(instance, DyeColorUtils.INSTANCE.getMixedColor(level, getCollarColor(), ((Cat) otherParent).getCollarColor()));
    }

}
