package concerrox.ported.content.springtolife.mobvariant

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import concerrox.ported.content.springtolife.mobvariant.MobVariantManager.TextureModelData
import concerrox.ported.content.springtolife.mobvariant.MobVariantManager.keyToVariant
import concerrox.ported.registry.ModBiomeTags
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.biome.Biome

data class MobVariant<E : Entity>(
    val name: ResourceLocation, val textureModelData: TextureModelData<E>, val spawnBiomes: TagKey<Biome>?
) {

    fun isWarm(): Boolean {
        return spawnBiomes == ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS
    }

    fun isCold(): Boolean {
        return spawnBiomes == ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS
    }

    companion object {

        val CODEC: Codec<MobVariant<out Entity>?> = ResourceLocation.CODEC.flatXmap({
            val variant = keyToVariant[it]
            if (variant != null) DataResult.success(variant) else DataResult.error { "Unknown mob variant: $it" }
        }, {
            DataResult.success(it?.name)
        })

        val STREAM_CODEC = object : StreamCodec<ByteBuf, MobVariant<out Entity>?> {

            override fun decode(buffer: ByteBuf): MobVariant<out Entity> {
                val key = ResourceLocation.STREAM_CODEC.decode(buffer)
                return keyToVariant[key] ?: throw IllegalArgumentException("Unknown mob variant: $key")
            }

            @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
            override fun encode(buffer: ByteBuf, value: MobVariant<out Entity>?) {
                if (value == null) throw IllegalStateException("Cannot encode null MobVariant")
                ResourceLocation.STREAM_CODEC.encode(buffer, value.name)
            }

        }

    }

}