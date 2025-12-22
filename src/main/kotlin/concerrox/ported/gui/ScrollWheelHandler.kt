package concerrox.ported.gui

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Vector2i
import kotlin.math.max
import kotlin.math.sign

@OnlyIn(Dist.CLIENT)
internal class ScrollWheelHandler {

    private var accumulatedScrollX = 0.0
    private var accumulatedScrollY = 0.0

    fun onMouseScroll(xOffset: Double, yOffset: Double): Vector2i {
        if (accumulatedScrollX != 0.0 && sign(xOffset) != sign(accumulatedScrollX)) {
            accumulatedScrollX = 0.0
        }
        if (accumulatedScrollY != 0.0 && sign(yOffset) != sign(accumulatedScrollY)) {
            accumulatedScrollY = 0.0
        }

        accumulatedScrollX += xOffset
        accumulatedScrollY += yOffset
        val i = accumulatedScrollX.toInt()
        val j = accumulatedScrollY.toInt()

        if (i == 0 && j == 0) {
            return Vector2i(0, 0)
        } else {
            accumulatedScrollX -= i.toDouble()
            accumulatedScrollY -= j.toDouble()
            return Vector2i(i, j)
        }
    }

    companion object {

        fun getNextScrollWheelSelection(yOffset: Double, selected: Int, selectionSize: Int): Int {
            var selected = selected
            val i = sign(yOffset).toInt()
            selected -= i

            selected = max(-1, selected)
            while (selected < 0) {
                selected += selectionSize
            }

            while (selected >= selectionSize) {
                selected -= selectionSize
            }

            return selected
        }

    }
}