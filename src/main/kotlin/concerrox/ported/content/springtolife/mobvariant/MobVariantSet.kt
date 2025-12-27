package concerrox.ported.content.springtolife.mobvariant

import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.biome.Biome

class MobVariantSet<E : Entity>() {

    internal val variants = mutableMapOf<TagKey<Biome>?, MutableSet<MobVariant<E>>>()

    fun add(variant: MobVariant<E>) {
        variants.getOrPut(variant.spawnBiomes) { mutableSetOf() } += variant
    }

//    companion object {
//
//        @Suppress("UNCHECKED_CAST")
//        val CODEC: Codec<MobVariantSet<out Entity>> = Codec.list(MobVariant.CODEC).xmap({
//            MobVariantSet<Entity>().apply {
//                variants.addAll(
//                    it.filterNotNull().map { item -> item as MobVariant<Entity> })
//            }
//        }, {
//            it.variants.toList()
//        })
//
//        @Suppress("UNCHECKED_CAST")
//        val STREAM_CODEC: StreamCodec<ByteBuf, MobVariantSet<out Entity>> =
//            ByteBufCodecs.collection(::HashSet, MobVariant.STREAM_CODEC, 256).map({
//                MobVariantSet<Entity>().apply { variants.addAll(it as HashSet<MobVariant<Entity>>) }
//            }, {
//                it.variants.toHashSet()
//            })
//
//    }

}