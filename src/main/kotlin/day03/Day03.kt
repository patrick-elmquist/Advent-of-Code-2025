package day03

import common.day
import common.util.log
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import kotlin.math.max

// answer #1: 17193
// answer #2: 171297349921310

fun main() {
    day(n = 3) {
        part1 { input ->
            input.lines
                .sumOf { line ->
                    val map = mutableMapOf<Int, Int>()
                    for (i in 0..(line.length - 2)) {
                        val char = line[i].digitToInt()
                        map.getOrPut(char) {
                            var max = Int.MIN_VALUE
                            for (j in (i + 1) until line.length) {
                                if (line[j].digitToInt() > max) {
                                    max = line[j].digitToInt()
                                }
                            }
                            max
                        }
                    }
                    map.maxBy { it.key }.let {
                        it.key * 10L + it.value
                    }
                }
        }
        verify {
            expect result 17193L
            run test 1 expect 357L
        }

        part2 { input ->
            input.lines
                .parallelStream()
                .map { line -> rec(line) }
                .reduce(0L, Long::plus)
        }
        verify {
            expect result 171297349921310L
            run test 1 expect 3121910778619L
        }
    }
}

private fun rec(
    line: String,
    numbers: List<Long> = emptyList(),
    index: Int = 0,
    digitsRemaining: Int = 12,
): Long {
    if (digitsRemaining == 0) {
        return numbers.fold(0L) { acc, i -> acc * 10L + i }
    }

    val string = line.substring(index)
    val (index, digit) = findFirstLargestNumber(
        string,
        digitsRemaining,
    )

    return rec(
        line = string,
        numbers = numbers + digit.toLong(),
        index = index + 1,
        digitsRemaining = digitsRemaining - 1,
    )
}

private fun findFirstLargestNumber(
    string: String,
    requiredDigits: Int,
): Pair<Int, Int> {
    var largest: Pair<Int, Int> = Pair(Int.MIN_VALUE, Int.MIN_VALUE)
    val range = 0..(string.length - requiredDigits)
    for (index in range) {
        val i = string[index].digitToInt()

        when {
            i == 9 -> return index to i
            i > largest.second -> largest = index to i
        }
    }
    if (largest.second == Int.MIN_VALUE) error("")
    return largest
}
