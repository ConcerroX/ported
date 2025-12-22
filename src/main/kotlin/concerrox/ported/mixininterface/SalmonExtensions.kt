package concerrox.ported.mixininterface

import concerrox.ported.content.bundlesofbravery.salmon.SalmonVariant
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.animal.Salmon

@Suppress("FunctionName")
interface SalmonExtensions {

    companion object {
        @JvmField
        val DATA_TYPE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(Salmon::class.java, EntityDataSerializers.INT)
//        @JvmStatic
//        fun of(salmon: Any): SalmonExtensions = SalmonExtensions::class.java.cast(salmon)
    }

    interface SalmonModelExtensions {
//        val SMALL_TRANSFORMER = MeshTransformer.scaling(0.5f)
//        val LARGE_TRANSFORMER = MeshTransformer.scaling(1.5f)
    }

    fun `ported$setVariant`(variant: SalmonVariant)
    fun `ported$getVariant`(): SalmonVariant

}