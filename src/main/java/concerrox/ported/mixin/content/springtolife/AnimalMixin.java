package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.registry.ModAttachmentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Animal.class)
public class AnimalMixin {

    @WrapOperation(method = "spawnChildFromBreeding", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Animal;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"))
    private AgeableMob onSpawnOffspringFromSpawnEgg(Animal instance, ServerLevel level, AgeableMob ageableMob,
                                                    Operation<AgeableMob> original) {
        var result = original.call(instance, level, ageableMob);
        var variantAttachment = ModAttachmentTypes.INSTANCE.getMOB_VARIANT();
        if (instance.hasData(variantAttachment) || ageableMob.hasData(variantAttachment)) {
            var variantSource = level.random.nextBoolean() ? instance : ageableMob;
            if (variantSource.hasData(variantAttachment))
                result.setData(variantAttachment, variantSource.getData(variantAttachment));
        }
        return result;
    }


}
