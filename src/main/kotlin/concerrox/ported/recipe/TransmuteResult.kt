package concerrox.ported.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@JvmRecord
data class TransmuteResult(val item: Holder<Item>, val count: Int, val components: DataComponentPatch) {

    constructor(item: Item) : this(item.builtInRegistryHolder(), 1, DataComponentPatch.EMPTY)

    fun apply(stack: ItemStack): ItemStack {
        val itemStack = stack.transmuteCopy(item.value(), this.count)
        itemStack.applyComponents(this.components)
        return itemStack
    }

    fun isResultUnchanged(stack: ItemStack): Boolean {
        val itemStack = apply(stack)
        return itemStack.count == 1 && ItemStack.isSameItemSameComponents(stack, itemStack)
    }

//    fun display(): SlotDisplay {
//        return ItemStackSlotDisplay(ItemStack(this.item, this.count, this.components))
//    }

    companion object {

        private fun validateStrict(stack: ItemStack): DataResult<ItemStack> {
            val result = ItemStack.validateComponents(stack.getComponents())
            return if (result.isError) result.map { _ -> stack }
            else if (stack.count > stack.maxStackSize) DataResult.error { "Item stack with stack size of " + stack.count + " was larger than maximum: " + stack.maxStackSize }
            else DataResult.success(stack)
        }

        private fun validate(result: TransmuteResult): DataResult<TransmuteResult> {
            return validateStrict(ItemStack(result.item, result.count, result.components)).map { _ -> result }
        }

        private val FULL_CODEC = RecordCodecBuilder.create {
            it.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("id").forGetter(TransmuteResult::item),
                ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(TransmuteResult::count),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                    .forGetter(TransmuteResult::components)
            ).apply(it, ::TransmuteResult)
        }

        val CODEC: Codec<TransmuteResult> =
            Codec.withAlternative(FULL_CODEC, BuiltInRegistries.ITEM.byNameCodec(), ::TransmuteResult)
                .validate { validate(it) }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TransmuteResult> = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM),
            TransmuteResult::item,
            ByteBufCodecs.VAR_INT,
            TransmuteResult::count,
            DataComponentPatch.STREAM_CODEC,
            TransmuteResult::components,
            ::TransmuteResult
        )
    }
}