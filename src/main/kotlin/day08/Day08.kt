package day08

import common.day
import common.util.Point
import kotlin.math.sqrt

// answer #1: 123234
// answer #2: 9259958565

fun main() {
    day(n = 8) {
        part1 { input ->
            val lines = input.lines
            val count = lines.first().toInt()
            val points = lines.drop(1).map {
                it.split(',').map { it.toLong() }.let { (x, y, z) -> Vec3l(x, y, z) }
            }

            val distances = mutableMapOf<List<Vec3l>, Float>()
            points.forEach { p1 ->
                points.forEach { p2 ->
                    if (p1 != p2) distances[listOf(p1, p2).sorted()] = p1.distanceTo(p2)
                }
            }
            val sorted = distances.entries.sortedBy { it.value }.map { it.key }.toMutableList()
//            println(sorted)

            val circuits = mutableListOf<MutableSet<Vec3l>>()
            var n = 0
            while (n < count) {
                val (p1, p2) = sorted.removeFirst()

                val p1Circuit = circuits.firstOrNull { p1 in it }
                val p2Circuit = circuits.firstOrNull { p2 in it }

                when {
                    p1Circuit != null && p2Circuit != null && p1Circuit == p2Circuit -> Unit

                    p1Circuit == null && p2Circuit == null -> {
                        circuits += mutableSetOf(p1, p2)
                    }

                    p1Circuit != null && p2Circuit != null -> {
                        circuits.remove(p2Circuit)
                        p1Circuit.addAll(p2Circuit)
                    }

                    p1Circuit != null -> {
                        p1Circuit += p2
                    }

                    p2Circuit != null -> {
                        p2Circuit += p1
                    }

                    else -> error("wtf")
                }
                n++
            }

            circuits.map { it.size.toLong() }.sortedDescending().take(3).reduce(Long::times)
        }
        verify {
            expect result 123234L
            run test 1 expect 40L
        }

        part2 { input ->
            val lines = input.lines
            val count = lines.first().toInt()
            val points = lines.drop(1).map {
                it.split(',').map { it.toLong() }.let { (x, y, z) -> Vec3l(x, y, z) }
            }

            val distances = mutableMapOf<List<Vec3l>, Float>()
            points.forEach { p1 ->
                points.forEach { p2 ->
                    if (p1 != p2) distances[listOf(p1, p2).sorted()] = p1.distanceTo(p2)
                }
            }
            val sorted = distances.entries.sortedBy { it.value }.map { it.key }.toMutableList()
//            println(sorted)

            val circuits = mutableListOf<MutableSet<Vec3l>>()
            points.forEach {
                circuits += mutableSetOf(it)
            }

            var n = 0
            var last: List<Vec3l>? = null
            while (circuits.size != 1) {
                val pair = sorted.removeFirst()
                last = pair
                val (p1, p2) = pair

                val p1Circuit = circuits.firstOrNull { p1 in it }
                val p2Circuit = circuits.firstOrNull { p2 in it }

                when {
                    p1Circuit != null && p2Circuit != null && p1Circuit == p2Circuit -> Unit

                    p1Circuit == null && p2Circuit == null -> {
                        circuits += mutableSetOf(p1, p2)
                    }

                    p1Circuit != null && p2Circuit != null -> {
                        circuits.remove(p2Circuit)
                        p1Circuit.addAll(p2Circuit)
                    }

                    p1Circuit != null -> {
                        p1Circuit += p2
                    }

                    p2Circuit != null -> {
                        p2Circuit += p1
                    }

                    else -> error("wtf")
                }

                n++
            }

            last!!.let { (p1, p2) -> p1.x * p2.x }
        }
        verify {
            expect result 9259958565L
            run test 1 expect 25272L
        }
    }
}

private data class Vec3l(val x: Long, val y: Long, val z: Long) : Comparable<Vec3l> {
    override fun compareTo(other: Vec3l): Int {
        val z = z.compareTo(other.z)
        if (z != 0) {
            return z
        }

        val y = y.compareTo(other.y)
        if (y != 0) {
            return y
        }

        return x.compareTo(other.x)
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }
}

private fun Vec3l.distanceTo(other: Vec3l): Float {
    val x = (x - other.x) * (x - other.x)
    val y = (y - other.y) * (y - other.y)
    val z = (z - other.z) * (z - other.z)
    return sqrt(x.toFloat() + y.toFloat() + z.toFloat())
}
