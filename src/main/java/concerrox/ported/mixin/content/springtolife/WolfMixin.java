package concerrox.ported.mixin.content.springtolife;

import concerrox.ported.content.springtolife.wolf.WolfSoundVariant;
import concerrox.ported.content.springtolife.wolf.WolfSoundVariants;
import concerrox.ported.registry.ModAttachmentTypes;
import concerrox.ported.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal {

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private Holder<WolfSoundVariant> ported$getSoundVariant() {
        if (hasData(ModAttachmentTypes.INSTANCE.getWOLF_SOUND_VARIANT())) {
            return getData(ModAttachmentTypes.INSTANCE.getWOLF_SOUND_VARIANT());
        } else {
            return registryAccess().registry(ModRegistries.INSTANCE.getWOLF_SOUND_VARIANT())
                    .orElseThrow()
                    .getHolderOrThrow(WolfSoundVariants.INSTANCE.getCLASSIC());
        }
    }

    @Unique
    private void ported$setSoundVariant(Holder<WolfSoundVariant> soundVariant) {
        setData(ModAttachmentTypes.INSTANCE.getWOLF_SOUND_VARIANT(), soundVariant);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                 SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        ported$setSoundVariant(WolfSoundVariants.INSTANCE.pickRandomSoundVariant(registryAccess()));
    }

    /**
     * @author ConcerroX
     * @reason to use sound variants
     */
    @Overwrite
    protected SoundEvent getAmbientSound() {
        if (((Wolf) (Object) this).isAngry()) {
            return ported$getSoundVariant().value().growlSound().value();
        } else if (random.nextInt(3) != 0) {
            return ported$getSoundVariant().value().ambientSound().value();
        } else {
            return isTame() && getHealth() < 20f ? ported$getSoundVariant().value()
                    .whineSound()
                    .value() : ported$getSoundVariant().value().pantSound().value();
        }
    }

    @Inject(method = "getDeathSound", at = @At("TAIL"), cancellable = true)
    protected void getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue(ported$getSoundVariant().value().deathSound().value());
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
            at = @At("TAIL"))
    protected void getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Wolf> cir) {
        var offspring = cir.getReturnValue();
        if (offspring != null) {
            offspring.setData(ModAttachmentTypes.INSTANCE.getWOLF_SOUND_VARIANT(), WolfSoundVariants.INSTANCE.pickRandomSoundVariant(offspring.registryAccess()));
        }
    }

}
