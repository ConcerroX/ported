package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.registry.ModSoundTypes;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSetType.class)
public class BlockSetTypeMixin {

    @Inject(method = "soundType", at = @At("RETURN"), cancellable = true)
    private void soundType(CallbackInfoReturnable<SoundType> cir) {
        if ((Object) this == BlockSetType.IRON) {
            cir.setReturnValue(ModSoundTypes.INSTANCE.getIRON());
        }
    }

}
