package day11

import common.day

// answer #1: 772
// answer #2: 423227545768872

fun main() {
    day(n = 11) {
        part1 { input ->
            val map = mutableMapOf<String, List<String>>()
            input.lines.forEach { line ->
                val (start, ends) = line.split(": ").let { (a, b) -> a to b.split(" ") }
                map[start] = ends
            }

            findPaths(start = "you", target = "out", map = map)
        }
        verify {
            expect result 772L
            run test 1 expect 5L
        }

        part2 { input ->
            val map = mutableMapOf<String, List<String>>()
            input.lines.forEach { line ->
                val (start, ends) = line.split(": ").let { (a, b) -> a to b.split(" ") }
                map[start] = ends
            }
            // looking at the graph, the order is svr -> fft -> dac -> out
            val pathsFromSvrToFft = findPaths(start = "svr", target = "fft", map = map)
            val pathsFromFftToDac = findPaths(start = "fft", target = "dac", map = map)
            val pathsFromDacToOut = findPaths(start = "dac", target = "out", map = map)
            pathsFromSvrToFft * pathsFromFftToDac * pathsFromDacToOut
        }
        verify {
            expect result 423227545768872L
            run test 2 expect 2L
        }
    }
}

private fun findPaths(
    start: String,
    target: String,
    map: Map<String, List<String>>,
    cache: MutableMap<String, Long> = mutableMapOf(),
): Long {
    return cache.getOrPut(start) {
        map.getValue(start).sumOf { end ->
            if (end == target) {
                1L
            } else {
                findPaths(end, target, map, cache)
            }
        }
    }
}
