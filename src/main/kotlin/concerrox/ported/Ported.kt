package concerrox.ported

import com.mojang.logging.LogUtils
import concerrox.ported.content.springtolife.mobvariant.VanillaMobVariants
import concerrox.ported.content.thegardenawakens.palegarden.PaleGardenCreator
import concerrox.ported.registry.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

internal fun res(path: String) = ResourceLocation.fromNamespaceAndPath(Ported.MOD_ID, path)

@Mod(Ported.MOD_ID)
class Ported(modEventBus: IEventBus, modContainer: ModContainer) {

    init {
        ModWoodTypes.register()
        ModBlockSetTypes.register()

        ModItems.ITEMS.register(modEventBus)
        ModItems.PORTED_ITEMS.register(modEventBus)
        ModBlocks.BLOCKS.register(modEventBus)
        ModBlocks.PORTED_BLOCKS.register(modEventBus)
        ModFeatures.FEATURES.register(modEventBus)
        ModEntityTypes.ENTITY_TYPES.register(modEventBus)
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus)
        ModDataComponents.DATA_COMPONENTS_PORTED.register(modEventBus)
        ModAttachmentTypes.ATTACHMENT_TYPES_PORTED.register(modEventBus)
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus)
        ModBlockEntityTypes.PORTED_BLOCK_ENTITY_TYPES.register(modEventBus) // Signs
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus)
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus)
        ModMemoryModuleTypes.MEMORY_MODULE_TYPE.register(modEventBus)
        ModTreeDecoratorTypes.TREE_DECORATOR_TYPES.register(modEventBus)

        modEventBus.addListener(ModPacketTypes::register)
        modContainer.registerConfig(ModConfig.Type.COMMON, PortedConfig.SPEC)

        modEventBus.addListener { _: FMLCommonSetupEvent ->
            val potBlock = Blocks.FLOWER_POT as FlowerPotBlock
            potBlock.addPlant(ModBlocks.PALE_OAK_SAPLING.id, ModBlocks.POTTED_PALE_OAK_SAPLING)
            potBlock.addPlant(ModBlocks.CLOSED_EYEBLOSSOM.id, ModBlocks.POTTED_CLOSED_EYEBLOSSOM)
            potBlock.addPlant(ModBlocks.OPEN_EYEBLOSSOM.id, ModBlocks.POTTED_OPEN_EYEBLOSSOM)

            VanillaMobVariants.register()
            DispenserBlock.registerProjectileBehavior(ModItems.BLUE_EGG)
            DispenserBlock.registerProjectileBehavior(ModItems.BROWN_EGG)

            PaleGardenCreator.isBiomeRegistered = true
        }

    }

    companion object {
        const val MOD_ID = "ported"
        const val PORTED_TARGET_VERSION = "1.21.5"
        internal val LOGGER = LogUtils.getLogger()
    }

}
