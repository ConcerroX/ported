package concerrox.ported.registry

import concerrox.ported.Ported
import concerrox.ported.content.springtolife.wolf.WolfSoundVariant
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.UnaryOperator

object ModDataComponents {

    val DATA_COMPONENTS: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ResourceLocation.DEFAULT_NAMESPACE)
    val DATA_COMPONENTS_PORTED: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Ported.MOD_ID)

    val WOLF_SOUND_VARIANT = dataComponent("wolf/sound_variant") {
        it.persistent(WolfSoundVariant.CODEC).networkSynchronized(WolfSoundVariant.STREAM_CODEC)
    }

    fun <D> dataComponent(
        name: String, builder: UnaryOperator<DataComponentType.Builder<D>>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<D>> {
        return DATA_COMPONENTS.registerComponentType(name, builder)
    }

    fun <D> dataComponentPorted(
        name: String, builder: UnaryOperator<DataComponentType.Builder<D>>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<D>> {
        return DATA_COMPONENTS_PORTED.registerComponentType(name, builder)
    }

}