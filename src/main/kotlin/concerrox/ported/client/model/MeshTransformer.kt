package concerrox.ported.client.model

import net.minecraft.client.model.geom.builders.MeshDefinition

fun interface MeshTransformer {

    fun apply(var1: MeshDefinition): MeshDefinition

    companion object {
//        fun scaling(scale: Float): MeshTransformer {
//            val f = 24.016f * (1.0f - scale)
//            return MeshTransformer { definition: MeshDefinition ->
//                definition.transformed({ p_362796_ ->
//                    p_362796_.scaled(
//                        scale
//                    ).translated(0.0f, f, 0.0f)
//                })
//            }
//        }
        val IDENTITY = MeshTransformer { definition -> definition }
    }
}