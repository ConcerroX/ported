//package concerrox.ported.mixin.content.thegardenawakens;
//
//import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import net.minecraft.core.Holder;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.level.block.FlowerBlock;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import org.spongepowered.asm.mixin.Mixin;
//
//@Mixin(FlowerBlock.class)
//public class FlowerBlockMixin {
//
//    @WrapMethod(
//            method = "<init>(Lnet/minecraft/core/Holder;FLnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V")
//    private void init(Holder<MobEffect> effect, float seconds, BlockBehaviour.Properties properties,
//                      Operation<Void> original) {
//        return original.call(effect, seconds, properties);
//    }
//
//}
