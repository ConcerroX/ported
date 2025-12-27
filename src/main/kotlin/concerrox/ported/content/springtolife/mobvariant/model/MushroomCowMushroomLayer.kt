package concerrox.ported.content.springtolife.mobvariant.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class MushroomCowMushroomLayer<T : MushroomCow>(
    renderer: RenderLayerParent<T, CowModel<T>>, private val blockRenderer: BlockRenderDispatcher
) : RenderLayer<T, CowModel<T>>(renderer) {

    override fun render(
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        if (!entity.isBaby) {
            val shouldNotRender = Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible
            if (!entity.isInvisible || shouldNotRender) {
                val blockstate = entity.variant.getBlockState()
                val i = LivingEntityRenderer.getOverlayCoords(entity, 0.0f)
                val blockstatemodel = this.blockRenderer.getBlockModel(blockstate)
                poseStack.pushPose()
                poseStack.translate(0.2f, -0.35f, 0.5f)
                poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f))
                poseStack.scale(-1.0f, -1.0f, 1.0f)
                poseStack.translate(-0.5f, -0.5f, -0.5f)
                this.renderMushroomBlock(poseStack, buffer, packedLight, shouldNotRender, blockstate, i, blockstatemodel)
                poseStack.popPose()
                poseStack.pushPose()
                poseStack.translate(0.2f, -0.35f, 0.5f)
                poseStack.mulPose(Axis.YP.rotationDegrees(42.0f))
                poseStack.translate(0.1f, 0.0f, -0.6f)
                poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f))
                poseStack.scale(-1.0f, -1.0f, 1.0f)
                poseStack.translate(-0.5f, -0.5f, -0.5f)
                this.renderMushroomBlock(poseStack, buffer, packedLight, shouldNotRender, blockstate, i, blockstatemodel)
                poseStack.popPose()
                poseStack.pushPose()
                (parentModel as CowModel<T>).getHead().translateAndRotate(poseStack)
                poseStack.translate(0.0f, -0.7f, -0.2f)
                poseStack.mulPose(Axis.YP.rotationDegrees(-78.0f))
                poseStack.scale(-1.0f, -1.0f, 1.0f)
                poseStack.translate(-0.5f, -0.5f, -0.5f)
                this.renderMushroomBlock(poseStack, buffer, packedLight, shouldNotRender, blockstate, i, blockstatemodel)
                poseStack.popPose()
            }
        }
    }

    private fun renderMushroomBlock(
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        renderOutline: Boolean,
        state: BlockState,
        packedOverlay: Int,
        model: BakedModel
    ) {
        if (renderOutline) {
            blockRenderer.modelRenderer.renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)),
                state,
                model,
                0.0f,
                0.0f,
                0.0f,
                packedLight,
                packedOverlay
            )
        } else {
            blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay)
        }
    }

}