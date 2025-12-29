package concerrox.ported.content.springtolife.test

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.GameMasterBlock

class TestInstanceBlock(properties: Properties) : Block(properties), GameMasterBlock {

    // TODO: actual functions

    override fun codec(): MapCodec<TestInstanceBlock> = simpleCodec(::TestInstanceBlock)

}