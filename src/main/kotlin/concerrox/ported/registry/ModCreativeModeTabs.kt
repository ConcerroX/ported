package concerrox.ported.registry

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.DEFAULT_NAMESPACE)

    init {
        CREATIVE_MODE_TABS.register("tab", Supplier {
            CreativeModeTab.builder().title(Component.translatable("itemGroup.blueberry"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
//                .icon { EXAMPLE_ITEM.get().defaultInstance }
                .displayItems { _, output ->
                    ModItems.ITEMS.entries.forEach {
                        output.accept(it.get())
                    }
                }.build()
        })
    }

}