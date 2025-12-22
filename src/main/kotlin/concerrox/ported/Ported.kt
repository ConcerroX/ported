package concerrox.ported

import com.mojang.datafixers.util.Pair
import com.mojang.logging.LogUtils
import concerrox.ported.registry.*
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import terrablender.api.Region
import terrablender.api.RegionType
import terrablender.api.Regions
import java.util.function.Consumer

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
        ModEntityTypes.ENTITY_TYPES.register(modEventBus)
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus)
        ModDataComponents.DATA_COMPONENTS.register(modEventBus)
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus)
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus)
        ModBlockEntityTypes.PORTED_BLOCK_ENTITY_TYPES.register(modEventBus) // Signs
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus)
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus)
        ModMemoryModuleTypes.MEMORY_MODULE_TYPE.register(modEventBus)
        ModTreeDecoratorTypes.TREE_DECORATOR_TYPES.register(modEventBus)

        modEventBus.addListener(ModPacketTypes::register)
        modContainer.registerConfig(ModConfig.Type.COMMON, PortedConfig.SPEC)

        val potBlock = Blocks.FLOWER_POT as FlowerPotBlock
        potBlock.addPlant(ModBlocks.PALE_OAK_SAPLING.id, ModBlocks.POTTED_PALE_OAK_SAPLING)
        potBlock.addPlant(ModBlocks.CLOSED_EYEBLOSSOM.id, ModBlocks.POTTED_CLOSED_EYEBLOSSOM)
        potBlock.addPlant(ModBlocks.OPEN_EYEBLOSSOM.id, ModBlocks.POTTED_OPEN_EYEBLOSSOM)

        Regions.register(object : Region(res("pale_garden"), RegionType.OVERWORLD, 1) {
            override fun addBiomes(
                registry: Registry<Biome>, mapper: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>
            ) {
                addModifiedVanillaOverworldBiomes(mapper) { builder ->
                    builder.replaceBiome(Biomes.DARK_FOREST, ModBiomes.PALE_GARDEN)
//                    val frozenPeaksPoints =
//                        ParameterPointListBuilder().temperature(Temperature.NEUTRAL).humidity(Humidity.HUMID)
//                            .continentalness(
//                                Continentalness.span(Continentalness.COAST, Continentalness.FAR_INLAND),
//                                Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND)
//                            ).build()
//                    frozenPeaksPoints.forEach {
//                        builder.replaceBiome(it, TestBiomes.COLD_BLUE)
//                    }
                }
            }
        })

    }

    companion object {
        const val MOD_ID = "ported"
        internal val LOGGER = LogUtils.getLogger()
    }

}
