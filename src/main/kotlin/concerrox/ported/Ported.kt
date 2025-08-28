package concerrox.ported

import com.mojang.logging.LogUtils
import concerrox.ported.registry.ModBlockEntityTypes
import concerrox.ported.registry.ModBlocks
import concerrox.ported.registry.ModCreativeModeTabs
import concerrox.ported.registry.ModItems
import concerrox.ported.registry.ModPacketTypes
import concerrox.ported.registry.ModRecipeSerializers
import concerrox.ported.registry.ModSoundEvents
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig

internal fun res(path: String) = ResourceLocation.fromNamespaceAndPath(Ported.MOD_ID, path)

@Mod(Ported.MOD_ID)
class Ported(modEventBus: IEventBus, modContainer: ModContainer) {

    init {
        ModItems.ITEMS.register(modEventBus)
        ModBlocks.BLOCKS.register(modEventBus)
//        ModSoundEvents.SOUND_EVENTS.register(modEventBus)
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus)
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus)
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus)
        modEventBus.addListener(ModPacketTypes::register)
        modContainer.registerConfig(ModConfig.Type.COMMON, PortedConfig.SPEC)
    }

    companion object {
        const val MOD_ID = "ported"
        private val LOGGER = LogUtils.getLogger()
    }

}
