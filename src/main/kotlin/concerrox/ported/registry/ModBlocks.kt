package concerrox.ported.registry

import concerrox.ported.util.new
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.SpawnerBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(ResourceLocation.DEFAULT_NAMESPACE)

    // TODO:fix this
//    val SPAWNER = BLOCKS.new("spawner") {
//        SpawnerBlock(newProperties {
//            mapColor(MapColor.STONE)
//            instrument(NoteBlockInstrument.BASEDRUM)
//            requiresCorrectToolForDrops()
//            strength(5f)
//            sound(ModSoundTypes.SPAWNER)
//            noOcclusion()
//        })
//    }

    private fun newProperties(builder: BlockBehaviour.Properties.() -> Unit) =
        BlockBehaviour.Properties.of().apply(builder)

}
