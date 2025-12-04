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
                .filter { it.value == '@' }

            grid.count { (point, _) -> grid.isRemovable(point) }
        }
        verify {
            expect result 1370
            run test 1 expect 13
        }

        part2 { input ->
            val grid = input.grid
                .filter { it.value == '@' }
                .toMutableMap()

            val countBefore = grid.size

            while (true) {
                grid.filter { (point, _) -> grid.isRemovable(point) }
                    .ifEmpty { break }
                    .let { removable -> removable.forEach { grid.remove(it.key) } }
            }

            countBefore - grid.size
        }
        verify {
            expect result 8437
            run test 1 expect 43
        }
    }
}

private fun Map<Point, Char>.isRemovable(roll: Point): Boolean =
    roll.neighbors(diagonal = true).count { this[it] != null } < 4
