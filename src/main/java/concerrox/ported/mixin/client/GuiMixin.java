package concerrox.ported.mixin.client;

import concerrox.ported.registry.ModSoundEvents;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    public int rightHeight;
    @Shadow
    @Final
    private static ResourceLocation AIR_SPRITE;
    @Shadow
    @Final
    private static ResourceLocation AIR_BURSTING_SPRITE;
    @Final
    @Shadow
    private RandomSource random;
    @Shadow
    private int tickCount;
    @Unique
    private int ported$lastBubblePopSoundPlayed;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract int getVisibleVehicleHeartRows(int vehicleHealth);

    @Unique
    private static final ResourceLocation AIR_EMPTY_SPRITE = ResourceLocation.withDefaultNamespace("hud/air_empty");

    @Unique
    private int ported$getAirBubbleYLine(int vehicleMaxHealth, int startY) {
        int heartRows = getVisibleVehicleHeartRows(vehicleMaxHealth) - 1;
        return startY - heartRows * 10;
    }

    @Unique
    private static int ported$getCurrentAirSupplyBubble(int currentAirSupply, int maxAirSupply, int offset) {
        return Mth.ceil((currentAirSupply + offset) * 10f / (float) maxAirSupply);
    }

    @Unique
    private static int ported$getEmptyBubbleDelayDuration(int airSupply, boolean isInWater) {
        return airSupply != 0 && isInWater ? 1 : 0;
    }

    @Unique
    private void ported$playAirBubblePoppedSound(int bubble, Player player, int pitch) {
        if (ported$lastBubblePopSoundPlayed != bubble) {
            float volume = 0.5F + 0.1F * (float) Math.max(0, pitch - 3 + 1);
            float f1 = 1.0F + 0.1F * (float) Math.max(0, pitch - 5 + 1);
            player.playSound(ModSoundEvents.INSTANCE.getBUBBLE_POP(), volume, f1);
            ported$lastBubblePopSoundPlayed = bubble;
        }
    }

    @Inject(method = "renderAirLevel", at = @At("HEAD"), cancellable = true)
    private void renderAirLevel(GuiGraphics guiGraphics, CallbackInfo ci) {
        var player = getCameraPlayer();
        if (player == null) return;

        int vehicleMaxHealth = 10;
        int x = guiGraphics.guiWidth() / 2 + 91;
        int y = guiGraphics.guiHeight() - rightHeight;

        int maxAirSupply = player.getMaxAirSupply();
        int airSupply = Math.clamp(player.getAirSupply(), 0, maxAirSupply);
        boolean isInWater = player.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value());

        if (isInWater || airSupply < maxAirSupply) {
            y = ported$getAirBubbleYLine(vehicleMaxHealth, y);
            int filledBubblesWithOffset = ported$getCurrentAirSupplyBubble(airSupply, maxAirSupply, -2);
            int currentFilledBubbles = ported$getCurrentAirSupplyBubble(airSupply, maxAirSupply, 0);
            int emptyBubblesCount = 10 - ported$getCurrentAirSupplyBubble(airSupply, maxAirSupply, ported$getEmptyBubbleDelayDuration(airSupply, isInWater));
            boolean isBubbleStateChanging = filledBubblesWithOffset != currentFilledBubbles;
            if (!isInWater) ported$lastBubblePopSoundPlayed = 0;

            for (int i = 1; i <= 10; ++i) {
                int bubbleX = x - (i - 1) * 8 - 9;
                if (i <= filledBubblesWithOffset) {
                    guiGraphics.blitSprite(AIR_SPRITE, bubbleX, y, 9, 9);
                } else if (isBubbleStateChanging && i == currentFilledBubbles && isInWater) {
                    guiGraphics.blitSprite(AIR_BURSTING_SPRITE, bubbleX, y, 9, 9);
                    ported$playAirBubblePoppedSound(i, player, emptyBubblesCount);
                } else if (i > 10 - emptyBubblesCount) {
                    int emptyBubbleYOffset = emptyBubblesCount == 10 && tickCount % 2 == 0 ? random.nextInt(2) : 0;
                    guiGraphics.blitSprite(AIR_EMPTY_SPRITE, bubbleX, y + emptyBubbleYOffset, 9, 9);
                }
            }
            rightHeight += 10;
        }
        ci.cancel();
    }

}
