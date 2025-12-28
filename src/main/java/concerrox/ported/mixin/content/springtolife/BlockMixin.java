package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.drygrass.AmbientDesertBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {

    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if ((Object) this == Blocks.DEAD_BUSH) {
            AmbientDesertBlockSoundsPlayer.INSTANCE.playAmbientDeadBushSounds(level, pos, random);
        }
    }

}
