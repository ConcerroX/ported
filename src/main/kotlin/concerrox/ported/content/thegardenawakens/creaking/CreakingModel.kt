package concerrox.ported.content.thegardenawakens.creaking

import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CreakingModel(private val modelPart: ModelPart) : HierarchicalModel<Creaking>(RenderType::entityCutoutNoCull) {

    private val head: ModelPart
    private val headParts: MutableList<ModelPart>

    private val walkAnimation = CreakingAnimation.CREAKING_WALK
    private val attackAnimation = CreakingAnimation.CREAKING_ATTACK
    private val invulnerableAnimation = CreakingAnimation.CREAKING_INVULNERABLE
    private val deathAnimation = CreakingAnimation.CREAKING_DEATH

    init {
        val root = modelPart.getChild("root")
        val upperBody = root.getChild("upper_body")
        head = upperBody.getChild("head")
        headParts = mutableListOf(head)
    }

//    fun getHeadModelParts(creaking: Creaking) = if (!creaking.eyesGlowing) NO_PARTS else headParts

    override fun root(): ModelPart = modelPart.getChild("root")

    override fun setupAnim(creaking: Creaking, p1: Float, partialTicks: Float, ageInTicks: Float, yRot: Float, xRot: Float) {
        root().allParts.forEach { part -> part.resetPose() }
        head.xRot = xRot * Math.PI.toFloat() / 180f
        head.yRot = yRot * Math.PI.toFloat() / 180f
        if (creaking.canMove()) {
            animateWalk(walkAnimation, creaking.walkAnimation.position(), creaking.walkAnimation.speed(), 1f, 1f)
        }
        val tickCount = creaking.tickCount.toFloat()
        animate(creaking.attackAnimationState, attackAnimation, tickCount)
        animate(creaking.invulnerabilityAnimationState, invulnerableAnimation, tickCount)
        animate(creaking.deathAnimationState, deathAnimation, tickCount)
    }

    companion object {

//        val NO_PARTS = mutableListOf<ModelPart>()

        fun createBodyLayer(): LayerDefinition = LayerDefinition.create(createMesh(), 64, 64)

        private fun createMesh(): MeshDefinition {
            val mesh = MeshDefinition()
            val meshRoot = mesh.root
            val root = meshRoot.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0f, 24f, 0f))
            val upperBody = root.addOrReplaceChild(
                "upper_body", CubeListBuilder.create(), PartPose.offset(-1f, -19f, 0f)
            )
            upperBody.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3f, -10f, -3f, 6f, 10f, 6f).texOffs(28, 31)
                    .addBox(-3f, -13f, -3f, 6f, 3f, 6f).texOffs(12, 40).addBox(3f, -13f, 0f, 9f, 14f, 0f)
                    .texOffs(34, 12).addBox(-12f, -14f, 0f, 9f, 14f, 0f),
                PartPose.offset(-3f, -11f, 0f)
            )
            upperBody.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 16).addBox(0f, -3f, -3f, 6f, 13f, 5f).texOffs(24, 0)
                    .addBox(-6f, -4f, -3f, 6f, 7f, 5f),
                PartPose.offset(0f, -7f, 1f)
            )
            upperBody.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create().texOffs(22, 13).addBox(-2f, -1.5f, -1.5f, 3f, 21f, 3f).texOffs(46, 0)
                    .addBox(-2f, 19.5f, -1.5f, 3f, 4f, 3f),
                PartPose.offset(-7f, -9.5f, 1.5f)
            )
            upperBody.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create().texOffs(30, 40).addBox(0f, -1f, -1.5f, 3f, 16f, 3f).texOffs(52, 12)
                    .addBox(0f, -5f, -1.5f, 3f, 4f, 3f).texOffs(52, 19).addBox(0f, 15f, -1.5f, 3f, 4f, 3f),
                PartPose.offset(6f, -9f, 0.5f)
            )
            root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(42, 40).addBox(-1.5f, 0f, -1.5f, 3f, 16f, 3f).texOffs(45, 55)
                    .addBox(-1.5f, 15.7f, -4.5f, 5f, 0f, 9f),
                PartPose.offset(1.5f, -16f, 0.5f)
            )
            root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(0, 34).addBox(-3f, -1.5f, -1.5f, 3f, 19f, 3f).texOffs(45, 46)
                    .addBox(-5f, 17.2f, -4.5f, 5f, 0f, 9f).texOffs(12, 34).addBox(-3f, -4.5f, -1.5f, 3f, 3f, 3f),
                PartPose.offset(-1f, -17.5f, 0.5f)
            )
            return mesh
        }

    }

}