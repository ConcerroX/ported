package concerrox.ported.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin extends Item {

    @Shadow
    @Final
    private Component upgradeDescription;

    @Shadow
    @Final
    private static ChatFormatting TITLE_FORMAT;

    @Unique
    private static final Component SMITHING_TEMPLATE_SUFFIX = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.withDefaultNamespace("smithing_template")))
            .withStyle(TITLE_FORMAT);

    public SmithingTemplateItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private Object appendHoverText(Object component) {
        if (component == upgradeDescription) return SMITHING_TEMPLATE_SUFFIX;
        return component;
    }

}
