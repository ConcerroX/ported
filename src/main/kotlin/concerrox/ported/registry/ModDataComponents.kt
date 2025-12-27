package concerrox.ported.registry

import concerrox.ported.Ported
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.UnaryOperator

object ModDataComponents {

    val DATA_COMPONENTS_PORTED: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Ported.MOD_ID)

//    val MOB_VARIANTS = dataComponentPorted("mob_variants") {
//        it.persistent(MobVariantSet.CODEC).networkSynchronized(MobVariantSet.STREAM_CODEC)
//    }

    fun <D> dataComponentPorted(
        name: String, builder: UnaryOperator<DataComponentType.Builder<D>>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<D>> {
        return DATA_COMPONENTS_PORTED.registerComponentType(name, builder)
    }

}