package day06

import common.day

// answer #1: 6295830249262
// answer #2: 9194682052782

fun main() {
    day(n = 6) {
        part1 { input ->
            val lists = mutableListOf<MutableList<Long>>()
            input.lines.dropLast(1)
                .forEach { line ->
                    line.trim()
                        .split("\\s+".toRegex())
                        .forEachIndexed { i, c ->
                            val list = if (lists.lastIndex < i) {
                                val l = mutableListOf<Long>()
                                lists.add(l)
                                l
                            } else {
                                lists[i]
                            }
                            list.add(c.toLong())
                        }
                }

            val instr = input.lines.last()
                .split("\\s+".toRegex())

            lists.mapIndexed { i, l ->
                when (instr[i]) {
                    "*" -> l.reduce(Long::times)
                    else -> l.reduce(Long::plus)
                }
            }.sum()
        }
        verify {
            expect result 6295830249262L
            run test 1 expect 4277556L
        }

        part2 { input ->
            val last = input.lines.last()
            val columns = last
                .mapIndexedNotNull { i, c ->
                    if (c.isWhitespace()) {
                        null
                    } else {
                        val other = last.withIndex()
                            .firstOrNull { (index, c) ->
                                index > i && !c.isWhitespace()
                            }?.let { it.index - 1 } ?: last.length
                        other - i
                    }
                }

            val lists = List(columns.size) { mutableListOf<String>() }
            input.lines.dropLast(1)
                .forEach { line ->
                    var sum = 0
                    columns.forEachIndexed { index, column ->
                        val d = line.substring(sum, (sum + column).coerceAtMost(line.length))
                        sum += column + 1
                        lists[index].add(d)
                    }
                }

            val instr = input.lines.last()
                .split("\\s+".toRegex())

            columns.mapIndexed { index, c ->
                val column = lists[index]
                val numbers = (0 until c).map { i ->
                    column.map { it[i] }.joinToString("").trim().toLong()
                }

                when (instr[index]) {
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
