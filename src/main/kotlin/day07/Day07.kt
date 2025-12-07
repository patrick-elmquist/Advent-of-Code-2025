package day07

import common.Input
import common.day
import common.grid
import common.util.Point

// answer #1: 1681
// answer #2: 422102272495018

fun main() {
    day(n = 7) {
        part1 { input ->
            val splitters = parseSplitters(input)
            val queue = mutableListOf(splitters.minBy { it.y })

            val visitedSplitters = mutableSetOf<Point>()
            val offsets = listOf(-1, 1)
            while (queue.isNotEmpty()) {
                val splitter = queue.removeFirst()

                if (splitter in visitedSplitters) continue
                visitedSplitters += splitter

                queue += offsets.mapNotNull { splitters.findSplitterBelow(splitter, it) }
            }

            visitedSplitters.size
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

private fun parseSplitters(input: Input): List<Point> =
    input.grid
        .filterValues { it == '^' }
        .keys
        .toList()

private fun countTimelinesRecursively(
    splitter: Point,
    splitters: List<Point>,
    cache: MutableMap<Point, Long> = mutableMapOf(),
): Long = cache.getOrPut(splitter) {
    val nextLeftSplitter = splitters.findSplitterBelow(splitter, offset = -1)
    val leftTimelines = if (nextLeftSplitter == null) {
        1L
    } else {
        countTimelinesRecursively(
            splitter = nextLeftSplitter,
            splitters = splitters,
            cache = cache,
        )
    }

    val nextRightSplitter = splitters.findSplitterBelow(splitter, offset = 1)
    val rightTimelines = if (nextRightSplitter == null) {
        1L
    } else {
        countTimelinesRecursively(
            splitter = nextRightSplitter,
            splitters = splitters,
            cache = cache,
        )
    }

    leftTimelines + rightTimelines
}

private fun List<Point>.findSplitterBelow(p: Point, offset: Int): Point? =
    firstOrNull { it.x == p.x + offset && it.y > p.y }
