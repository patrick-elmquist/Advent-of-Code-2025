package day09

import common.day
import common.util.Vec2i
import kotlin.math.abs

// answer #1: 4767418746
// answer #2: 1461987144

data class Line(val a: Vec2i, val b: Vec2i) {
    override fun toString(): String = "Line(${a.x},${a.y} - ${b.x},${b.y})"
}

fun main() {
    day(n = 9) {
        part1 { input ->
            val tiles = input.lines.map { line ->
                line.split(",").map { it.toInt() }.let(::Vec2i)
            }

             tiles.maxOf { p1 ->
                tiles.maxOf { p2 ->
                    area(p1, p2)
                }
            }
        }
        verify {
            expect result 4767418746L
            run test 1 expect 50L
        }

        part2 { input ->
            val tiles = input.lines.map { line -> line.split(",").map { it.toInt() }.let(::Vec2i) }
            val lines = (tiles + tiles.first()).windowed(2) { (p1, p2) -> Line(p1, p2) }

            var maxArea = Long.MIN_VALUE
            for (i in tiles.indices) {
                val p1 = tiles[i]

                for (j in (i + 1)..tiles.lastIndex) {
                    val p2 = tiles[j]

                    val area = area(p1, p2)
                    if (area > maxArea) {
                        val intersected = lines.none { line ->
                            intersects(Line(p1, p2), line)
                        }
                        if (intersected) {
                            maxArea = area
                        }
                    }
                }
            }
            maxArea
        }
        verify {
            expect result 1461987144L
            run test 1 expect 24L
        }
    }
}

private fun intersects(line1: Line, line2: Line): Boolean {
    val (l1MinX, l1MaxX) = sort(line1.a.x, line1.b.x)
    val (l1MinY, l1MaxY) = sort(line1.a.y, line1.b.y)
    val (l2MinX, l2MaxX) = sort(line2.a.x, line2.b.x)
    val (l2MinY, l2MaxY) = sort(line2.a.y, line2.b.y)
    return l2MaxX > l1MinX && l2MinX < l1MaxX && l2MaxY > l1MinY && l2MinY < l1MaxY;
}

private fun sort(a: Int, b: Int): Pair<Int, Int> = if (a < b) a to b else b to a

private fun area(p1: Vec2i, p2: Vec2i): Long {
    val w = abs(p1.x - p2.x) + 1
    val h = abs(p1.y - p2.y) + 1
    return w.toLong() * h.toLong()
}
