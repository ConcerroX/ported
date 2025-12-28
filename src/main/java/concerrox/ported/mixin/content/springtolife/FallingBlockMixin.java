package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.drygrass.AmbientDesertBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block {

    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (this == Blocks.SAND || this == Blocks.RED_SAND) {
            AmbientDesertBlockSoundsPlayer.INSTANCE.playAmbientSandSounds(level, pos, random);
        }
    }

}
