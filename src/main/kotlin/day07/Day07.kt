package day07

import common.Input
import common.day
import common.grid
import common.util.Point

// answer #1: 1681
// answer #2: 422102272495018

private val offsets = listOf(-1, 1)

fun main() {
    day(n = 7) {
        part1 { input ->
            val splitters = parseSplitters(input)
            val queue = mutableListOf(splitters.minBy { it.y })

            val visited = mutableSetOf<Point>()
            while (queue.isNotEmpty()) {
                val splitter = queue.removeFirst()

                if (splitter in visited) continue
                visited += splitter

                queue += offsets.mapNotNull { offset ->
                    splitters.findSplitterBelow(splitter, offset)
                }
            }

            visited.size
        }

        verify {
            expect result 1681
            run test 1 expect 21
        }

        part2 { input ->
            val splitters = parseSplitters(input)

            countTimelinesRecursively(
                splitter = splitters.minBy { it.y },
                splitters = splitters,
            )
        }
        verify {
            expect result 422102272495018L
            run test 1 expect 40L
        }
    }
}

private fun countTimelinesRecursively(
    splitter: Point,
    splitters: List<Point>,
    cache: MutableMap<Point, Long> = mutableMapOf(),
): Long = cache.getOrPut(splitter) {
    offsets
        .map { offset -> splitters.findSplitterBelow(splitter, offset) }
        .sumOf { nextSplitter ->
            if (nextSplitter == null) {
                1L
            } else {
                countTimelinesRecursively(
                    splitter = nextSplitter,
                    splitters = splitters,
                    cache = cache,
                )
            }
        }
}

private fun List<Point>.findSplitterBelow(splitter: Point, offset: Int): Point? =
    firstOrNull { it.x == splitter.x + offset && it.y > splitter.y }

private fun parseSplitters(input: Input): List<Point> =
    input.grid
        .filterValues { it == '^' }
        .keys
        .toList()
