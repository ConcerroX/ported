package concerrox.ported.client.gui

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Vector2i
import kotlin.math.max
import kotlin.math.sign

@OnlyIn(Dist.CLIENT)
class ScrollWheelHandler {
    private var accumulatedScrollX = 0.0
    private var accumulatedScrollY = 0.0

    fun onMouseScroll(xOffset: Double, yOffset: Double): Vector2i {
        if (this.accumulatedScrollX != 0.0 && sign(xOffset) != sign(this.accumulatedScrollX)) {
            this.accumulatedScrollX = 0.0
        }

        if (this.accumulatedScrollY != 0.0 && sign(yOffset) != sign(this.accumulatedScrollY)) {
            this.accumulatedScrollY = 0.0
        }

        this.accumulatedScrollX += xOffset
        this.accumulatedScrollY += yOffset
        val i = this.accumulatedScrollX.toInt()
        val j = this.accumulatedScrollY.toInt()
        if (i == 0 && j == 0) {
            return Vector2i(0, 0)
        } else {
            this.accumulatedScrollX -= i.toDouble()
            this.accumulatedScrollY -= j.toDouble()
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