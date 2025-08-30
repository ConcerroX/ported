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

    private fun newProperties(builder: BlockBehaviour.Properties.() -> Unit) =
        BlockBehaviour.Properties.of().apply(builder)

}
