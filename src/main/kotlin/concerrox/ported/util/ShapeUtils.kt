package concerrox.ported.util

import com.google.common.collect.Maps
import com.mojang.math.OctahedralGroup
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.*
import java.util.Map

object ShapeUtils {

    private val BLOCK_CENTER = Vec3(0.5, 0.5, 0.5)

    fun rotateHorizontal(shape: VoxelShape): MutableMap<Direction, VoxelShape> {
        return rotateHorizontal(shape, OctahedralGroup.IDENTITY, BLOCK_CENTER)
    }

    fun rotateHorizontal(
        shape: VoxelShape, rotation: OctahedralGroup, origin: Vec3
    ): MutableMap<Direction, VoxelShape> {
        return Maps.newEnumMap(
            Map.of(
                Direction.NORTH,
                rotate(shape, rotation),
                Direction.EAST,
                rotate(shape, OctahedralGroup.ROT_90_Y_NEG.compose(rotation), origin),
                Direction.SOUTH,
                rotate(shape, OctahedralGroup.ROT_180_FACE_XZ.compose(rotation), origin),
                Direction.WEST,
                rotate(shape, OctahedralGroup.ROT_90_Y_POS.compose(rotation), origin)
            )
        )
    }

    fun rotate(shape: VoxelShape, octohedralGroup: OctahedralGroup): VoxelShape {
        return rotate(shape, octohedralGroup, BLOCK_CENTER)
    }

    fun rotate(shape: VoxelShape, octohedralGroup: OctahedralGroup, pos: Vec3): VoxelShape {
        if (octohedralGroup == OctahedralGroup.IDENTITY) {
            return shape
        } else {
            val discreteVoxelShape: DiscreteVoxelShape = shape.shape.rotate(octohedralGroup)
            if (shape is CubeVoxelShape && BLOCK_CENTER == pos) {
                return CubeVoxelShape(discreteVoxelShape)
            } else {
                val directionAxis1 = octohedralGroup.permutation.permuteAxis(Direction.Axis.X)
                val directionAxis2 = octohedralGroup.permutation.permuteAxis(Direction.Axis.Y)
                val directionAxis3 = octohedralGroup.permutation.permuteAxis(Direction.Axis.Z)
                val doubleList = shape.getCoords(directionAxis1)
                val doubleList1 = shape.getCoords(directionAxis2)
                val doubleList2 = shape.getCoords(directionAxis3)
                val flag = octohedralGroup.inverts(Direction.Axis.X)
                val flag1 = octohedralGroup.inverts(Direction.Axis.Y)
                val flag2 = octohedralGroup.inverts(Direction.Axis.Z)
                return ArrayVoxelShape(
                    discreteVoxelShape,
                    flipAxisIfNeeded(doubleList, flag, pos.get(directionAxis1), pos.x),
                    flipAxisIfNeeded(doubleList1, flag1, pos.get(directionAxis2), pos.y),
                    flipAxisIfNeeded(doubleList2, flag2, pos.get(directionAxis3), pos.z)
                )
            }
        }
    }

    fun flipAxisIfNeeded(coords: DoubleList, flip: Boolean, originalOrigin: Double, newOrigin: Double): DoubleList {
        if (!flip && originalOrigin == newOrigin) {
            return coords
        } else {
            val i = coords.size
            val doubleList = DoubleArrayList(i)
            if (flip) {
                for (j in i - 1 downTo 0) {
                    doubleList.add(-(coords.getDouble(j) - originalOrigin) + newOrigin)
                }
            } else {
                var k = 0
                while (k in 0..<i) {
                    doubleList.add(coords.getDouble(k) - originalOrigin + newOrigin)
                    ++k
                }
            }
            return doubleList
        }
    }


}