package day11

import common.Input
import common.day

// answer #1: 772
// answer #2: 423227545768872

fun main() {
    day(n = 11) {
        part1 { input ->
            val connectionMap = parseMap(input)

            connectionMap.findPaths(start = "you", target = "out")
        }
        verify {
            expect result 772L
            run test 1 expect 5L
        }

        part2 { input ->
            val connectionMap = parseMap(input)

            // looking at the graph, the order is svr -> fft -> dac -> out
            val pathsFromSvrToFft = connectionMap.findPaths(start = "svr", target = "fft")
            val pathsFromFftToDac = connectionMap.findPaths(start = "fft", target = "dac")
            val pathsFromDacToOut = connectionMap.findPaths(start = "dac", target = "out")

            pathsFromSvrToFft * pathsFromFftToDac * pathsFromDacToOut
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
            else -> this.findPaths(next, target, cache)
        }
    }
}

private fun parseMap(input: Input): Map<String, List<String>> =
    input.lines.associate { line ->
        line.split(": ").let { (start, ends) -> start to ends.split(" ") }
    }
