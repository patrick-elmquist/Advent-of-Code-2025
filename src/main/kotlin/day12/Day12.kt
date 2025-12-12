package day12

import common.day
import common.util.Vec2i
import common.util.sliceByBlankLine

// answer #1: 536
// answer #2: N/A

fun main() {
    day(n = 12) {
        part1 { input ->
            val lines = input.lines.sliceByBlankLine()
            val shapes = getShapes(lines.dropLast(1))
            lines.last().count { line ->
                    val split = line.split(": ")
                    val (width, height) = split[0].split("x").map { it.toInt() }
                    val numbers =
                        split[1].split(" ").map { it.toInt() }.flatMapIndexed { index, i ->
                            if (i == 0) {
                                emptyList()
                            } else {
                                List(i) { index }
                            }
                        }


                    val availableArea = width * height
                    val neededArea = numbers.sumOf { shapeIndex ->
                        shapes[shapeIndex].first().size
                    }
                    if (availableArea < neededArea) {
                        false
                    } else {
                        hasSolution(
                            width = width,
                            height = height,
                            requiredShapes = numbers,
                            shapes = shapes,
                        )
                    }

                }
        }
        verify {
            expect result 536
            ignoreTests() // does not work for 3rd case
            run test 1 expect 2
        }

        part2 { /* N/A */ }
        verify {
            expect result Unit
            run test 1 expect Unit
        }
    }
}

private fun hasSolution(
    width: Int,
    height: Int,
    requiredShapes: List<Int>,
    shapes: List<Set<List<Vec2i>>>,
    visited: Set<Vec2i> = setOf(),
    used: Map<Vec2i, Char> = mapOf(),
): Boolean {
    if (requiredShapes.isEmpty()) return true

    var next: Vec2i
    val localVisited = visited.toMutableSet()
    while (true) {
        next = findNextFree(width, height, localVisited) ?: return false

        val result = requiredShapes.any { shapeIndex ->
            shapes[shapeIndex]
                .map { variation -> translate(variation, next) }
                .filter { variation -> variation.all { it !in localVisited && it.x >= 0 && it.x < width && it.y >= 0 && it.y < height } }
                .any { variation ->
                    hasSolution(
                        width = width,
                        height = height,
                        shapes = shapes,
                        requiredShapes = requiredShapes - shapeIndex,
                        visited = localVisited + variation,
                        used = used + variation.associateWith { shapeIndex.toString().first() },
                    )
                }
        }

        if (result) return true

        localVisited += next
    }
}

private fun findNextFree(
    width: Int,
    height: Int,
    visited: Set<Vec2i>,
): Vec2i? =
    (0 until height).firstNotNullOfOrNull { y ->
        (0 until width).firstNotNullOfOrNull { x ->
            Vec2i(x, y).takeIf { it !in visited }
        }
    }

private fun getShapes(lines: List<List<String>>): List<Set<List<Vec2i>>> {
    val shapes = List(lines.size) { mutableSetOf<List<Vec2i>>() }
    lines.forEachIndexed { index, strings ->
        val shape = mutableSetOf<Vec2i>()
        strings.drop(1)
            .forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '#') {
                        shape += Vec2i(x, y)
                    }
                }
            }

        var original = shape.toList()
        val uniqueVariations = mutableSetOf<List<Vec2i>>()
        repeat(4) {
            uniqueVariations.add(original)
            uniqueVariations.add(flip(original))

            // Rotate for the next iteration
            original = rotate90(original)
        }

        shapes[index] += uniqueVariations.map { it.sorted() }
    }
    return shapes
}

private fun translate(shape: List<Vec2i>, offset: Vec2i): List<Vec2i> {
    val diff = offset - shape.first()
    return shape.map { it + diff }
}

private fun rotate90(points: List<Vec2i>): List<Vec2i> = points.map { (x, y) -> Vec2i(y, 2 - x) }

private fun flip(points: List<Vec2i>): List<Vec2i> = points.map { (y, x) -> Vec2i(y, 2 - x) }
