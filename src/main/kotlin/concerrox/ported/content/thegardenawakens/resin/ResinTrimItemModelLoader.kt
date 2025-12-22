package concerrox.ported.content.thegardenawakens.resin

import com.google.gson.JsonObject
import concerrox.ported.PortedConfig
import dev.emi.emi.runtime.EmiPersistentData.GSON
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.util.GsonHelper
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

internal object ResinTrimItemModelLoader {

    private const val RESIN_MODEL_INDEX = 0.114514f

    fun loadModels(loadedModels: MutableMap<ResourceLocation, Resource>): MutableMap<ResourceLocation, Resource> {
        fun loadResource(resource: Resource): JsonObject = resource.openAsReader().use { GsonHelper.parse(it) }
        fun toResource(source: PackResources, obj: JsonObject) =
            Resource(source) { IOUtils.toInputStream(GSON.toJson(obj), StandardCharsets.UTF_8) }

        // TODO: fix this
        PortedConfig.DEFAULT_TRIMMABLE_ARMOR.forEach {
            val itemId = ResourceLocation.parse(it)
            val namespace = itemId.namespace
            val path = itemId.path
            val modelId = ResourceLocation.fromNamespaceAndPath(namespace, "models/item/$path.json")
            val modelRes = loadedModels[modelId] ?: return@forEach
            val itemModel = loadResource(modelRes)
            itemModel.getAsJsonArray("overrides").add(JsonObject().apply {
                val resinModelId = "$namespace:item/${path}_resin_trim"
                addProperty("model", resinModelId)
                add("predicate", JsonObject().apply {
                    addProperty("trim_type", RESIN_MODEL_INDEX)
                })
            })
            loadedModels[modelId] = toResource(modelRes.source(), itemModel)
        }
        return loadedModels
    }

}