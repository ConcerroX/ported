package concerrox.ported.content.springtolife.mobvariant

import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.biome.Biome
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

object MobVariantManager {

    internal val entityToVariantSets = mutableMapOf<EntityType<out Entity>, MobVariantSet<out Entity>>()
    internal val keyToVariant = mutableMapOf<ResourceLocation, MobVariant<out Entity>?>()

    fun hasVariant(entityType: EntityType<out Entity>): Boolean {
        return entityToVariantSets.containsKey(entityType)
    }

    fun getVariantSet(entityType: EntityType<out Entity>): MobVariantSet<out Entity>? {
        return entityToVariantSets[entityType]
    }

    fun randomVariantByBiomeTag(entityType: EntityType<out Entity>, biomeTag: TagKey<Biome>): MobVariant<out Entity> {
        return getVariantSet(entityType)?.variants[biomeTag]?.random()
            ?: throw IllegalArgumentException("No variant found for $entityType and $biomeTag")
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Entity> addVariants(entity: EntityType<E>, vararg variants: MobVariant<E>) {
        variants.forEach { addVariant(entity, it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Entity> addVariant(entity: EntityType<E>, variant: MobVariant<E>) {
        keyToVariant[variant.name] = variant
        (entityToVariantSets.getOrPut(entity) { MobVariantSet() } as MobVariantSet<E>).add(variant)
    }

    internal fun onFinalizeSpawn(
        entity: Entity,
        level: ServerLevelAccessor,
    ): MobVariant<out Entity>? {
        if (!hasVariant(entity.type)) return null
        val biomeTagToVariants = getVariantSet(entity.type)!!.variants
        for ((biomeTag, variants) in biomeTagToVariants) {
            if (biomeTag == null) continue
            if (level.getBiome(entity.blockPosition()).`is`(biomeTag)) return variants.random()
        }
        return biomeTagToVariants[null]?.random()
    }

    @JvmRecord
    data class TextureModelData<E : Entity>(
        val entityModelConstructor: (ModelPart) -> EntityModel<E>,
        val adultModel: ModelLayerLocation,
        val babyModel: ModelLayerLocation?,
        val textureLocation: ResourceLocation
    ) {
        constructor(
            entityModelConstructor: (ModelPart) -> EntityModel<E>,
            key: String,
            textureLocation: ResourceLocation,
            noBabyModel: Boolean = true
        ) : this(
            entityModelConstructor,
            ModelLayerLocation(ResourceLocation.withDefaultNamespace(key), "main"),
            if (noBabyModel) null else ModelLayerLocation(ResourceLocation.withDefaultNamespace(key + "_baby"), "main"),
            textureLocation
        )
    }

    @OnlyIn(Dist.CLIENT)
    @JvmRecord
    data class AdultAndBabyModelPair<T : EntityModel<out Entity>>(val adultModel: T, val babyModel: T?) {
        fun getModel(isBaby: Boolean) = if (isBaby && babyModel != null) babyModel else adultModel
    }

}