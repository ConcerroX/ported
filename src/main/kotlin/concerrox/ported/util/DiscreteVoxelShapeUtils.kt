package concerrox.ported.util

import com.mojang.math.OctahedralGroup
import com.mojang.math.SymmetricGroup3
import net.minecraft.core.Direction
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape
import net.minecraft.world.phys.shapes.DiscreteVoxelShape
import org.joml.Vector3i

private fun SymmetricGroup3.permuteVector(vector: Vector3i): Vector3i? {
    val i = vector.get(permutation(0))
    val j = vector.get(permutation(1))
    val k = vector.get(permutation(2))
    return vector.set(i, j, k)
}

internal fun SymmetricGroup3.permuteAxis(axis: Direction.Axis): Direction.Axis {
    return Direction.Axis.VALUES[permutation(axis.ordinal)]
}

private fun OctahedralGroup.rotate(rotation: Vector3i): Vector3i {
    permutation.permuteVector(rotation)
    rotation.x *= if (invertX) -1 else 1
    rotation.y *= if (invertY) -1 else 1
    rotation.z *= if (invertZ) -1 else 1
    return rotation
}

internal fun DiscreteVoxelShape.rotate(octahedralGroup: OctahedralGroup): DiscreteVoxelShape {
    if (octahedralGroup == OctahedralGroup.IDENTITY) return this

    val vector3i = octahedralGroup.rotate(Vector3i(xSize, ySize, zSize))
    val i = DiscreteVoxelShapeUtils.fixupCoordinate(vector3i, 0)
    val j = DiscreteVoxelShapeUtils.fixupCoordinate(vector3i, 1)
    val k = DiscreteVoxelShapeUtils.fixupCoordinate(vector3i, 2)
    val discreteVoxelShape = BitSetDiscreteVoxelShape(vector3i.x, vector3i.y, vector3i.z)

    for (l in 0..<xSize) {
        for (i1 in 0..<ySize) {
            for (j1 in 0..<zSize) {
                if (isFull(l, i1, j1)) {
                    val vector3i1 = octahedralGroup.rotate(vector3i.set(l, i1, j1))
                    val k1 = i + vector3i1.x
                    val l1 = j + vector3i1.y
                    val i2 = k + vector3i1.z
                    discreteVoxelShape.fill(k1, l1, i2)
                }
            }
        }
    }
    return discreteVoxelShape
}

object DiscreteVoxelShapeUtils {

    fun fixupCoordinate(coordinate: Vector3i, axis: Int): Int {
        val i = coordinate.get(axis)
        if (i < 0) {
            coordinate.setComponent(axis, -i)
            return -i - 1
        } else {
            return 0
        }
    }

}