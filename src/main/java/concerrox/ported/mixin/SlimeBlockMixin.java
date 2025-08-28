package concerrox.ported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

    @WrapOperation(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/HalfTransparentBlock;fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V"))
    private void fallOn(SlimeBlock instance, Level level, BlockState blockState, BlockPos blockPos, Entity entity,
                              float v, Operation<Void> original) {
        // pass
    }

}
