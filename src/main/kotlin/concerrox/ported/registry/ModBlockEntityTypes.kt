package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.content.springtolife.test.TestBlockEntity
import concerrox.ported.content.thegardenawakens.creakingheart.CreakingHeartBlockEntity
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakHangingSignBlockEntity
import concerrox.ported.content.thegardenawakens.paleoak.PaleOakSignBlockEntity
import concerrox.ported.util.new
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModBlockEntityTypes {

    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.DEFAULT_NAMESPACE)

    val CREAKING_HEART = BLOCK_ENTITY_TYPES.new("creaking_heart") {
        BlockEntityType.Builder.of(::CreakingHeartBlockEntity, ModBlocks.CREAKING_HEART.get()).build(null)
    }
    val TEST_BLOCK = BLOCK_ENTITY_TYPES.new("test_block") {
        BlockEntityType.Builder.of(::TestBlockEntity, ModBlocks.TEST_BLOCK.get(),).build(null)
    }



    val PORTED_BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Ported.MOD_ID)

    val SIGN = PORTED_BLOCK_ENTITY_TYPES.new("sign") {
        BlockEntityType.Builder.of(::PaleOakSignBlockEntity, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get())
            .build(null)
    }
    val HANGING_SIGN = PORTED_BLOCK_ENTITY_TYPES.new("hanging_sign") {
        BlockEntityType.Builder.of(
            ::PaleOakHangingSignBlockEntity, ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get()
        ).build(null)
    }

}