package day07

import common.Input
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
            val grid = input.grid.filter { it.value != '.' }
            val s = grid.entries.single { it.value == 'S' }.key
            val splitters =
                grid.filterValues { it != 'S' }.keys.sortedWith(compareBy({ it.x }, { it.y }))
            val start = splitters.first { it.x == s.x }

            rec(
                splitter = start,
                cache = mutableMapOf(),
                splitters = splitters,
            )
        }
        verify {
            expect result 422102272495018L
            run test 1 expect 40L
        }
    }
}

private fun rec(
    splitter: Point,
    cache: MutableMap<Point, Long>,
    splitters: List<Point>,
): Long {
    return cache.getOrPut(splitter) {
        val left = splitters.firstOrNull { it.x == splitter.x - 1 && it.y > splitter.y }
        val leftTimelines = if (left == null) {
            1L
        } else {
            rec(
                splitter = left,
                cache = cache,
                splitters = splitters,
            )
        }


        val right = splitters.firstOrNull { it.x == splitter.x + 1 && it.y > splitter.y }
        val rightTimelines = if (right == null) {
            1L
        } else {
            rec(
                splitter = right,
                cache = cache,
                splitters = splitters,
            )
        }

        leftTimelines + rightTimelines
    }
}
