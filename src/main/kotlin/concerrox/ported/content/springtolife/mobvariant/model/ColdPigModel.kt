package concerrox.ported.content.springtolife.mobvariant.model

import net.minecraft.client.model.PigModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.animal.Pig
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ColdPigModel(p_478808_: ModelPart?) : PigModel<Pig>(p_478808_) {

    companion object {

        fun createBasePigModel(cubeDeformation: CubeDeformation): MeshDefinition {
            val meshdefinition = createBodyMesh(6, cubeDeformation)
            val partdefinition = meshdefinition.getRoot()
            partdefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -8.0f, 8.0f, 8.0f, 8.0f, cubeDeformation)
                    .texOffs(16, 16).addBox(-2.0f, 0.0f, -9.0f, 4.0f, 3.0f, 1.0f, cubeDeformation),
                PartPose.offset(0.0f, 12.0f, -6.0f)
            )
            return meshdefinition
        }

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition: MeshDefinition = createBasePigModel(CubeDeformation.NONE)
            val partdefinition = meshdefinition.getRoot()
            partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(28, 8).addBox(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f).texOffs(28, 32)
                    .addBox(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f, CubeDeformation(0.5f)),
                PartPose.offsetAndRotation(0.0f, 11.0f, 2.0f, (Math.PI.toFloat() / 2f), 0.0f, 0.0f)
            )
            return LayerDefinition.create(meshdefinition, 64, 64)
        }

    }

}