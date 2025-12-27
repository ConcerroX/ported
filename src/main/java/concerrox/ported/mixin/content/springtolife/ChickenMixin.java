package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.registry.ModAttachmentTypes;
import concerrox.ported.registry.ModItems;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Chicken.class)
public class ChickenMixin {

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity onSpawnOffspringFromSpawnEgg(Chicken instance, ItemLike itemLike,
                                                    Operation<ItemEntity> original) {
        var variantAttachment = ModAttachmentTypes.INSTANCE.getMOB_VARIANT();
        if (instance.hasData(variantAttachment)) {
            if (instance.getData(variantAttachment).isCold()) {
                itemLike = ModItems.INSTANCE.getBLUE_EGG();
            } else if (instance.getData(variantAttachment).isWarm()) {
                itemLike = ModItems.INSTANCE.getBROWN_EGG();
            }
        }
        return original.call(instance, itemLike);
    }


}
