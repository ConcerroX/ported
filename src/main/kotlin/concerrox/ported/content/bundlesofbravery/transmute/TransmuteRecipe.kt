package concerrox.ported.content.bundlesofbravery.transmute

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import concerrox.ported.registry.ModRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class TransmuteRecipe(
    private val group: String,
    private val bookCategory: CraftingBookCategory,
    val input: Ingredient,
    val material: Ingredient,
    private val result: TransmuteResult
) : CraftingRecipe {
//    private var placementInfo: PlacementInfo? = null

    override fun matches(craftingInput: CraftingInput, level: Level): Boolean {
        if (craftingInput.ingredientCount() != 2) return false
        var isInputValid = false
        var isMaterialValid = false
        for (i in 0..<craftingInput.size()) {
            val stack = craftingInput.getItem(i)
            if (!stack.isEmpty) {
                if (!isInputValid && input.test(stack)) {
                    if (result.isResultUnchanged(stack)) return false
                    isInputValid = true
                } else {
                    if (isMaterialValid || !material.test(stack)) return false
                    isMaterialValid = true
                }
            }
        }
        return isInputValid && isMaterialValid
    }

    override fun assemble(craftingInput: CraftingInput, provider: HolderLookup.Provider): ItemStack {
        for (i in 0..<craftingInput.size()) {
            val stack = craftingInput.getItem(i)
            if (!stack.isEmpty && input.test(stack)) return result.apply(stack)
        }
        return ItemStack.EMPTY
    }

    override fun canCraftInDimensions(width: Int, height: Int) = width * height >= ingredients.size

    // Croptopia mod does not handle nullability correctly so we have to make this nullable
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getResultItem(registries: HolderLookup.Provider?) = ItemStack(result.item, result.count)

//    public override fun display(): MutableList<RecipeDisplay> {
//        return List.of<RecipeDisplay?>(
//            ShapelessCraftingRecipeDisplay(
//                List.of(this.input.display(), this.material.display()),
//                this.result.display(),
//                ItemSlotDisplay(Items.CRAFTING_TABLE)
//            )
//        )
//    }

    override fun getSerializer(): Serializer = ModRecipeSerializers.TRANSMUTE.get()
    override fun getGroup() = group
    override fun category() = bookCategory

//    override fun placementInfo(): PlacementInfo {
//        if (this.placementInfo == null) {
//            this.placementInfo = PlacementInfo.create(List.of<E?>(this.input, this.material))
//        }
//
//        return this.placementInfo
//    }

    class Serializer : RecipeSerializer<TransmuteRecipe> {

        override fun codec(): MapCodec<TransmuteRecipe> = CODEC
        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> = STREAM_CODEC

        companion object {
            private val CODEC = RecordCodecBuilder.mapCodec {
                it.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(TransmuteRecipe::group),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC)
                        .forGetter(TransmuteRecipe::bookCategory),
                    Ingredient.CODEC.fieldOf("input").forGetter(TransmuteRecipe::input),
                    Ingredient.CODEC.fieldOf("material").forGetter(TransmuteRecipe::material),
                    TransmuteResult.CODEC.fieldOf("result").forGetter(TransmuteRecipe::result)
                ).apply(it, ::TransmuteRecipe)
            }
            private val STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                TransmuteRecipe::group,
                CraftingBookCategory.STREAM_CODEC,
                TransmuteRecipe::bookCategory,
                Ingredient.CONTENTS_STREAM_CODEC,
                TransmuteRecipe::input,
                Ingredient.CONTENTS_STREAM_CODEC,
                TransmuteRecipe::material,
                TransmuteResult.STREAM_CODEC,
                TransmuteRecipe::result,
                ::TransmuteRecipe
            )
        }
    }
}