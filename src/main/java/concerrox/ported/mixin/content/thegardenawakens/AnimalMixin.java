package concerrox.ported.mixin.content.thegardenawakens;

import concerrox.ported.content.thegardenawakens.eyeblossom.EyeblossomBlock;
import concerrox.ported.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin {

    @Shadow
    protected abstract void usePlayerItem(Player player, InteractionHand hand, ItemStack stack);

    @Inject(method = "mobInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;getAge()I"),
            cancellable = true)
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if ((Object) this instanceof Bee bee) {
            var stack = player.getItemInHand(hand);
            if (stack.is(ModItems.INSTANCE.getOPEN_EYEBLOSSOM())) {
                usePlayerItem(player, hand, stack);
                if (!bee.level().isClientSide) bee.addEffect(EyeblossomBlock.Companion.getBeeInteractionEffect());
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else if (stack.is(ModItems.INSTANCE.getCLOSED_EYEBLOSSOM())) {
                cir.setReturnValue(InteractionResult.CONSUME);
            } else if (stack.is(Items.WITHER_ROSE)) {
                usePlayerItem(player, hand, stack);
                if (!bee.level().isClientSide) bee.addEffect(new MobEffectInstance(MobEffects.WITHER, 40));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

}
