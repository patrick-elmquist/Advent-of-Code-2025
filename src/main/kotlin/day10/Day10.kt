package day10

import common.day
import java.util.PriorityQueue

// answer #1: 399
// answer #2:

fun main() {
    day(n = 10) {
        part1 { input ->
            input.lines
                .sumOf {
                    val split = it.split(" ")
                    val lights = split.first().removeSurrounding("[", "]")
                        .map { if (it == '#') '1' else '0' }.joinToString("")
                    val joltage = split.last().removeSurrounding("{", "}")
                    val switches = split.drop(1).dropLast(1)
                        .map { it.drop(1).dropLast(1).split(",").map { it.toInt() } }

                    println(lights)
                    println(switches)

                    fewestKeyPressesRequired(lights, switches)
                }

        }
        verify {
            expect result 399
            run test 1 expect 7
        }

        part2 { input ->
            input.lines.map {
                val split = it.split(" ")
                val lights = split.first().removeSurrounding("[", "]")
                val joltage = split.last().removeSurrounding("{", "}")
                val switches = split.drop(1).dropLast(1)
                    .map { it.drop(1).dropLast(1).split(",").map { it.toInt() } }
            }
        }
        verify {
            expect result null
            run test 1 expect Unit
        }
    }
}

private fun fewestKeyPressesRequired(lights: String, switches: List<List<Int>>): Int {
    val value = Integer.parseInt(lights.reversed(), 2)
    println("lights $value (${lights.reversed()})")

    val len = lights.length
    val numbers = switches.map { switch ->
        val buttonsValue = MutableList(len) { 0 }
        switch.forEach { i -> buttonsValue[len - 1 - i] = 1 }
        println(switch)
        println(buttonsValue)
        println(Integer.parseInt(buttonsValue.joinToString(""), 2))
        println()
        Integer.parseInt(buttonsValue.joinToString(""), 2)
    }

    return findMinPresses(
        target = value,
        numbers = numbers.toSet(),
    )
}

data class State(
    val int: Int,
    val presses: List<Int>,
)

fun findMinPresses(
    target: Int,
    numbers: Set<Int>,
): Int {
    val initialStates = numbers.map {
        State(
            int = it,
            presses = mutableListOf(it),
        )
    }
    val queue = PriorityQueue<State>(compareBy { it.presses.size })
    queue.addAll(initialStates)

    println()
    println("RUNNING")
    while (true) {
        val state = queue.poll()!!
        println(state)

        if (state.int == target) return state.presses.size

        numbers.filter { it != state.presses.last() }
            .forEach { n ->
                queue.add(
                    State(
                        int = n xor state.int,
                        presses = state.presses + n,
                    ),
                )
            }
    }
}
