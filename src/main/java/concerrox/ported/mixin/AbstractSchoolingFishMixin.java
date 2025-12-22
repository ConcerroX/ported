package concerrox.ported.mixin;

import concerrox.ported.content.bundlesofbravery.salmon.SalmonVariant;
import concerrox.ported.mixininterface.SalmonExtensions;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSchoolingFish.class)
public abstract class AbstractSchoolingFishMixin implements SalmonExtensions {

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                               SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if ((Object) this instanceof Salmon salmon) {
            var builder = SimpleWeightedRandomList.<SalmonVariant>builder();
            builder.add(SalmonVariant.SMALL, 30);
            builder.add(SalmonVariant.MEDIUM, 50);
            builder.add(SalmonVariant.LARGE, 15);
            builder.build()
                    .getRandom(salmon.getRandom())
                    .ifPresent(it -> ((SalmonExtensions) salmon).ported$setVariant(it.data()));
        }
    }

}
