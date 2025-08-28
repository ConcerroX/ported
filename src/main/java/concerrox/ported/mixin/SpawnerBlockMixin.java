package concerrox.ported.mixin;

import concerrox.ported.registry.ModSoundTypes;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnerBlock.class)
public class SpawnerBlockMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/BaseEntityBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V"))
    private static BlockBehaviour.Properties constructor(BlockBehaviour.Properties properties) {
        return properties.sound(ModSoundTypes.INSTANCE.getSPAWNER());
    }

}
