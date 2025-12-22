package concerrox.ported.content.removedfeatures.netherreactorcore

import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty

class NetherReactorCoreBlock(properties: Properties) : Block(properties) {

    companion object {
        val STAGE: EnumProperty<Stage> = EnumProperty.create("stage", Stage::class.java)
    }

    init {
        registerDefaultState(defaultBlockState().setValue(STAGE, Stage.NORMAL))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(STAGE)
    }

    enum class Stage(private val id: String) : StringRepresentable {
        NORMAL("normal"), INITIALIZED("initialized"), FINISHED("finished");

        override fun getSerializedName() = id
    }

}