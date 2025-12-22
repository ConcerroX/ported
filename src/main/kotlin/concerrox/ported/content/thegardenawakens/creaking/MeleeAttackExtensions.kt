package concerrox.ported.content.thegardenawakens.creaking

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.behavior.EntityTracker
import net.minecraft.world.entity.ai.behavior.OneShot
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder
import net.minecraft.world.entity.ai.behavior.declarative.Trigger
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.item.ProjectileWeaponItem
import java.util.function.Predicate

object MeleeAttackExtensions {

    internal fun <T : Mob> create(canAttack: Predicate<T>, attackCooldown: Int): OneShot<T> = BehaviorBuilder.create {
        it.group(
            it.registered(MemoryModuleType.LOOK_TARGET),
            it.present(MemoryModuleType.ATTACK_TARGET),
            it.absent(MemoryModuleType.ATTACK_COOLING_DOWN),
            it.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
        ).apply(it) { positionTrackerAccessor, entityAccessor, booleanAccessor, nearestAccessor ->
            Trigger { _, mob, _ ->
                val livingEntity = it.get(entityAccessor)
                if (canAttack.test(mob)
                    && !isHoldingUsableProjectileWeapon(mob)
                    && mob.isWithinMeleeAttackRange(livingEntity)
                    && it.get(nearestAccessor).contains(livingEntity)
                ) {
                    positionTrackerAccessor!!.set(EntityTracker(livingEntity, true))
                    mob.swing(InteractionHand.MAIN_HAND)
                    mob.doHurtTarget(livingEntity)
                    booleanAccessor.setWithExpiry(true, attackCooldown.toLong())
                    true
                } else false
            }
        }
    }

    private fun isHoldingUsableProjectileWeapon(mob: Mob) = mob.isHolding { stack ->
        val item = stack.item
        item is ProjectileWeaponItem && mob.canFireProjectileWeapon(item)
    }

}