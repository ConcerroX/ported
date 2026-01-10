package concerrox.ported.registry

import concerrox.ported.id
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

object ModLootTables {

//    val WOODLAND_MANSION_MODIFIER = createPorted("chests/woodland_mansion_modifier")

    private fun create(path: String): ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace(path))

    private fun createPorted(path: String): ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, id(path))

}