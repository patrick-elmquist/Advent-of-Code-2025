package day09

import common.day
import common.util.Vec2i
import kotlin.math.abs

// answer #1: 4767418746
// answer #2: 1461987144

fun main() {
    day(n = 9) {
        part1 { input ->
            val tiles = input.lines.map(::Vec2i)
            tiles.maxOf { t1 ->
                tiles.maxOf { t2 ->
                    calculateArea(t1, t2)
                }
            }
        }
        verify {
            expect result 4767418746L
            run test 1 expect 50L
        }

        part2 { input ->
            val tiles = input.lines.map(::Vec2i)

            // add first again to complete the loop
            val lines = (tiles + tiles.first()).windowed(2).map(::Line)

            var maxArea = Long.MIN_VALUE
            for ((i, tile1) in tiles.withIndex()) {
                for (j in (i + 1)..tiles.lastIndex) {
                    val line = Line(tile1, tiles[j])
                    val area = calculateArea(line.a, line.b)
                    if (area > maxArea && lines.none { it.intersectsWith(line) }) {
                        maxArea = area
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

private data class Line(val a: Vec2i, val b: Vec2i) {
    constructor(points: List<Vec2i>) : this(points[0], points[1])

    fun intersectsWith(other: Line): Boolean {
        val (minX, maxX) = sort(a.x, b.x)
        val (minY, maxY) = sort(a.y, b.y)
        val (otherMinX, otherMaxX) = sort(other.a.x, other.b.x)
        val (otherMinY, otherMaxY) = sort(other.a.y, other.b.y)
        return otherMaxX > minX && otherMinX < maxX && otherMaxY > minY && otherMinY < maxY
    }

    private fun sort(a: Int, b: Int): Pair<Int, Int> = if (a < b) a to b else b to a
}

private fun calculateArea(p1: Vec2i, p2: Vec2i): Long {
    val w = abs(p1.x - p2.x) + 1
    val h = abs(p1.y - p2.y) + 1
    return w.toLong() * h.toLong()
}
