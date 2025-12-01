package day01

import common.day

// answer #1: 1089
// answer #2: 6530

fun main() {
    day(n = 1) {
        part1 { input ->
            var pos = 50
            input.lines.count { line ->
                val (dir, dist) = line.toDirectionAndDistance()
                pos += dir * dist
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
                val (dir, dist) = line.toDirectionAndDistance()
                (1..dist).count {
                    pos += dir
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

private fun String.toDirectionAndDistance(): Pair<Int, Int> =
    Pair(
        first().let { if (it == 'L') -1 else 1 },
        drop(1).toInt(),
    )
