package day12

import common.day
import common.util.Vec2i
import common.util.out
import common.util.sliceByBlankLine

// answer #1:
// answer #2:

fun main() {
    day(n = 12) {
        part1 { input ->
            val lines = input.lines.sliceByBlankLine()
            val shapes = getShapes(lines.dropLast(1))
            lines.last().count {
                val split = it.split(": ")
                val (width, height) = split[0].split("x").map { it.toInt() }
                val numbers = split[1].split(" ").map { it.toInt() }
                hasSolution(
                    width = width,
                    height = height,
                    shapes = numbers,
                    allShapes = shapes,
                )
            }
        }
        verify {
            expect result null
            run test 1 expect 2
        }

        part2 { input ->

        }
        verify {
            expect result null
            run test 1 expect Unit
        }
    }
}

private fun hasSolution(
    width: Int,
    height: Int,
    shapes: List<Int>,
    allShapes: List<Set<Set<Vec2i>>>,
): Boolean {





    return TODO()
}

private fun getShapes(lines: List<List<String>>): List<Set<Set<Vec2i>>> {
    val shapes = List(lines.drop(1).size) { mutableSetOf<Set<Vec2i>>() }
    lines.forEachIndexed { index, strings ->
        var shape = mutableSetOf<Vec2i>()
        strings.drop(1)
            .forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '#') {
                        shape += Vec2i(x, y)
                    }
                }
            }

        val uniqueVariations = mutableSetOf<Set<Vec2i>>()
        repeat(4) {
            uniqueVariations.add(shape.toSet())
            uniqueVariations.add(flip(shape).toSet())

            // Rotate for the next iteration
            shape = rotate90(shape).toMutableSet()
        }

        shapes[index] += uniqueVariations
    }
    return shapes
}

private fun rotate90(points: Set<Vec2i>): Set<Vec2i> {
    return points.map { (x, y) -> Vec2i(y, 2 - x) }.toSet()
}

private fun flip(points: Set<Vec2i>): Set<Vec2i> {
    return points.map { (y, x) -> Vec2i(y, 2 - x) }.toSet()
}
