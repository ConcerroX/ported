package concerrox.ported.content.thegardenawakens.resin

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.MultifaceSpreader

class ResinClumpBlock(properties: Properties) : MultifaceBlock(properties) {

    private val spreader = MultifaceSpreader(this)
    override fun codec(): MapCodec<ResinClumpBlock> = simpleCodec(::ResinClumpBlock)
    override fun getSpreader() = spreader

}