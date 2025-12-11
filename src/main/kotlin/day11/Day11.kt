package day11

import common.Input
import common.day

// answer #1: 772
// answer #2: 423227545768872

fun main() {
    day(n = 11) {
        part1 { input ->
            val map = parseConnectionsMap(input)

            map.findPaths(start = "you", target = "out")
        }
        verify {
            expect result 772L
            run test 1 expect 5L
        }

        part2 { input ->
            val map = parseConnectionsMap(input)

            // looking at the graph, the order is svr -> fft -> dac -> out
            listOf(
                map.findPaths(start = "svr", target = "fft"),
                map.findPaths(start = "fft", target = "dac"),
                map.findPaths(start = "dac", target = "out"),
            ).reduce(Long::times)
        }
        verify {
            expect result 423227545768872L
            run test 2 expect 2L
        }
    }
}

private fun Map<String, List<String>>.findPaths(
    start: String,
    target: String,
    cache: MutableMap<String, Long> = mutableMapOf(),
): Long = cache.getOrPut(start) {
    getValue(start).sumOf { next ->
        when (next) {
            target -> 1L
            "out" -> 0L
            else -> findPaths(next, target, cache)
        }
    }
}

private fun parseConnectionsMap(input: Input): Map<String, List<String>> =
    input.lines.associate { line ->
        line.split(": ").let { (start, ends) -> start to ends.split(" ") }
    }
