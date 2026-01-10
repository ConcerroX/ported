//package concerrox.ported.content.chasetheskies.happyghast
//
//import com.google.common.collect.ImmutableList
//import com.mojang.datafixers.util.Pair
//import concerrox.ported.registry.ModMemoryModuleTypes
//import concerrox.ported.registry.ModSensorTypes
//import net.minecraft.util.valueproviders.UniformInt
//import net.minecraft.world.entity.ai.Brain
//import net.minecraft.world.entity.ai.behavior.*
//import net.minecraft.world.entity.ai.memory.MemoryModuleType
//import net.minecraft.world.entity.ai.memory.MemoryStatus
//import net.minecraft.world.entity.ai.sensing.Sensor
//import net.minecraft.world.entity.ai.sensing.SensorType
//import net.minecraft.world.entity.schedule.Activity
//
//object HappyGhastAi {
//
//    private const val SPEED_MULTIPLIER_WHEN_IDLING = 1f
//    private const val SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25f
//    private const val SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.1f
//    private const val BABY_GHAST_CLOSE_ENOUGH_DIST = 3.0
//    private val ADULT_FOLLOW_RANGE = UniformInt.of(3, 16)
//
//    private val SENSOR_TYPES = ImmutableList.of<SensorType<out Sensor<in HappyGhast>>>(
//        SensorType.NEAREST_LIVING_ENTITIES,
//        SensorType.HURT_BY,
//        ModSensorTypes.FOOD_TEMPTATIONS.get(),
//        ModSensorTypes.NEAREST_ADULT_ANY_TYPE.get(),
//        SensorType.NEAREST_PLAYERS
//    )
//    private val MEMORY_TYPES = ImmutableList.of(
//        MemoryModuleType.WALK_TARGET,
//        MemoryModuleType.LOOK_TARGET,
//        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
//        MemoryModuleType.PATH,
//        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
//        MemoryModuleType.TEMPTING_PLAYER,
//        MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
//        MemoryModuleType.IS_TEMPTED,
//        MemoryModuleType.BREED_TARGET,
//        MemoryModuleType.IS_PANICKING,
//        MemoryModuleType.HURT_BY,
//        MemoryModuleType.NEAREST_VISIBLE_ADULT,
//        MemoryModuleType.NEAREST_PLAYERS,
//        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
//        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
//        ModMemoryModuleTypes.NEAREST_VISIBLE_ATTACKABLE_PLAYERS.get()
//    )
//
//    fun brainProvider(): Brain.Provider<HappyGhast> {
//        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES)
//    }
//
//    internal fun makeBrain(brain: Brain<HappyGhast>): Brain<*> {
//        initCoreActivity(brain)
//        initIdleActivity(brain)
//        initPanicActivity(brain)
//        brain.setCoreActivities(setOf(Activity.CORE))
//        brain.setDefaultActivity(Activity.IDLE)
//        brain.useDefaultActivity()
//        return brain
//    }
//
//    private fun initCoreActivity(brain: Brain<HappyGhast>) {
//        brain.addActivity(
//            Activity.CORE, 0, ImmutableList.of(
//                Swim(0.8f),
//                AnimalPanic(2f),
//                LookAtTargetSink(45, 90),
//                MoveToTargetSink(),
//                CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
//            )
//        )
//    }
//
//    private fun initIdleActivity(brain: Brain<HappyGhast>) {
//        brain.addActivity(
//            Activity.IDLE, ImmutableList.of(
//                Pair.of(
//                    1, FollowTemptation(
//                        { _ -> SPEED_MULTIPLIER_WHEN_TEMPTED },
//                        { _ -> BABY_GHAST_CLOSE_ENOUGH_DIST },
//                    )
//                ), Pair.of(
//                    2, HappyGhastBabyFollowAdult.create(
//                        ADULT_FOLLOW_RANGE,
//                        { _ -> SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT },
//                        MemoryModuleType.NEAREST_VISIBLE_PLAYER
//                    )
//                ), Pair.of(
//                    3, HappyGhastBabyFollowAdult.create(
//                        ADULT_FOLLOW_RANGE,
//                        { _ -> SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT },
//                        MemoryModuleType.NEAREST_VISIBLE_ADULT
//                    )
//                ), Pair.of(
//                    4, RunOne(
//                        ImmutableList.of(
//                            Pair.of(RandomStroll.fly(SPEED_MULTIPLIER_WHEN_IDLING), 1),
//                            Pair.of(SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_WHEN_IDLING, 3), 1),
//                        )
//                    )
//                )
//            )
//        )
//    }
//
//    private fun initPanicActivity(brain: Brain<HappyGhast>) {
//        brain.addActivityWithConditions(
//            Activity.PANIC,
//            ImmutableList.of(),
//            mutableSetOf(Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_PRESENT))
//        )
//    }
//
//    fun updateActivity(happyGhast: HappyGhast) {
//        happyGhast.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE))
//    }
//
//}