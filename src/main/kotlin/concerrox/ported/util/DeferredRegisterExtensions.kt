package concerrox.ported.util

import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

fun <T : Any, R : T> DeferredRegister<T>.new(path: String, content: () -> R): DeferredHolder<T, R> =
    register(path, content)