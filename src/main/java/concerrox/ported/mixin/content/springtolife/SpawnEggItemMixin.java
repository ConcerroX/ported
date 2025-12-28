package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.registry.ModAttachmentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin extends Item {

    @Shadow
    @Final
    private EntityType<?> defaultType;

    public SpawnEggItemMixin(Properties properties) {
        super(properties);
    }

    @WrapOperation(method = "spawnOffspringFromSpawnEgg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/AgeableMob;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"))
    private AgeableMob onSpawnOffspringFromSpawnEgg(AgeableMob instance, ServerLevel level, AgeableMob ageableMob,
                                                    Operation<AgeableMob> original) {
        var result = original.call(instance, level, ageableMob);
        if (instance.hasData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT()))
            result.setData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT(), instance.getData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT()));
        return result;
    }

    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void getColor(int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (BuiltInRegistries.ITEM.getKey(this).getNamespace() == ResourceLocation.DEFAULT_NAMESPACE)
            cir.setReturnValue(0x00FFFFFF);
    }

}
