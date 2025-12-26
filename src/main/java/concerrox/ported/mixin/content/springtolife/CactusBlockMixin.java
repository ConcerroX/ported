package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin extends Block {

    @Shadow
    @Final
    public static IntegerProperty AGE;

    @Unique
    private static final int ATTEMPT_GROW_CACTUS_FLOWER_AGE = 8;
    @Unique
    private static final double ATTEMPT_GROW_CACTUS_FLOWER_SMALL_CACTUS_CHANCE = 0.1;
    @Unique
    private static final double ATTEMPT_GROW_CACTUS_FLOWER_TALL_CACTUS_CHANCE = 0.25F;

    public CactusBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author ConcerroX
     * @reason to add cactus flowers
     */
    @SuppressWarnings("DataFlowIssue")
    @Overwrite
    protected void randomTick(@NotNull BlockState state, ServerLevel level, BlockPos pos,
                              @NotNull RandomSource random) {
        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)) {
            int y = 1;
            int age = state.getValue(AGE);
            if (CommonHooks.canCropGrow(level, above, state, true)) {
                while (level.getBlockState(pos.below(y)).is((CactusBlock) (Object) this)) {
                    ++y;
                    if (y == 3 && age == 15) return;
                }
                if (age == ATTEMPT_GROW_CACTUS_FLOWER_AGE && canSurvive(defaultBlockState(), level, pos.above())) {
                    double prob = y >= 3 ? ATTEMPT_GROW_CACTUS_FLOWER_TALL_CACTUS_CHANCE : ATTEMPT_GROW_CACTUS_FLOWER_SMALL_CACTUS_CHANCE;
                    if (random.nextDouble() <= prob) {
                        level.setBlockAndUpdate(above, ModBlocks.INSTANCE.getCACTUS_FLOWER().get().defaultBlockState());
                    }
                } else if (age == 15 && y < 3) {
                    level.setBlockAndUpdate(above, defaultBlockState());
                    BlockState newState = state.setValue(AGE, 0);
                    level.setBlock(pos, newState, 260);
                    level.neighborChanged(newState, above, (CactusBlock) (Object) this, pos, false);
                }
                if (age < 15) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 260);
                }
                CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
    }

}
