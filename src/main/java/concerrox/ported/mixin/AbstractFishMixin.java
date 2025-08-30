package concerrox.ported.mixin;

import concerrox.ported.entity.SalmonVariant;
import concerrox.ported.mixininterface.SalmonExtensions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFish.class)
public abstract class AbstractFishMixin implements SalmonExtensions {

    @Inject(method = "saveToBucketTag", at = @At("TAIL"))
    private void saveToBucketTag(ItemStack stack, CallbackInfo ci) {
        if ((Object) this instanceof Salmon)
            CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> tag.putInt("BucketVariantTag", ported$getVariant().getId()));
    }

    @Inject(method = "loadFromBucketTag", at = @At("TAIL"))
    private void loadFromBucketTag(CompoundTag tag, CallbackInfo ci) {
        if ((Object) this instanceof Salmon) {
            if (tag.contains("BucketVariantTag", Tag.TAG_INT))
                ported$setVariant(SalmonVariant.BY_ID.apply(tag.getInt("BucketVariantTag")));
        }
    }

}
