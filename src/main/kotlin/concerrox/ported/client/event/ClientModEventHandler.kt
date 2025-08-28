package concerrox.ported.client.event

import concerrox.ported.Ported
import concerrox.ported.data.ModItemTagsProvider
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ModelEvent

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Ported.MOD_ID, value = [Dist.CLIENT])
object ClientModEventHandler {

    fun createBundleOpenBackModelResourceLocation(item: Item): ModelResourceLocation {
        val res = BuiltInRegistries.ITEM.getKey(item)
        return ModelResourceLocation.standalone(ResourceLocation.withDefaultNamespace("item/" + res.path + "_open_back"))
    }

    fun createBundleOpenFrontModelResourceLocation(item: Item): ModelResourceLocation {
        val res = BuiltInRegistries.ITEM.getKey(item)
        return ModelResourceLocation.standalone(ResourceLocation.withDefaultNamespace("item/" + res.path + "_open_front"))
    }

    @SubscribeEvent
    fun onRegisterAdditionalModel(event: ModelEvent.RegisterAdditional) {
        ModItemTagsProvider.BUNDLES.forEach {
            event.register(createBundleOpenFrontModelResourceLocation(it))
            event.register(createBundleOpenBackModelResourceLocation(it))
        }
    }

}