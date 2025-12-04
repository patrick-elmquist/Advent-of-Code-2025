package day04

import common.day
import common.grid
import common.util.Point
import common.util.neighbors

// answer #1: 1370
// answer #2: 8437

fun main() {
    day(n = 4) {
        part1 { input ->
            val grid = input.grid
                .filterValues { it == '@' }

            grid.count { (xy, _) -> grid.isRemovable(xy) }
        }
        verify {
            expect result 1370
            run test 1 expect 13
        }

        part2 { input ->
            val grid = input.grid
                .filterValues { it == '@' }
                .toMutableMap()

            val initialRollCount = grid.size

            while (true) {
                grid.filterKeys { xy -> grid.isRemovable(xy) }
                    .ifEmpty { break }
                    .also { removable -> grid -= removable.keys }
            }

            initialRollCount - grid.size
        }
        verify {
            expect result 8437
            run test 1 expect 43
        }
    }
}

private fun Map<Point, Char>.isRemovable(xy: Point): Boolean =
    xy.neighbors(diagonal = true).count { it in this } < 4
