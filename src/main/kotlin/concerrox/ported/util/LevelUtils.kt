package concerrox.ported.util

import net.minecraft.world.level.Level

fun Level.isMoonVisible(): Boolean {
    if (!dimensionType().natural()) {
        return false
    } else {
        val i = (dayTime % 24000L).toInt()
        return i in 12600..23400
    }
}
