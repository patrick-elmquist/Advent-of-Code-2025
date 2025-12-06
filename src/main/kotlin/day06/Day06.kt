package day06

import common.day

// answer #1: 6295830249262
// answer #2: 9194682052782

private val whitespaceRegex = "\\s+".toRegex()

fun main() {
    day(n = 6) {
        part1 { input ->
            val trimmed = input.lines.map { it.trim().split(whitespaceRegex).map { it.trim() } }
            val operators = trimmed.last()
            val lists = List(operators.size) { mutableListOf<Long>() }
            trimmed.dropLast(1)
                .forEach { line ->
                    line.forEachIndexed { i, number ->
                        lists[i].add(number.toLong())
                    }
                }

            lists
                .mapIndexed { index, numbers ->
                    numbers.reduce(operators[index].first())
                }
                .sum()
        }
        verify {
            expect result 6295830249262L
            run test 1 expect 4277556L
        }

        part2 { input ->
            val lines = input.lines.map { "$it " }
            var operator: Char? = null
            val numbers = mutableListOf<Long>()
            lines.first().indices.sumOf { index ->
                val column = lines.map { it[index] }
                if (column.all(Char::isWhitespace)) {
                    numbers.reduce(operator!!)
                } else {
                    if (column.last() in setOf('*', '+')) {
                        operator = column.last()
                        numbers.clear()
                    }
                    numbers.add(column.dropLast(1).joinToString("").trim().toLong())
                    0L
                }
            }
        }
        verify {
            expect result 9194682052782L
            run test 1 expect 3263827L
        }
    }

}

private fun List<Long>.reduce(operator: Char): Long =
    when (operator) {
        '*' -> reduce(Long::times)
        else -> reduce(Long::plus)
    }
