package day04

import common.day
import common.grid
import common.util.neighbors

// answer #1: 1370
// answer #2: 8437

fun main() {
    day(n = 4) {
        part1 { input ->
            val grid = input.grid
                .filter { it.value == '@' }

            grid.count { (point, _) ->
                val n = point.neighbors(diagonal = true)
                n.count { grid[it] != null } < 4
            }
        }
        verify {
            expect result 1370
            run test 1 expect 13
        }

        part2 { input ->
            val grid = input.grid
                .filter { it.value == '@' }
                .toMutableMap()

            val before = grid.size

            while (true) {
                val removable = grid.filter { (point, _) ->
                    val n = point.neighbors(diagonal = true)
                    n.count { grid[it] != null } < 4
                }
                if (removable.isEmpty()) {
                    break
                } else {
                    removable.forEach { grid.remove(it.key) }
                }
            }

            before - grid.size
        }
        verify {
            expect result 8437
            run test 1 expect 43
        }
    }
}
