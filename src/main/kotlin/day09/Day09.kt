package day09

import common.day
import common.util.Vec2i
import kotlin.math.abs
import kotlin.math.max

// answer #1: 4767418746
// answer #2:

data class Line(val a: Vec2i, val b: Vec2i) {
    override fun toString(): String = "Line(${a.x},${a.y} - ${b.x},${b.y})"
}

fun main() {
    day(n = 9) {
        part1 { input ->
            val points = input.lines.map { line ->
                line.split(",").map { it.toInt() }.let(::Vec2i)
            }

            points.maxOf { p1 ->
                points.maxOf { p2 ->
                    val w = abs(p1.x - p2.x) + 1
                    val h = abs(p1.y - p2.y) + 1
                    w.toLong() * h.toLong()
                }
            }
        }
        verify {
            expect result 4767418746L
            run test 1 expect 50L
        }

        part2 { input ->
            val points = input.lines.map { line -> line.split(",").map { it.toInt() }.let(::Vec2i) }
            val lines = (points + points.first()).windowed(2) { (p1, p2) -> Line(p1, p2) }

            var maxArea = Long.MIN_VALUE
            for (i in points.indices) {
                val p1 = points[i]
                ((i + 1)..points.lastIndex).forEach { j ->
                    val p2 = points[j]

                    val area = (abs(p2.x - p1.x).toLong() + 1) * (abs(p2.y - p1.y).toLong()+1)
//                    println("$p1  $p2  $area ($maxArea)")
                    if (area > maxArea) {
                        val intersected = lines.any { line ->
                            intersects(Line(p1, p2), line).also {
                                if (it) {
//                                    println("$p1  $p2  $line intersected")
                                } else {
//                                    println("not intersecting")
                                }
                            }
                        }
                        if (intersected) {
//                            println("ignoring area")
                        } else {
                            maxArea = area
                        }
                    }
                }
            }
            println(points)
            println(lines)
            println(maxArea)

            maxArea

        }
        verify {
            expect result null
            run test 1 expect 24L
        }
    }
}

private fun intersects(l1: Line, l2: Line): Boolean {
    val (l1mix, l1max) = minMax(l1.a.x, l1.b.x)
    val (l1miy, l1may) = minMax(l1.a.y, l1.b.y)

    val (l2mix, l2max) = minMax(l2.a.x, l2.b.x)
    val (l2miy, l2may) = minMax(l2.a.y, l2.b.y)

    return l2max > l1mix && l2mix < l1max && l2may > l1miy && l2miy < l1may;
}

private fun minMax(a: Int, b: Int): Pair<Int, Int> {
    return if (a < b) a to b else b to a
}

