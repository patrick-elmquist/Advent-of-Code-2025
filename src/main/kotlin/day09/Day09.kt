package day09

import common.day
import common.util.Vec2i
import common.util.log
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// answer #1: 4767418746
// answer #2:

data class Line(val p1: Vec2i, val p2: Vec2i)

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
            val points = input.lines.map { line ->
                line.split(",").map { it.toInt() }.let(::Vec2i)
            }.associateWith { '#' }.toMutableMap()

            val groupedX = points.keys.groupBy { it.x }
            val groupedY = points.keys.groupBy { it.y }

            val start = points.keys.minOf { it }

            val rowRanges = mutableMapOf<Int, MutableList<IntRange>>()
            val colRanges = mutableMapOf<Int, MutableList<IntRange>>()

            val border = mutableSetOf<Vec2i>()
            var current = start
            var nextCoord = Coord.X
            do {
                val next = when (nextCoord) {
                    Coord.X -> {
                        val next = groupedY.getValue(current.y).single { it != current }

                        (min(current.x, next.x)..max(current.x, next.x)).forEach {
                            border += Vec2i(it, next.y)
                        }
                        rowRanges.getOrPut(current.y) { mutableListOf() } += if (current.x < next.x) {
                            (current.x..next.x)
                        } else {
                            next.x..current.x
                        }
                        next
                    }

                    Coord.Y -> {
                        val next = groupedX.getValue(current.x).single { it != current }
                        colRanges.getOrPut(current.x) { mutableListOf() } += if (current.y < next.y) {
                            (current.y..next.y)
                        } else {
                            next.y..current.y
                        }
                        (min(current.y, next.y)..max(current.y, next.y)).forEach {
                            border += Vec2i(next.x, it)
                        }
                        next
                    }
                }

//                println("cur:$current next:$next")
                nextCoord = when (nextCoord) {
                    Coord.X -> Coord.Y
                    Coord.Y -> Coord.X
                }

                current = next

            } while (current != start)

            val allPossibleRects = points.keys
                .flatMap { p1 ->
                    points.keys.mapNotNull { p2 ->
                        if (p1 == p2) {
                            null
                        } else {
                            rect(p1, p2)
                        }
                    }
                }

//            val test = allPossibleRects.sortedByDescending { it.area() }
            val test = listOf(rect(Vec2i(7, 3), Vec2i(11, 1)))
                .first { rect ->
                    rect.log()
                    val allLines = rect.lines

                    val horSides = allLines

                    val colIntersect = colRanges.any { (x, range) ->
                        val line = Line(Vec2i(x, range.first().first), Vec2i(x, range.first().last))
                        horSides.any { side ->
                            intersects(line, side).also {
                                println("cols intersect:$line $side $it")
                            }
                        }
                    }
                    if (colIntersect) return@first false


                    val verSides = allLines
                    val rowIntersect = rowRanges.any { (y, range) ->
                        val line = Line(Vec2i(range.first().first, y), Vec2i(range.first().last, y))
                        verSides.any { side ->
                            intersects(line, side).also {
                                println("rows intersect:$line $side $it")
                            }
                        }
                    }

                    if (rowIntersect) false else true
                }


            println("test:$test ${test.area()}")
            println("rows")
            rowRanges.forEach { (i, ranges) -> println("$i $ranges") }
            println("cols")
            colRanges.forEach { (i, ranges) -> println("$i $ranges") }
        }
        verify {
            expect result null
            run test 1 expect 24L
        }
    }
}

private fun intersects(l1: Line, l2: Line): Boolean {
    if (true) return linesIntersect(l1, l2)
    val (l1MinX, l1maxX) = minMax(l1.p1.x, l1.p2.x)
    val (l1MinY, l1maxY) = minMax(l1.p1.y, l1.p2.y)

    val (l2MinX, l2maxX) = minMax(l2.p1.x, l2.p2.x)
    val (l2MinY, l2maxY) = minMax(l2.p1.y, l2.p2.y)

    return l2maxX > l1MinX && l2MinX < l1maxX && l2maxY > l1MinY && l2MinY < l1maxY;
}

private fun minMax(a: Int, b: Int): Pair<Int, Int> {
    return if (a < b) a to b else b to a
}

fun linesIntersect(l1: Line, l2: Line): Boolean {
    return linesIntersect(l1.p1, l1.p2, l2.p1, l2.p2)
}

fun linesIntersect(b1: Vec2i, b2: Vec2i, p3: Vec2i, p4: Vec2i): Boolean {
    val line1Horizontal = b1.y == b2.y
    val line2Horizontal = p3.y == p4.y

    // Both horizontal - check if they overlap on same y
    if (line1Horizontal && line2Horizontal) {
        if (b1.y != p3.y) return false
        val minB = min(b1.x, b2.x)
        val maxB = max(b1.x, b2.x)

        val minL = min(p3.x, p4.x)
        val maxL = max(p3.x, p4.x)

        println("horizontal")
        return !(minB < maxL && minL < maxB)
    }

    // Both vertical - check if they overlap on same x
    if (!line1Horizontal && !line2Horizontal) {
        if (b1.x != p3.x) return false
        println("vertical")
        return !(min(b1.y, b2.y) < max(p3.y, p4.y) && min(p3.y, p4.y) < max(b1.y, b2.y))
    }

    // One horizontal, one vertical - check intersection point
    val (hStart, hEnd, vStart, vEnd) = if (line1Horizontal) {
        listOf(b1, b2, p3, p4)
    } else {
        listOf(p3, p4, b1, b2)
    }

    val hY = hStart.y
    val hXRange = min(hStart.x, hEnd.x)..max(hStart.x, hEnd.x)
    val vX = vStart.x
    val vYRange = min(vStart.y, vEnd.y)..max(vStart.y, vEnd.y)

    return vX in hXRange && hY in vYRange
}

enum class Coord { X, Y }
enum class Dir { Up, Right, Down, Left }

data class Rect(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val leftLine = Line(Vec2i(left, top), Vec2i(left, bottom))
    val topLine = Line(Vec2i(left, top), Vec2i(right, top))
    val rightLine = Line(Vec2i(right, top), Vec2i(right, bottom))
    val bottomLine = Line(Vec2i(left, bottom), Vec2i(right, bottom))

    val lines = listOf(leftLine, topLine, rightLine, bottomLine)
    fun area(): Long {
        return (right - left + 1).toLong() * (bottom - top + 1).toLong()
    }
}

fun rect(p1: Vec2i, p2: Vec2i): Rect = Rect(
    left = min(p1.x, p2.x),
    top = min(p1.y, p2.y),
    right = max(p1.x, p2.x),
    bottom = max(p1.y, p2.y),
)

/**
 *if (RectA.Left < RectB.Right && RectA.Right > RectB.Left &&
 *      RectA.Top > RectB.Bottom && RectA.Bottom < RectB.Top )
 */
fun intersects(a: Rect, b: Rect): Boolean {
    return a.left < b.right && a.right > b.left &&
            a.top > b.bottom && a.bottom < b.top
}






















