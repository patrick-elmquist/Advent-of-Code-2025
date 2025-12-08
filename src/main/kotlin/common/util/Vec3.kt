package common.util

import kotlin.math.sqrt

data class Vec3l(val x: Long, val y: Long, val z: Long) : Comparable<Vec3l> {

    constructor(xyz: List<Long>) : this(xyz[0], xyz[1], xyz[2])

    override fun compareTo(other: Vec3l): Int =
        compareValuesBy(this, other, { it.z }, { it.y }, { it.x })

    operator fun plus(point: Vec3l) = Vec3l(x + point.x, y + point.y, z + point.z)
    operator fun minus(point: Vec3l) = Vec3l(x - point.x, y - point.y, z - point.z)

    fun distanceTo(other: Vec3l): Float {
        val x = (x - other.x) * (x - other.x)
        val y = (y - other.y) * (y - other.y)
        val z = (z - other.z) * (z - other.z)
        return sqrt(x.toFloat() + y.toFloat() + z.toFloat())
    }

    companion object {
        val Zero = Vec3l(0, 0, 0)
        val Origin = Zero
    }
}

