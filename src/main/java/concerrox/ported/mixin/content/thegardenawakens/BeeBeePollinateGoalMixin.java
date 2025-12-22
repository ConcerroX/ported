package concerrox.ported.mixin.content.thegardenawakens;

import concerrox.ported.registry.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public class BeeBeePollinateGoalMixin {

    @Inject(method = "lambda$new$0", at = @At("HEAD"), cancellable = true)
    private static void validPollinationBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(ModBlocks.INSTANCE.getCLOSED_EYEBLOSSOM())) cir.setReturnValue(false);
    }

}
