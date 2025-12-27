package concerrox.ported.content.springtolife.mobvariant.model

import net.minecraft.client.model.ChickenModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.animal.Chicken
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ColdChickenModel(p_480686_: ModelPart) : ChickenModel<Chicken>(p_480686_) {

    companion object {

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = createBodyLayer2()
            meshdefinition.root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 9).addBox(-3.0f, -4.0f, -3.0f, 6.0f, 8.0f, 6.0f).texOffs(38, 9)
                    .addBox(0.0f, 3.0f, -1.0f, 0.0f, 3.0f, 5.0f),
                PartPose.offsetAndRotation(0.0f, 16.0f, 0.0f, (Math.PI.toFloat() / 2f), 0.0f, 0.0f)
            )
            meshdefinition.root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -6.0f, -2.0f, 4.0f, 6.0f, 3.0f).texOffs(44, 0)
                    .addBox(-3.0f, -7.0f, -2.015f, 6.0f, 3.0f, 4.0f),
                PartPose.offset(0.0f, 15.0f, -4.0f)
            )
            return LayerDefinition.create(meshdefinition, 64, 32)
        }

        fun createBodyLayer2(): MeshDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root
            partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -6.0f, -2.0f, 4.0f, 6.0f, 3.0f),
                PartPose.offset(0.0f, 15.0f, -4.0f)
            )
            partdefinition.addOrReplaceChild(
                "beak",
                CubeListBuilder.create().texOffs(14, 0).addBox(-2.0f, -4.0f, -4.0f, 4.0f, 2.0f, 2.0f),
                PartPose.offset(0.0f, 15.0f, -4.0f)
            )
            partdefinition.addOrReplaceChild(
                "red_thing",
                CubeListBuilder.create().texOffs(14, 4).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 2.0f, 2.0f),
                PartPose.offset(0.0f, 15.0f, -4.0f)
            )
            partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 9).addBox(-3.0f, -4.0f, -3.0f, 6.0f, 8.0f, 6.0f),
                PartPose.offsetAndRotation(0.0f, 16.0f, 0.0f, (Math.PI.toFloat() / 2f), 0.0f, 0.0f)
            )
            val cubelistbuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0f, 0.0f, -3.0f, 3.0f, 5.0f, 3.0f)
            partdefinition.addOrReplaceChild("right_leg", cubelistbuilder, PartPose.offset(-2.0f, 19.0f, 1.0f))
            partdefinition.addOrReplaceChild("left_leg", cubelistbuilder, PartPose.offset(1.0f, 19.0f, 1.0f))
            partdefinition.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(0.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                PartPose.offset(-4.0f, 13.0f, 0.0f)
            )
            partdefinition.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(-1.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                PartPose.offset(4.0f, 13.0f, 0.0f)
            )
            return meshdefinition
        }

        protected fun createBaseChickenModel(): MeshDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root
            val partdefinition1 = partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -6.0f, -2.0f, 4.0f, 6.0f, 3.0f),
                PartPose.offset(0.0f, 15.0f, -4.0f)
            )
            partdefinition1.addOrReplaceChild(
                "beak",
                CubeListBuilder.create().texOffs(14, 0).addBox(-2.0f, -4.0f, -4.0f, 4.0f, 2.0f, 2.0f),
                PartPose.ZERO
            )
            partdefinition1.addOrReplaceChild(
                "red_thing",
                CubeListBuilder.create().texOffs(14, 4).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 2.0f, 2.0f),
                PartPose.ZERO
            )
            partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 9).addBox(-3.0f, -4.0f, -3.0f, 6.0f, 8.0f, 6.0f),
                PartPose.offsetAndRotation(0.0f, 16.0f, 0.0f, (Math.PI.toFloat() / 2f), 0.0f, 0.0f)
            )
            val cubelistbuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0f, 0.0f, -3.0f, 3.0f, 5.0f, 3.0f)
            partdefinition.addOrReplaceChild("right_leg", cubelistbuilder, PartPose.offset(-2.0f, 19.0f, 1.0f))
            partdefinition.addOrReplaceChild("left_leg", cubelistbuilder, PartPose.offset(1.0f, 19.0f, 1.0f))
            partdefinition.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(0.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                PartPose.offset(-4.0f, 13.0f, 0.0f)
            )
            partdefinition.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(-1.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                PartPose.offset(4.0f, 13.0f, 0.0f)
            )
            return meshdefinition
        }

    }

}