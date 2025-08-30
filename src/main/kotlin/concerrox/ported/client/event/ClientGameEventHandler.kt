package concerrox.ported.client.event

import concerrox.ported.Ported
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.TitleScreen
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModList
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ScreenEvent

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = Ported.MOD_ID, value = [Dist.CLIENT])
object ClientGameEventHandler {

    @SubscribeEvent
    fun onRenderScreen(event: ScreenEvent.Render.Post) {
        val screen = event.screen
        val font = Minecraft.getInstance().font
        if (screen is TitleScreen) {
//            event.guiGraphics.drawString(
//                font,
//                "Ported " + ModList.get().getModFileById(Ported.MOD_ID).versionString() + ", based on 1.21.2",
//                2,
//                screen.height - (10 + 3 * (font.lineHeight + 1)),
//                0xFFFFFF
//            )
        }
    }

}