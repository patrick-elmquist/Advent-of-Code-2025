package day03

import common.day

// answer #1: 17193
// answer #2: 171297349921310

fun main() {
    day(n = 3) {
        part1 { input ->
            input.lines
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
    bank: String,
    batteryCount: Int,
    batteries: Long = 0L,
): Long {
    if (batteryCount == 0) return batteries

    val (index, joltage) = findNextMaxJoltage(
        bank = bank,
        remainingBatteries = batteryCount,
    )

    return maximizeJoltage(
        bank = bank.substring(index + 1),
        batteries = batteries * 10 + joltage,
        batteryCount = batteryCount - 1,
    )
}

private fun findNextMaxJoltage(
    bank: String,
    remainingBatteries: Int,
): Pair<Int, Int> {
    var maxWithIndex: Pair<Int, Int> = -1 to Int.MIN_VALUE
    for (index in 0..(bank.length - remainingBatteries)) {
        val joltage = bank[index].digitToInt()
        when {
            joltage == 9 -> return index to joltage
            joltage > maxWithIndex.second -> maxWithIndex = index to joltage
        }
    }
    return maxWithIndex
}
