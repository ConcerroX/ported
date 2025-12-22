package concerrox.ported.mixin.content.thegardenawakens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import kotlin.Pair;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin {

    @Unique
    private static final Map<Holder<MobEffect>, Pair<Float, Float>> EFFECT_REPLACEMENTS = Map.of(MobEffects.FIRE_RESISTANCE, new Pair<>(4f, 3f), MobEffects.BLINDNESS, new Pair<>(8f, 11f), MobEffects.WEAKNESS, new Pair<>(9f, 7f), MobEffects.REGENERATION, new Pair<>(8f, 7f), MobEffects.JUMP, new Pair<>(6f, 5f), MobEffects.WITHER, new Pair<>(8f, 7f), MobEffects.POISON, new Pair<>(12f, 11f));

    @WrapOperation(
            method = "<init>(Lnet/minecraft/core/Holder;FLnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FlowerBlock;makeEffectList(Lnet/minecraft/core/Holder;F)Lnet/minecraft/world/item/component/SuspiciousStewEffects;"))
    private static SuspiciousStewEffects init(Holder<MobEffect> effect, float seconds,
                                              Operation<SuspiciousStewEffects> original) {
        var replacement = EFFECT_REPLACEMENTS.get(effect);
        if (replacement != null && replacement.getFirst() == seconds) {
            return original.call(effect, replacement.getSecond());
        } else {
            return original.call(effect, seconds);
        }
    }

}
