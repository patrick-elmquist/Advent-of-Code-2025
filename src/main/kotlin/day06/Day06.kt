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
            val last = input.lines.last()
            val operators = last.trim().split("\\s+".toRegex())

            val columnWidths = last.mapIndexedNotNull { i, c ->
                if (c.isWhitespace()) {
                    null
                } else {
                    val other = last.withIndex()
                        .firstOrNull { (index, c) -> index > i && !c.isWhitespace() }
                        ?.let { it.index - 1 }
                        ?: last.length
                    other - i
                }
            }

            val columns = List(columnWidths.size) { mutableListOf<String>() }
            input.lines.dropLast(1)
                .forEach { line ->
                    var start = 0
                    columnWidths.forEachIndexed { index, columnWidth ->
                        val end = (start + columnWidth).coerceAtMost(line.length)
                        val entry = line.substring(start, end)
                        start += columnWidth + 1
                        columns[index].add(entry)
                    }
                }

            columnWidths.mapIndexed { index, c ->
                val column = columns[index]
                val numbers = (0 until c).map { i ->
                    column.map { it[i] }.joinToString("").trim().toLong()
                }

                when (operators[index]) {
                    "*" -> numbers.reduce(Long::times)
                    else -> numbers.reduce(Long::plus)
                }
            }.sum()
        }
        verify {
            expect result 9194682052782L
            run test 1 expect 3263827L
        }
    }
}
