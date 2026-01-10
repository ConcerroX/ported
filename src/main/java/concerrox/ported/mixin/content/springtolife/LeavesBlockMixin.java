package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.registry.ModBlocks;
import concerrox.ported.registry.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    private static final Map<ResourceLocation, Float> LEAF_PARTICLE_CHANCES = Map.of(
            ResourceLocation.withDefaultNamespace("oak_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("spruce_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("birch_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("jungle_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("acacia_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("dark_oak_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("pale_oak_leaves"), 0.02f,
            ResourceLocation.withDefaultNamespace("mangrove_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("azalea_leaves"), 0.01f,
            ResourceLocation.withDefaultNamespace("flowering_azalea_leaves"), 0.01f
    );

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private void ported$makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random,
                                                   BlockState blockBelow, BlockPos belowPos) {
        var leafParticleChance = LEAF_PARTICLE_CHANCES.getOrDefault(BuiltInRegistries.BLOCK.getKey(this), 0f);
        if (!(random.nextFloat() >= leafParticleChance) && !isFaceFull(blockBelow.getCollisionShape(level, belowPos), Direction.UP)) {
            ported$spawnFallingLeavesParticle(level, pos, random);
        }
    }

    @Unique
    protected void ported$spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        if ((Block) this == ModBlocks.INSTANCE.getPALE_OAK_LEAVES().get()) {
            ParticleUtils.spawnParticleBelow(level, pos, random, ModParticleTypes.INSTANCE.getPALE_OAK_LEAVES().get());
        } else {
            var option = ColorParticleOption.create(
                    ModParticleTypes.INSTANCE.getTINTED_LEAVES().get(),
                    ported$getClientLeafTintColor(level, pos)
            );
            ParticleUtils.spawnParticleBelow(level, pos, random, option);
        }
    }

    @Unique
    public int ported$getClientLeafTintColor(Level level, BlockPos pos) {
        if (level instanceof ServerLevel) return 0;
        return Minecraft.getInstance().getBlockColors().getColor(level.getBlockState(pos), level, pos, 0);
    }

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockPos below = pos.below();
        BlockState blockstate = level.getBlockState(below);
        ported$makeFallingLeavesParticles(level, pos, random, blockstate, below);
    }

}
