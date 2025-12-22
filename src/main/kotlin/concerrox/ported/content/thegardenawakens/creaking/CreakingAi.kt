package concerrox.ported.content.thegardenawakens.creaking

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import concerrox.ported.registry.ModMemoryModuleTypes
import concerrox.ported.content.thegardenawakens.creaking.MeleeAttackExtensions
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.behavior.*
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.schedule.Activity

object CreakingAi {

    internal val SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS
    )

    internal val MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
        ModMemoryModuleTypes.NEAREST_VISIBLE_ATTACKABLE_PLAYERS.get(),
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.ATTACK_COOLING_DOWN
    )

    fun initCoreActivity(brain: Brain<Creaking>) {
        val swim = object : Swim(0.8f) {
            override fun checkExtraStartConditions(level: ServerLevel, owner: Mob) =
                owner is Creaking && owner.canMove() && super.checkExtraStartConditions(level, owner)
        }
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(swim, LookAtTargetSink(45, 90), MoveToTargetSink()))
    }

    @Suppress("DEPRECATION")
    fun initIdleActivity(brain: Brain<Creaking>) {
        val startAttacking = StartAttacking.create<Creaking>({ it.isActive }, {
            it.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
        })
        val looking = SetEntityLookTargetSometimes.create(8f, UniformInt.of(30, 60))
        val runOne = RunOne(
            ImmutableList.of(
                Pair.of(RandomStroll.stroll(0.3f), 2),
                Pair.of(SetWalkTargetFromLookTarget.create(0.3f, 3), 2),
                Pair.of(DoNothing(30, 60), 1)
            )
        )
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(startAttacking, looking, runOne))
    }

    fun initFightActivity(creaking: Creaking, brain: Brain<Creaking>) {
        val walking = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1f)
        val melee = MeleeAttackExtensions.create(Creaking::canMove, 40)
        val stoping = StopAttackingIfTargetInvalid.create { _: Mob, target ->
            !isAttackTargetStillReachable(creaking, target)
        }
        brain.addActivityWithConditions(
            Activity.FIGHT,
            10,
            ImmutableList.of(walking, melee, stoping),
            ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT))
        )
    }

    private fun <E : LivingEntity> Brain<E>.addActivityWithConditions(
        activity: Activity,
        priorityStart: Int,
        tasks: ImmutableList<out BehaviorControl<in E>>,
        memoryStatuses: MutableSet<Pair<MemoryModuleType<*>?, MemoryStatus>>
    ) {
        addActivityWithConditions(activity, createPriorityPairs(priorityStart, tasks), memoryStatuses)
    }

    private fun <E : LivingEntity> createPriorityPairs(
        priorityStart: Int, tasks: ImmutableList<out BehaviorControl<in E>>
    ): ImmutableList<out Pair<Int, out BehaviorControl<in E>>> {
        var i = priorityStart
        val builder = ImmutableList.builder<Pair<Int, out BehaviorControl<in E>>>()
        val iterator = tasks.iterator()
        while (iterator.hasNext()) {
            val control = iterator.next()
            builder.add(Pair.of(i++, control))
        }
        return builder.build()
    }

    private fun isAttackTargetStillReachable(creaking: Creaking, target: LivingEntity): Boolean {
        val optional = creaking.getBrain().getMemory(ModMemoryModuleTypes.NEAREST_VISIBLE_ATTACKABLE_PLAYERS.get())
        return optional.map { players ->
            target is Player && players.contains(target)
        }.orElse(false)
    }

    fun brainProvider(): Brain.Provider<Creaking> = Brain.provider(MEMORY_TYPES, SENSOR_TYPES)

    fun makeBrain(creaking: Creaking, brain: Brain<Creaking>): Brain<Creaking> {
        initCoreActivity(brain)
        initIdleActivity(brain)
        initFightActivity(creaking, brain)
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.useDefaultActivity()
        return brain
    }

    fun updateActivity(creaking: Creaking) {
        if (!creaking.canMove()) {
            creaking.getBrain().useDefaultActivity()
        } else {
            creaking.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
        }
    }

}