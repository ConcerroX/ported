package concerrox.ported.mixin.content.thegardenawakens;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import concerrox.ported.content.thegardenawakens.resin.ResinTrimItemModelLoader;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {

    @ModifyExpressionValue(method = "lambda$loadBlockModels$7", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/resources/FileToIdConverter;listMatchingResources(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/Map;"))
    private static Map<ResourceLocation, Resource> loadBlockModels(Map<ResourceLocation, Resource> original) {
        return ResinTrimItemModelLoader.INSTANCE.loadModels(original);
    }

}
