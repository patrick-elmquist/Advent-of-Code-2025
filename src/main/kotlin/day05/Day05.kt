package day05

import common.day
import common.util.sliceByBlank
import kotlin.math.max
import kotlin.math.min

// answer #1: 635
// answer #2: 369761800782619

fun main() {
    day(n = 5) {
        part1 { input ->
            val (ranges, ids) = input.lines.sliceByBlank()
                .let { (ranges, ids) ->
                    ranges.map {
                        it.split("-").let { (start, end) ->
                            start.toLong()..end.toLong()
                        }
                    } to ids.map { it.toLong() }
                }

            ids.count { id -> ranges.any { id in it } }

        }
        verify {
            expect result 635
            run test 1 expect 3
        }

        part2 { input ->
            val (ranges, ids) = input.lines.sliceByBlank()
                .let { (ranges, ids) ->
                    ranges.map {
                        it.split("-").let { (start, end) ->
                            start.toLong()..end.toLong()
                        }
                    } to ids.map { it.toLong() }
                }

            val visited = mutableListOf<LongRange>()
            val queue = ranges.toMutableList()
            while (queue.isNotEmpty()) {
                val range = queue.removeLast()
                val start = range.start
                val end = range.endInclusive

                val match = queue.firstOrNull { start in it || end in it || (it.start in range && it.endInclusive in range) }
                if (match != null) {
                    queue.remove(match)
                    queue.add(merge(range, match))
                } else {
                    visited.add(range)
                }
            }

            visited.sumOf {
                it.endInclusive - it.start + 1
            }
        }
        verify {
            expect result 369761800782619L
            run test 1 expect 14L
        }
    }
}

private fun merge(a: LongRange, b: LongRange): LongRange {
    return (min(a.first, b.first)..max(a.endInclusive, b.endInclusive))
        .also { println("merging a:$a and b:$b into $it") }
}
