package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.registry.ModBlocks;
import concerrox.ported.registry.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {

    @Unique
    private static Map<Block, Float> LEAF_PARTICLE_CHANCES;
    @Unique
    private static boolean ported$isLeafParticleChancesCached = false;

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private void ported$makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random,
                                                   BlockState blockBelow, BlockPos belowPos) {
        if (!ported$isLeafParticleChancesCached) {
            LEAF_PARTICLE_CHANCES = Map.of(Blocks.OAK_LEAVES, 0.01f, Blocks.SPRUCE_LEAVES, 0.01f, Blocks.BIRCH_LEAVES, 0.01f, Blocks.JUNGLE_LEAVES, 0.01f, Blocks.ACACIA_LEAVES, 0.01f, Blocks.DARK_OAK_LEAVES, 0.01f, ModBlocks.INSTANCE.getPALE_OAK_LEAVES()
                    .get(), 0.02f, Blocks.MANGROVE_LEAVES, 0.01f, Blocks.AZALEA_LEAVES, 0.01f, Blocks.FLOWERING_AZALEA_LEAVES, 0.01f);
            ported$isLeafParticleChancesCached = true;
        }
        var leafParticleChance = LEAF_PARTICLE_CHANCES.get(this);
        if (!(random.nextFloat() >= leafParticleChance) && !isFaceFull(blockBelow.getCollisionShape(level, belowPos), Direction.UP)) {
            ported$spawnFallingLeavesParticle(level, pos, random);
        }
    }

    @Unique
    protected void ported$spawnFallingLeavesParticle(Level level, BlockPos p_400280_, RandomSource p_400310_) {
        if ((Block) this == ModBlocks.INSTANCE.getPALE_OAK_LEAVES().get()) {
            ParticleUtils.spawnParticleBelow(level, p_400280_, p_400310_, ModParticleTypes.INSTANCE.getPALE_OAK_LEAVES()
                    .get());
        } else {
            var particleOption = ColorParticleOption.create(ModParticleTypes.INSTANCE.getTINTED_LEAVES()
                    .get(), ported$getClientLeafTintColor(level, p_400280_));
            ParticleUtils.spawnParticleBelow(level, p_400280_, p_400310_, particleOption);
        }
    }

    @Unique
    public int ported$getClientLeafTintColor(Level level, BlockPos p_400248_) {
        if (level instanceof ServerLevel) return 0;
        return Minecraft.getInstance().getBlockColors().getColor(level.getBlockState(p_400248_), level, p_400248_, 0);
    }

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        ported$makeFallingLeavesParticles(level, pos, random, blockstate, blockpos);
    }

}
