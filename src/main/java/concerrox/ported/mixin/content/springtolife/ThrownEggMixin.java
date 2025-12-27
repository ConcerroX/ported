package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import concerrox.ported.content.springtolife.mobvariant.MobVariantManager;
import concerrox.ported.registry.ModAttachmentTypes;
import concerrox.ported.registry.ModBiomeTags;
import concerrox.ported.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectile {

    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "onHit", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"))
    private Entity onSpawnOffspringFromSpawnEgg(EntityType<? extends Entity> instance, Level level,
                                                Operation<Entity> original) {
        var ret = original.call(instance, level);
        if (getItem().is(ModItems.INSTANCE.getBROWN_EGG())) {
            ret.setData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT(), MobVariantManager.INSTANCE.randomVariantByBiomeTag(instance, ModBiomeTags.INSTANCE.getSPAWNS_WARM_VARIANT_FARM_ANIMALS()));
        } else if (getItem().is(ModItems.INSTANCE.getBLUE_EGG())) {
            ret.setData(ModAttachmentTypes.INSTANCE.getMOB_VARIANT(), MobVariantManager.INSTANCE.randomVariantByBiomeTag(instance, ModBiomeTags.INSTANCE.getSPAWNS_COLD_VARIANT_FARM_ANIMALS()));
        }
        return ret;
    }

}
