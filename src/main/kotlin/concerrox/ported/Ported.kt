package concerrox.ported

import com.mojang.logging.LogUtils
import concerrox.ported.registry.*
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
        ModDataComponents.DATA_COMPONENTS.register(modEventBus)
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus)
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus)
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus)
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus)

        modEventBus.addListener(ModPacketTypes::register)
        modContainer.registerConfig(ModConfig.Type.COMMON, PortedConfig.SPEC)
    }

    companion object {
        const val MOD_ID = "ported"
        internal val LOGGER = LogUtils.getLogger()
    }

}
