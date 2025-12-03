package day03

import common.day

// answer #1: 17193
// answer #2: 171297349921310

fun main() {
    day(n = 3) {
        part1 { input ->
            input.lines
                .map { line -> line.map(Char::digitToInt)}
                .parallelStream()
                .map { bank -> maximizeJoltage(bank, batteryCount = 2) }
                .reduce(0L, Long::plus)
        }
        verify {
            expect result 17193L
            run test 1 expect 357L
        }

        part2 { input ->
            input.lines
                .map { line -> line.map(Char::digitToInt)}
                .parallelStream()
                .map { bank -> maximizeJoltage(bank, batteryCount = 12) }
                .reduce(0L, Long::plus)
        }
        verify {
            expect result 171297349921310L
            run test 1 expect 3121910778619L
        }
    }
}

private fun maximizeJoltage(
    bank: List<Int>,
    batteryCount: Int,
    joltage: Long = 0L,
): Long {
    if (batteryCount == 0) return joltage
    val index = (0..(bank.size - batteryCount)).maxBy { bank[it] }
    return maximizeJoltage(
        bank = bank.subList(index + 1, bank.size),
        joltage = joltage * 10 + bank[index],
        batteryCount = batteryCount - 1,
    )
}
