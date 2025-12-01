package day01

import common.day

// answer #1: 1089
// answer #2: 6530

fun main() {
    day(n = 1) {
        part1 { input ->
            var pos = 50
            input.lines.count { line ->
                val (direction, distance) = parseDirectionAndDistance(line)
                pos += direction * distance
                pos %= 100
                pos == 0
            }
        }
        verify {
            expect result 1089
            run test 1 expect 3
        }

        part2 { input ->
            var pos = 50
            input.lines.sumOf { line ->
                val (direction, distance) = parseDirectionAndDistance(line)
                (1..distance).count {
                    pos += direction
                    pos %= 100
                    pos == 0
                }
            }
        }
        verify {
            expect result 6530
            run test 1 expect 6
        }
    }
}

private fun parseDirectionAndDistance(line: String): Pair<Int, Int> =
    Pair(
        line.first().let {
            when (it) {
                'L' -> -1
                else -> 1
            }
        },
        line.drop(1).toInt(),
    )
