package day09

import common.day
import common.util.Vec2i
import kotlin.math.abs

// answer #1: 4767418746
// answer #2: 1461987144

fun main() {
    day(n = 9) {
        part1 { input ->
            val tiles = input.lines.map { line ->
                line.split(",").map { it.toInt() }.let(::Vec2i)
            }

            tiles.maxOf { p1 ->
                tiles.maxOf { p2 ->
                    calculateArea(p1, p2)
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
                val tile1 = tiles[i]
                for (j in (i + 1)..tiles.lastIndex) {
                    val tile2 = tiles[j]
                    val area = calculateArea(tile1, tile2)
                    if (area > maxArea && lines.none { line -> line.intersectsWith(Line(tile1, tile2)) }) {
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
