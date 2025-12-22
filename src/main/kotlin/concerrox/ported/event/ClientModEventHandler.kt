package concerrox.ported.event

import concerrox.ported.Ported
import concerrox.ported.content.thegardenawakens.creaking.CreakingModel
import concerrox.ported.content.thegardenawakens.creaking.CreakingRenderer
import concerrox.ported.content.thegardenawakens.creaking.particle.TerrainParticleCrumblingProvider
import concerrox.ported.content.thegardenawakens.creaking.particle.TrailParticle
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakBoatRenderer
import concerrox.ported.data.ModItemTagsProvider
import concerrox.ported.registry.*
import net.minecraft.client.Minecraft
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.renderer.blockentity.HangingSignRenderer
import net.minecraft.client.renderer.blockentity.SignRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent


@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Ported.MOD_ID, value = [Dist.CLIENT])
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

    @SubscribeEvent
    fun onRegisterEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntityTypes.CREAKING.get(), ::CreakingRenderer)
        event.registerEntityRenderer(ModEntityTypes.PALE_OAK_BOAT.get()) { PaleOakBoatRenderer(it, false) }
        event.registerEntityRenderer(ModEntityTypes.PALE_OAK_CHEST_BOAT.get()) { PaleOakBoatRenderer(it, true) }

        event.registerBlockEntityRenderer(ModBlockEntityTypes.SIGN.get(), ::SignRenderer)
        event.registerBlockEntityRenderer(ModBlockEntityTypes.HANGING_SIGN.get(), ::HangingSignRenderer)
    }

    @SubscribeEvent
    fun onRegisterEntityLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(ModModelLayers.CREAKING, CreakingModel::createBodyLayer)
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel)
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel)
    }

    @SubscribeEvent
    fun onRegisterParticleProviders(event: RegisterParticleProvidersEvent) {
        event.registerSpriteSet(ModParticleTypes.TRAIL.get(), TrailParticle::Provider)
        event.registerSpecial(
            ModParticleTypes.BLOCK_CRUMBLE.get(), TerrainParticleCrumblingProvider::createTerrainParticle
        )
    }

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