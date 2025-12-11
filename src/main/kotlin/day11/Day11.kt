package day11

import common.day

// answer #1:
// answer #2:

fun main() {
    day(n = 11) {
        part1 { input ->
            val map = mutableMapOf<String, List<String>>()
            input.lines.forEach { line ->
                val (start, ends) = line.split(": ").let { (a, b) -> a to b.split(" ") }
                map[start] = ends
            }
            println(map)

            val me = map["you"]
            println(me)
            findPaths(start = "you", map = map)
        }
        verify {
            expect result null
            run test 1 expect 5L
        }

        part2 { input ->

        }
        verify {
            expect result null
            run test 1 expect Unit
        }
    }
}

private fun findPaths(
    start: String,
    map: Map<String, List<String>>,
    cache: MutableMap<String, Long> = mutableMapOf(),
): Long {
    return cache.getOrPut(start) {
        map.getValue(start).sumOf { end ->
            if (end == "out") {
                1L
            } else {
                findPaths(end, map, cache)
            }
        }
    }
}
