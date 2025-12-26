package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.registry.ModBlockTags;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(EatBlockGoal.class)
public class EatBlockGoalMixin {

    @Redirect(method = "canUse",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
    private boolean ported$customTallGrassCanUse(Predicate<BlockState> predicate, Object stateObj) {
        BlockState state = (BlockState) stateObj;
        return this.ported$isEdible(state);
    }

    @Redirect(method = "tick",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
    private boolean ported$customTallGrassTick(Predicate<BlockState> predicate, Object stateObj) {
        BlockState state = (BlockState) stateObj;
        return this.ported$isEdible(state);
    }

    @Unique
    private boolean ported$isEdible(@Nullable BlockState state) {
        if (state == null) return false;
        return state.is(ModBlockTags.INSTANCE.getEDIBLE_FOR_SHEEP());
    }

}
