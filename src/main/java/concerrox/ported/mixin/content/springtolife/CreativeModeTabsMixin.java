package concerrox.ported.mixin.content.springtolife;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {

    @ModifyExpressionValue(method = "lambda$bootstrap$27", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;PIG_SPAWN_EGG:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC))
    private static Item ported$replaceSpawnEggTabIcon(Item original) {
        return Items.CREEPER_SPAWN_EGG;
    }

}
