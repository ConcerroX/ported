package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.registry.ModAttachmentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {

    @WrapOperation(method = "spawnOffspringFromSpawnEgg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/AgeableMob;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"))
    private AgeableMob onSpawnOffspringFromSpawnEgg(AgeableMob instance, ServerLevel level, AgeableMob ageableMob,
                                                    Operation<AgeableMob> original) {
        var result = original.call(instance, level, ageableMob);
        if (instance.hasData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT()))
            result.setData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT(), instance.getData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT()));
        return result;
    }

}
