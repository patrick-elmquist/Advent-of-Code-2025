package day07

import common.day
import common.grid
import common.util.Point
import common.util.arrayDequeOf

// answer #1: 1681
// answer #2: 422102272495018

fun main() {
    day(n = 7) {
        part1 { input ->
            val grid = input.grid.filter { it.value != '.' }
            val s = grid.entries.single { it.value == 'S' }.key
            val beams = arrayDequeOf(s.copy(y = s.y + 1))
            val splitters = grid.entries.sortedWith(compareBy({ it.key.x }, { it.key.y }))
            val hitSplitters = mutableSetOf(s)

            val splits = mutableSetOf<Point>()
            while (beams.isNotEmpty()) {
                val beam = beams.removeFirst()

                if (beam in hitSplitters) continue
                hitSplitters += beam

                val collision =
                    splitters.firstOrNull { it.key.x == beam.x && it.key.y > beam.y }?.key

                if (collision != null) {
                    beams += collision.copy(x = collision.x - 1)
                    beams += collision.copy(x = collision.x + 1)
                    splits += collision
                }
            }

            splits.size
        }

        verify {
            expect result 1681
            run test 1 expect 21
        }

        part2 { input ->
            val grid = input.grid
            val splitters = grid.filterValues { it == '^' }
                .keys
                .sortedWith(compareBy({ it.x }, { it.y }))
            val start = grid.entries.single { it.value == 'S' }.key

            countTimelinesRecursively(
                splitter = splitters.first { it.x == start.x },
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
    val nextLeftSplitter = splitters.findSplitterBelow(splitter, x = -1)
    val leftTimelines = if (nextLeftSplitter == null) {
        1L
    } else {
        countTimelinesRecursively(
            splitter = nextLeftSplitter,
            splitters = splitters,
            cache = cache,
        )
    }

    val nextRightSplitter = splitters.findSplitterBelow(splitter, x = 1)
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

private fun List<Point>.findSplitterBelow(p: Point, x: Int): Point? =
    firstOrNull { it.x == p.x + x && it.y > p.y }
