package concerrox.ported.event

import concerrox.ported.Ported
import concerrox.ported.content.springtolife.firefly.FireflyParticle
import concerrox.ported.content.springtolife.leaflitter.DryFoliageColors
import concerrox.ported.content.springtolife.mobvariant.model.ColdChickenModel
import concerrox.ported.content.springtolife.mobvariant.model.ColdCowModel
import concerrox.ported.content.springtolife.mobvariant.model.ColdPigModel
import concerrox.ported.content.springtolife.mobvariant.model.CowModel
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
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.blockentity.HangingSignRenderer
import net.minecraft.client.renderer.blockentity.SignRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ColorResolver
import net.minecraft.world.level.GrassColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.*


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

        event.registerLayerDefinition(ModModelLayers.COLD_CHICKEN, ColdChickenModel::createBodyLayer)
        event.registerLayerDefinition(ModModelLayers.COLD_PIG, ColdPigModel::createBodyLayer)
        event.registerLayerDefinition(ModModelLayers.COW_NEW) { CowModel.createBodyLayer() }
        event.registerLayerDefinition(ModModelLayers.COLD_COW, ColdCowModel::createBodyLayer)
        event.registerLayerDefinition(ModModelLayers.MOOSHROOM_NEW) { CowModel.createBodyLayer() }
    }

    val DRY_FOLIAGE_COLOR_RESOLVER = ColorResolver { biome, _, _ -> DryFoliageColors.getDryFoliageColor(biome) }

    @SubscribeEvent
    fun onRegisterColorResolverColorHandlers(event: RegisterColorHandlersEvent.ColorResolvers) {
        event.register(DRY_FOLIAGE_COLOR_RESOLVER)
    }

    @SubscribeEvent
    fun onRegisterBlockColorHandlers(event: RegisterColorHandlersEvent.Block) {
        event.register(
            { _, level, pos, _ ->
                if (level != null && pos != null) {
                    level.getBlockTint(pos, DRY_FOLIAGE_COLOR_RESOLVER)
                } else {
                    -10732494
                }
            }, ModBlocks.LEAF_LITTER.get()
        )

        event.register(
            { _, level, pos, p276244 ->
                if (p276244 == 0) {
                    -1
                } else if (level != null && pos != null) {
                    BiomeColors.getAverageGrassColor(level, pos)
                } else {
                    GrassColor.getDefaultColor()
                }
            }, ModBlocks.WILDFLOWERS.get()
        )

        event.register(
            { _, level, pos, _ ->
                if (level != null && pos != null) {
                    BiomeColors.getAverageGrassColor(level, pos)
                } else {
                    GrassColor.getDefaultColor()
                }
            }, ModBlocks.BUSH.get()
        )

    }

    @SubscribeEvent
    fun onRegisterItemColorHandlers(event: RegisterColorHandlersEvent.Item) {
        event.register({ _, _ -> GrassColor.getDefaultColor() }, ModBlocks.BUSH.get())
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

    @SubscribeEvent
    fun onRegisterParticleProviders(event: RegisterParticleProvidersEvent) {
        event.registerSpriteSet(ModParticleTypes.TRAIL.get(), TrailParticle::Provider)
        event.registerSpriteSet(ModParticleTypes.FIREFLY.get(), FireflyParticle::FireflyProvider)
        event.registerSpecial(
            ModParticleTypes.BLOCK_CRUMBLE.get(), TerrainParticleCrumblingProvider::createTerrainParticle
        )
    }

}