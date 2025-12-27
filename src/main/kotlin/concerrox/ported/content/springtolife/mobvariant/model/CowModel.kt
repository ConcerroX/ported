package concerrox.ported.content.springtolife.mobvariant.model

import net.minecraft.client.model.QuadrupedModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.animal.Cow
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class CowModel<T: Cow>(root: ModelPart) : QuadrupedModel<T>(root, false, 10.0f, 4.0f, 2.0f, 2.0f, 24) {

    fun getHead(): ModelPart = head

    companion object {
        private const val LEG_SIZE = 12

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = createBaseCowModel()
            return LayerDefinition.create(meshdefinition, 64, 64)
        }

        fun createBaseCowModel(): MeshDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.getRoot()
            partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -6.0f, 8.0f, 8.0f, 6.0f).texOffs(1, 33)
                    .addBox(-3.0f, 1.0f, -7.0f, 6.0f, 3.0f, 1.0f).texOffs(22, 0)
                    .addBox("right_horn", -5.0f, -5.0f, -5.0f, 1.0f, 3.0f, 1.0f).texOffs(22, 0)
                    .addBox("left_horn", 4.0f, -5.0f, -5.0f, 1.0f, 3.0f, 1.0f),
                PartPose.offset(0.0f, 4.0f, -8.0f)
            )
            partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(18, 4).addBox(-6.0f, -10.0f, -7.0f, 12.0f, 18.0f, 10.0f).texOffs(52, 0)
                    .addBox(-2.0f, 2.0f, -8.0f, 4.0f, 6.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 5.0f, 2.0f, (Math.PI.toFloat() / 2f), 0.0f, 0.0f)
            )
            val cubelistbuilder =
                CubeListBuilder.create().mirror().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f)
            val cubelistbuilder1 = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f)
            partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder1, PartPose.offset(-4.0f, 12.0f, 7.0f))
            partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(4.0f, 12.0f, 7.0f))
            partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder1, PartPose.offset(-4.0f, 12.0f, -5.0f))
            partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(4.0f, 12.0f, -5.0f))
            return meshdefinition
        }
    }
}