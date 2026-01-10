package concerrox.ported.registry

import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object ModMemoryModuleTypes {

    internal val MEMORY_MODULE_TYPE =
        DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

    val NEAREST_VISIBLE_ATTACKABLE_PLAYERS = MEMORY_MODULE_TYPE.new("nearest_visible_targetable_players") {
        MemoryModuleType<List<Player>>(Optional.empty())
    }

}