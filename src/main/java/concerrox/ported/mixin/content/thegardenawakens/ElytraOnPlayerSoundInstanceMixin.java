package concerrox.ported.mixin.content.thegardenawakens;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraOnPlayerSoundInstance.class)
public abstract class ElytraOnPlayerSoundInstanceMixin extends AbstractTickableSoundInstance {

    protected ElytraOnPlayerSoundInstanceMixin(SoundEvent p_235076_, SoundSource p_235077_, RandomSource p_235078_) {
        super(p_235076_, p_235077_, p_235078_);
    }

    /**
     * ElytraOnPlayerSoundInstance updates its position in every tick but not at initial,
     * However, the change will not be sent to the subtitle overlay,
     * hence the position of the subtitle will always be (0, 0, 0).
     * This should fix this by setting the position at initial.
     * See <a href="https://bugs.mojang.com/browse/MC/issues/MC-177084">MC-177084</a>.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(LocalPlayer player, CallbackInfo ci) {
        x = (float) player.getX();
        y = (float) player.getY();
        z = (float) player.getZ();
    }

}
