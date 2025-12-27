package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.mobvariant.MobVariantManager;
import concerrox.ported.registry.ModAttachmentTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                 SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        var variant = MobVariantManager.INSTANCE.onFinalizeSpawn$ported_neoforge_1_21_1(this, level);
        if (variant != null) setData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT(), variant);
    }

}
