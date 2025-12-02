package day02

import common.Input
import common.day
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.text.split

// answer #1: 13919717792
// answer #2: 14582313461

fun main() {
    day(n = 2) {
        part1 { input ->
            input.toRanges().sumOf { range ->
                range
                    .sumOf { number ->
                        val string = number.toString()
                        when {
                            string.length % 2 != 0 -> 0L
                            string.isRepeating(string.length / 2) -> number
                            else -> 0L
                        }
                    }
            }
        }
        verify {
            expect result 13919717792L
            run test 1 expect 1227775554L
        }

        part2 { input ->
            input.toRanges().sumOf { range ->
                range.sumOf { number ->
                    if (hasRepeatingChunk(number)) number else 0L
                }
            }
        }
        verify {
            expect result 14582313461
            run test 1 expect 4174379265L
        }
    }
}

private fun hasRepeatingChunk(number: Long): Boolean {
    val string = number.toString()
    var n = string.length / 2
    while (n > 0) {
        if (string.length % n == 0 && string.isRepeating(n)) {
            return true
        }
        n--
    }
    return false
}

private fun String.isRepeating(chunkSize: Int): Boolean =
    chunked(chunkSize).distinct().size == 1

private fun Input.toRanges(): Sequence<LongRange> = lines.first()
    .splitToSequence(",")
    .map { it.split("-") }
    .map { (start, end) -> start.toLong()..end.toLong() }
