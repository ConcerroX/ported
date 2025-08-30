package concerrox.ported.mixin;

import concerrox.ported.entity.SalmonVariant;
import concerrox.ported.mixininterface.SalmonExtensions;
import concerrox.ported.registry.ModAttachmentTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Salmon.class)
public class SalmonMixin implements SalmonExtensions {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<Salmon> entityType, Level level, CallbackInfo ci) {
        ((Salmon) (Object) this).refreshDimensions();
    }

    @Unique
    @Override
    public void ported$setVariant(@NotNull SalmonVariant variant) {
        ((Salmon) (Object) this).setData(ModAttachmentTypes.INSTANCE.getSALMON_SIZE(), variant);
        ((Salmon) (Object) this).refreshDimensions();
    }

    @Unique
    @Override
    public @NotNull SalmonVariant ported$getVariant() {
        return ((Salmon) (Object) this).getData(ModAttachmentTypes.INSTANCE.getSALMON_SIZE());
    }

}
