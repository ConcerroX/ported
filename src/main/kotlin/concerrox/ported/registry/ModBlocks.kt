package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.content.removedfeatures.netherreactorcore.NetherReactorCoreBlock
import concerrox.ported.content.springtolife.leaflitter.LeafLitterBlock
import concerrox.ported.content.springtolife.wildflowers.FlowerBedBlock
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlock
import concerrox.ported.content.thegardenawakens.eyeblossom.EyeblossomBlock
import concerrox.ported.content.thegardenawakens.eyeblossom.EyeblossomFlowerPotBlock
import concerrox.ported.content.thegardenawakens.palemoss.PaleHangingMossBlock
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossBlock
import concerrox.ported.content.thegardenawakens.palemoss.PaleMossCarpetBlock
import concerrox.ported.content.thegardenawakens.paleoak.*
import concerrox.ported.content.thegardenawakens.resin.ResinClumpBlock
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object ModBlocks {

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(ResourceLocation.DEFAULT_NAMESPACE)
    val PORTED_BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(Ported.MOD_ID)

    val CREAKING_HEART = new("creaking_heart") {
        CreakingHeartBlock(newProperties {
            mapColor(MapColor.COLOR_ORANGE)
            instrument(NoteBlockInstrument.BASEDRUM)
            strength(10f)
            sound(ModSoundTypes.CREAKING_HEART)
        })
    }

    val RESIN_CLUMP = new("resin_clump") {
        ResinClumpBlock(newProperties {
            mapColor(MapColor.TERRACOTTA_ORANGE)
            replaceable()
            noCollission()
            sound(ModSoundTypes.RESIN)
            ignitedByLava()
            pushReaction(PushReaction.DESTROY)
        })
    }

    val RESIN_BLOCK = block("resin_block", newProperties {
        mapColor(MapColor.TERRACOTTA_ORANGE)
        instrument(NoteBlockInstrument.BASEDRUM)
        sound(ModSoundTypes.RESIN)
    })

    fun resinBricksProperties(): BlockBehaviour.Properties = newProperties {
        mapColor(MapColor.TERRACOTTA_ORANGE)
        instrument(NoteBlockInstrument.BASEDRUM)
        requiresCorrectToolForDrops()
        sound(ModSoundTypes.RESIN_BRICKS)
        strength(1.5f, 6f)
    }

    val RESIN_BRICKS = block("resin_bricks", resinBricksProperties())
    val RESIN_BRICK_STAIRS = new("resin_brick_stairs") {
        StairBlock(RESIN_BRICKS.get().defaultBlockState(), resinBricksProperties())
    }
    val RESIN_BRICK_SLAB = new("resin_brick_slab") {
        SlabBlock(resinBricksProperties())
    }
    val RESIN_BRICK_WALL = new("resin_brick_wall") {
        WallBlock(resinBricksProperties())
    }
    val CHISELED_RESIN_BRICKS = block("chiseled_resin_bricks", resinBricksProperties())

    val PALE_OAK_LEAVES = new("pale_oak_leaves") {
        LeavesBlock(newProperties {
            mapColor(MapColor.METAL)
            strength(0.2f)
            randomTicks()
            sound(SoundType.GRASS)
            noOcclusion()
            isValidSpawn(Blocks::ocelotOrParrot)
            isSuffocating { _, _, _ -> false }
            isViewBlocking { _, _, _ -> false }
            ignitedByLava()
            pushReaction(PushReaction.DESTROY)
            isRedstoneConductor { _, _, _ -> false }
        }) //TODO:particle
    }
    val PALE_OAK_SAPLING = new("pale_oak_sapling") {
        SaplingBlock(
            TreeGrower(
                "pale_oak", Optional.of(ModConfiguredFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty()
            ), newProperties {
                mapColor(MapColor.METAL)
                noCollission()
                randomTicks()
                instabreak()
                sound(SoundType.GRASS)
                pushReaction(PushReaction.DESTROY)
            })
    }

    private fun logProperties(@Suppress("SameParameterValue") sideColor: MapColor, topColor: MapColor) = newProperties {
        mapColor { if (it.getValue(RotatedPillarBlock.AXIS) === Direction.Axis.Y) sideColor else topColor }
        instrument(NoteBlockInstrument.BASS)
        strength(2f)
        sound(SoundType.WOOD)
        ignitedByLava()
    }

    val PALE_OAK_LOG = new("pale_oak_log") {
        PaleOakLogBlock(
            STRIPPED_PALE_OAK_LOG, logProperties(
                MapColor.QUARTZ, MapColor.STONE
            )
        )
    }
    val STRIPPED_PALE_OAK_LOG = new("stripped_pale_oak_log") {
        RotatedPillarBlock(
            logProperties(
                MapColor.QUARTZ, MapColor.QUARTZ
            )
        )
    }

    private fun paleOakWoodProperties(builder: BlockBehaviour.Properties.() -> Unit) = newProperties {
        mapColor(MapColor.QUARTZ)
        instrument(NoteBlockInstrument.BASS)
        strength(2f)
        sound(SoundType.WOOD)
        ignitedByLava()
    }.apply(builder)

    val PALE_OAK_WOOD = new("pale_oak_wood") {
        PaleOakLogBlock(STRIPPED_PALE_OAK_WOOD, paleOakWoodProperties {
            mapColor(MapColor.STONE)
        })
    }
    val STRIPPED_PALE_OAK_WOOD = new("stripped_pale_oak_wood") {
        RotatedPillarBlock(paleOakWoodProperties {})
    }
    val PALE_OAK_PLANKS = block("pale_oak_planks", paleOakWoodProperties {
        strength(2f, 3f)
    })

    val PALE_OAK_SIGN = new("pale_oak_sign") {
        PaleOakStandingSignBlock(paleOakWoodProperties {
            forceSolidOn()
            noCollission()
            strength(1f)
        })
    }
    val PALE_OAK_WALL_SIGN = new("pale_oak_wall_sign") {
        PaleOakWallSignBlock(paleOakWoodProperties {
            forceSolidOn()
            noCollission()
            strength(1f)
            lootFrom(PALE_OAK_SIGN)
        })
    }
    val PALE_OAK_HANGING_SIGN = new("pale_oak_hanging_sign") {
        PaleOakCeilingHangingSignBlock(paleOakWoodProperties {
            forceSolidOn()
            noCollission()
            strength(1f)
        })
    }
    val PALE_OAK_WALL_HANGING_SIGN = new("pale_oak_wall_hanging_sign") {
        PaleOakWallHangingSignBlock(paleOakWoodProperties {
            forceSolidOn()
            noCollission()
            strength(1f)
            lootFrom(PALE_OAK_HANGING_SIGN)
        })
    }

    val PALE_OAK_PRESSURE_PLATE = new("pale_oak_pressure_plate") {
        PressurePlateBlock(ModBlockSetTypes.PALE_OAK, paleOakWoodProperties {
            forceSolidOn()
            noCollission()
            strength(0.5f)
            pushReaction(PushReaction.DESTROY)
        })
    }
    val PALE_OAK_TRAPDOOR = new("pale_oak_trapdoor") {
        TrapDoorBlock(ModBlockSetTypes.PALE_OAK, paleOakWoodProperties {
            noOcclusion()
            isValidSpawn { _, _, _, _ -> false }
            ignitedByLava()
        })
    }

    @Suppress("DEPRECATION")
    val PALE_OAK_STAIRS = new("pale_oak_stairs") {
        StairBlock(
            PALE_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(PALE_OAK_PLANKS.get())
        )
    }
    val PALE_OAK_SLAB = new("pale_oak_slab") {
        SlabBlock(paleOakWoodProperties {
            strength(2f, 3f)
        })
    }
    val PALE_OAK_FENCE_GATE = new("pale_oak_fence_gate") {
        FenceGateBlock(ModWoodTypes.PALE_OAK, paleOakWoodProperties {
            forceSolidOn()
            strength(2f, 3f)
        })
    }
    val PALE_OAK_FENCE = new("pale_oak_fence") {
        FenceBlock(paleOakWoodProperties {
            strength(2f, 3f)
        })
    }
    val PALE_OAK_DOOR = new("pale_oak_door") {
        DoorBlock(ModBlockSetTypes.PALE_OAK, paleOakWoodProperties {
            noOcclusion()
            pushReaction(PushReaction.DESTROY)
        })
    }
    val PALE_OAK_BUTTON = new("pale_oak_button") {
        ButtonBlock(ModBlockSetTypes.PALE_OAK, 30, newProperties {
            noCollission()
            strength(0.5f)
            pushReaction(PushReaction.DESTROY)
        })
    }

    private fun flowerPotProperties() = newProperties {
        instabreak()
        noOcclusion()
        pushReaction(PushReaction.DESTROY)
    }

    val POTTED_PALE_OAK_SAPLING = new("potted_pale_oak_sapling") {
        FlowerPotBlock({ Blocks.FLOWER_POT as FlowerPotBlock }, PALE_OAK_SAPLING, flowerPotProperties())
    }

    val PALE_MOSS_BLOCK = new("pale_moss_block") {
        PaleMossBlock(newProperties {
            ignitedByLava()
            mapColor(MapColor.COLOR_LIGHT_GRAY)
            strength(0.1f)
            sound(SoundType.MOSS)
            pushReaction(PushReaction.DESTROY)
        })
    }
    val PALE_MOSS_CARPET = new("pale_moss_carpet") {
        PaleMossCarpetBlock(newProperties {
            ignitedByLava()
            mapColor(PALE_MOSS_BLOCK.get().defaultMapColor())
            strength(0.1f)
            sound(SoundType.MOSS_CARPET)
            pushReaction(PushReaction.DESTROY)
        })
    }
    val PALE_HANGING_MOSS = new("pale_hanging_moss") {
        PaleHangingMossBlock(newProperties {
            ignitedByLava()
            mapColor(PALE_MOSS_BLOCK.get().defaultMapColor())
            noCollission()
            sound(SoundType.MOSS_CARPET)
            pushReaction(PushReaction.DESTROY)
        })
    }

    val OPEN_EYEBLOSSOM = new("open_eyeblossom") {
        EyeblossomBlock(EyeblossomBlock.Type.OPEN, newProperties {
            mapColor(MapColor.COLOR_ORANGE)
            noCollission()
            instabreak()
            sound(SoundType.GRASS)
            offsetType(BlockBehaviour.OffsetType.XZ)
            pushReaction(PushReaction.DESTROY)
            randomTicks()
        })
    }
    val CLOSED_EYEBLOSSOM = new("closed_eyeblossom") {
        EyeblossomBlock(EyeblossomBlock.Type.CLOSED, newProperties {
            mapColor(MapColor.METAL)
            noCollission()
            instabreak()
            sound(SoundType.GRASS)
            offsetType(BlockBehaviour.OffsetType.XZ)
            pushReaction(PushReaction.DESTROY)
            randomTicks()
        })
    }
    val POTTED_OPEN_EYEBLOSSOM = new("potted_open_eyeblossom") {
        EyeblossomFlowerPotBlock(
            { Blocks.FLOWER_POT as FlowerPotBlock }, OPEN_EYEBLOSSOM, flowerPotProperties().randomTicks()
        )
    }
    val POTTED_CLOSED_EYEBLOSSOM = new("potted_closed_eyeblossom") {
        EyeblossomFlowerPotBlock(
            { Blocks.FLOWER_POT as FlowerPotBlock }, CLOSED_EYEBLOSSOM, flowerPotProperties().randomTicks()
        )
    }

    // 1.21.5
    val LEAF_LITTER = new("leaf_litter") {
        LeafLitterBlock(newProperties {
            mapColor(MapColor.COLOR_BROWN)
            replaceable()
            noCollission()
            sound(ModSoundTypes.LEAF_LITTER)
            pushReaction(PushReaction.DESTROY)
        })
    }
    val WILDFLOWERS = new("wildflowers") {
        FlowerBedBlock(newProperties {
            mapColor(MapColor.PLANT)
            noCollission()
            sound(SoundType.PINK_PETALS)
            pushReaction(PushReaction.DESTROY)
        })
    }

    // Ported
    val GLOWING_OBSIDIAN = portedBlock("glowing_obsidian", copyProperties(Blocks.OBSIDIAN) {
        lightLevel { 12 }
    })
    val NETHER_REACTOR_CORE = portedNew("nether_reactor_core") {
        NetherReactorCoreBlock(newProperties {
            strength(3f)
            mapColor(MapColor.COLOR_LIGHT_GRAY)
            requiresCorrectToolForDrops()
            sound(SoundType.STONE)
        })
    }
    val STONECUTTER = portedBlock("stonecutter", copyProperties(Blocks.STONECUTTER) {})

    private fun <B : Block> new(path: String, content: (BlockBehaviour.Properties) -> B): DeferredBlock<B> =
        BLOCKS.registerBlock(path, content)

    private fun <B : Block> portedNew(path: String, content: (BlockBehaviour.Properties) -> B): DeferredBlock<B> =
        PORTED_BLOCKS.registerBlock(path, content)

    private fun block(path: String, builder: BlockBehaviour.Properties): DeferredBlock<Block> =
        BLOCKS.registerSimpleBlock(path, builder)

    private fun portedBlock(path: String, builder: BlockBehaviour.Properties): DeferredBlock<Block> =
        PORTED_BLOCKS.registerSimpleBlock(path, builder)

    private fun newProperties(builder: BlockBehaviour.Properties.() -> Unit) =
        BlockBehaviour.Properties.of().apply(builder)

    private fun copyProperties(base: BlockBehaviour, builder: BlockBehaviour.Properties.() -> Unit) =
        BlockBehaviour.Properties.ofFullCopy(base).apply(builder)

}
