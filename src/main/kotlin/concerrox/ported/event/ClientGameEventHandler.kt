package concerrox.ported.event

import concerrox.ported.registry.ModBiomes
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ClientTickEvent

@OnlyIn(Dist.CLIENT)
object ClientGameEventHandler {

    private val minecraft = Minecraft.getInstance()

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent.Post) {
        val player = minecraft.player ?: return
        val level = player.level()
        val biome = level.getBiome(player.blockPosition()).value()
        if (biome == level.registryAccess().registryOrThrow(Registries.BIOME).get(ModBiomes.PALE_GARDEN)) {
            minecraft.musicManager.stopPlaying()
        }
    }

}