//package concerrox.ported.content.chasetheskies.happyghast
//
//import net.minecraft.util.valueproviders.UniformInt
//import net.minecraft.world.entity.LivingEntity
//import net.minecraft.world.entity.ai.behavior.EntityTracker
//import net.minecraft.world.entity.ai.behavior.OneShot
//import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder
//import net.minecraft.world.entity.ai.behavior.declarative.Trigger
//import net.minecraft.world.entity.ai.memory.MemoryModuleType
//import net.minecraft.world.entity.ai.memory.WalkTarget
//import java.util.function.Function
//
//object HappyGhastBabyFollowAdult {
//
//    fun create(followRange: UniformInt, speedModifier: Float): OneShot<LivingEntity> {
//        return create(
//            followRange, { _ -> speedModifier }, MemoryModuleType.NEAREST_VISIBLE_ADULT
//        )
//    }
//
//    fun create(
//        followRange: UniformInt,
//        speedModifier: Function<LivingEntity, Float>,
//        nearestVisibleAdult: MemoryModuleType<out LivingEntity>
//    ): OneShot<LivingEntity> {
//        return BehaviorBuilder.create { builder ->
//            builder.group(
//                builder.present(nearestVisibleAdult),
//                builder.registered(MemoryModuleType.LOOK_TARGET),
//                builder.absent(MemoryModuleType.WALK_TARGET)
//            ).apply(builder) { entityAccessor, posAccessor, targetAccessor ->
//                Trigger { _, entity, _: Long ->
//                    if (!entity.isBaby) return@Trigger false
//                    val leader = builder.get(entityAccessor) as LivingEntity
//                    if (entity.closerThan(leader, followRange.maxValue + 1.0) && !entity.closerThan(
//                            leader, followRange.minValue.toDouble()
//                        )
//                    ) {
//                        val walkTarget = WalkTarget(
//                            EntityTracker(leader, false), speedModifier.apply(entity), followRange.minValue - 1
//                        )
//                        posAccessor.set(EntityTracker(leader, true))
//                        targetAccessor.set(walkTarget)
//                        return@Trigger true
//                    } else {
//                        return@Trigger false
//                    }
//                }
//            }
//        }
//    }
//}