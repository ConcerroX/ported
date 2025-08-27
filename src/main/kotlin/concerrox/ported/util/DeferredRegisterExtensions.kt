package concerrox.ported.util

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

fun <T : Any, R : T> DeferredRegister<T>.new(path: String, content: () -> R): DeferredHolder<T, R> =
    register(path, content)