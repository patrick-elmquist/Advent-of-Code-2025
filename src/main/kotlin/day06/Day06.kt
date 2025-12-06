package day06

import common.day

// answer #1: 6295830249262
// answer #2: 9194682052782

fun main() {
    day(n = 6) {
        part1 { input ->
            val operators = input.lines.last().trim().split("\\s+".toRegex())
            val lists = List(operators.size) { mutableListOf<Long>() }
            input.lines.dropLast(1)
                .forEach { line ->
                    line.trim()
                        .split("\\s+".toRegex())
                        .forEachIndexed { i, number ->
                            lists[i].add(number.toLong())
                        }
                }

            lists.mapIndexed { index, numbers ->
                when (operators[index]) {
                    "*" -> numbers.reduce(Long::times)
                    else -> numbers.reduce(Long::plus)
                }
            }.sum()
        }
        verify {
            expect result 6295830249262L
            run test 1 expect 4277556L
        }

        part2 { input ->
            val lines = input.lines.map { "$it " }
            val columnIndices = lines.first().indices
            var operator: Char? = null
            val numbers = mutableListOf<Long>()
            columnIndices.sumOf { index ->
                val column = lines.map { it[index] }
                if (column.all(Char::isWhitespace)) {
                    when (operator!!) {
                        '*' -> numbers.reduce(Long::times)
                        else -> numbers.reduce(Long::plus)
                    }
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
