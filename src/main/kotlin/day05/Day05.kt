package day05

import common.Input
import common.day
import common.util.sliceByBlankLine
import kotlin.math.max
import kotlin.math.min

// answer #1: 635
// answer #2: 369761800782619

fun main() {
    day(n = 5) {
        part1 { input ->
            val (ranges, ids) = input.parseRangesAndIds()
            ids.count { id -> ranges.any { range -> id in range } }
        }
        verify {
            expect result 635
            run test 1 expect 3
        }

        part2 { input ->
            val (ranges, _) = input.parseRangesAndIds()
            mergeRanges(ranges).sumOf { range -> range.last - range.first + 1 }
        }
        verify {
            expect result 369761800782619L
            run test 1 expect 14L
        }
    }
}

private fun mergeRanges(ranges: List<LongRange>): List<LongRange> =
    buildList {
        val queue = ranges.toMutableList()
        while (queue.isNotEmpty()) {
            val range = queue.removeLast()
            val index = queue.indexOfFirst { range.overlaps(it) }
            if (index > -1) {
                val overlappingRange = queue.removeAt(index)
                val mergedRange = range.merge(overlappingRange)
                queue.add(mergedRange)
            } else {
                add(range)
            }
        }
    }

private fun LongRange.overlaps(other: LongRange): Boolean =
    first <= other.last && other.first <= last

private fun LongRange.merge(other: LongRange): LongRange =
    min(first, other.first)..max(last, other.last)

private fun Input.parseRangesAndIds(): Pair<List<LongRange>, List<Long>> =
    lines.sliceByBlankLine()
        .let { (r, i) ->
            val ranges = r.map { range ->
                range.split("-")
                    .map(String::toLong)
                    .let { (start, end) -> start..end }
            }
            ranges to i.map(String::toLong)
        }
