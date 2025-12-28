package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.sheep.SheepColorSpawnRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {

    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract void setColor(DyeColor dyeColor);

    @Unique
    private static DyeColor ported$getRandomSheepColor(ServerLevelAccessor level, BlockPos pos) {
        Holder<Biome> holder = level.getBiome(pos);
        return SheepColorSpawnRules.INSTANCE.getSheepColor(holder, level.getRandom());
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                               SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        setColor(ported$getRandomSheepColor(level, blockPosition()));
    }

}
