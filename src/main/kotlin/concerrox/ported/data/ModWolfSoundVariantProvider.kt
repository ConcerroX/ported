package concerrox.ported.data

import concerrox.ported.content.springtolife.wolf.WolfSoundVariant
import concerrox.ported.content.springtolife.wolf.WolfSoundVariants
import net.minecraft.data.worldgen.BootstrapContext

object ModWolfSoundVariantProvider {

    fun bootstrap(context: BootstrapContext<WolfSoundVariant>) {
        WolfSoundVariants.bootstrap(context)
    }

}