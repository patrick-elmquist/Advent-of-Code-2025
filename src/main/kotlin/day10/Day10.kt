package day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import common.day
import java.util.PriorityQueue

// answer #1: 399
// answer #2:

fun main() {
    day(n = 10) {
        part1 { input ->
            input.lines.sumOf {
                val split = it.split(" ")
                val lights = split.first().removeSurrounding("[", "]")
                    .map { if (it == '#') '1' else '0' }.joinToString("")
                val switches = split.drop(1).dropLast(1)
                    .map { it.drop(1).dropLast(1).split(",").map { it.toInt() } }

                fewestKeyPressesRequired(lights, switches)
            }

        }
        verify {
            expect result 399
            run test 1 expect 7
        }

        part2 { input ->
            input.lines
//                .take(1) // TODO REMOVE BEFORE REAL RUN
                .sumOf {

                    val split = it.split(" ")
                    val joltage =
                        split.last().removeSurrounding("{", "}").split(",").map { it.toInt() }
                    val switches = split.drop(1).dropLast(1)
                        .map { it.drop(1).dropLast(1).split(",").map { it.toInt() }.toSet() }

                    println(it)
                    solveWithZ3(joltage, switches)
                }
        }
        verify {
            expect result null
            run test 1 expect 33
        }
    }
}

private fun solveWithZ3(
    target: List<Int>,
    buttons: List<Set<Int>>,
): Int {
    val context = Context()
    val optimize = context.mkOptimize()
    val presses = context.mkIntConst("presses")

    val buttonVars = buttons.indices.map {
        context.mkIntConst("button$it")
    }.toTypedArray()

    val countersToButtons = mutableMapOf<Int, List<IntExpr>>()

    buttons.indices.forEach { i ->
        val buttonVar = buttonVars[i]
        for (flip in buttons[i]) {
            countersToButtons.computeIfAbsent(flip) { mutableListOf(buttonVar) }
        }
    }

    countersToButtons.entries.forEach { entry ->
        val (counterIndex, counterButtons) = entry
        val targetValue = context.mkInt(target[counterIndex])
        val buttonPressesArray: Array<IntExpr> = counterButtons.toTypedArray().ifEmpty { emptyArray() }
        val sumOfButtonPresses = context.mkAdd(*buttonPressesArray) as IntExpr
        val equation = context.mkEq(targetValue, sumOfButtonPresses)
        optimize.Add(equation)
    }

    val zero = context.mkInt(0)
    buttonVars.forEach { buttonVar ->
        val nonNegative = context.mkGe(buttonVar, zero)
        optimize.Add(nonNegative)
    }

    val sumOfAllButtonsVars = context.mkAdd(*buttonVars) as IntExpr
    val totalPressesEq = context.mkEq(presses, sumOfAllButtonsVars)
    optimize.Add(totalPressesEq)

    optimize.MkMinimize(presses)

    val status = optimize.Check()

    return if (status == Status.SATISFIABLE) {
        val model = optimize.model
        val outputValue = model.evaluate(presses, false) as (IntNum)
        outputValue.getInt()
    } else if (status == Status.UNSATISFIABLE) {
        error("Unsatisfiable")
    } else {
        error("Unknown $status")
    }
}


data class State2(
    val joltage: List<Int>,
    val presses: Int,
)

private fun solve(
    target: List<Int>,
    switches: List<Set<Int>>,
): Int {
    val len = target.size
    val initialState = State2(
        joltage = target,
        presses = 0,
    )

    val queue = PriorityQueue<State2>(compareBy { it.presses })
    queue.add(initialState)

    println()
    println("RUNNING")
    println("Switches: $switches")
    println("Target: $target")
    var loops = 0L

    while (true) {
        loops++
        val state = queue.poll()!!
//        println(state)

        if (state.joltage.any { it < 0 }) continue
        if (state.joltage.sum() == 0) {
            println("presses: ${state.presses} loops: $loops")
            return state.presses
        }

        switches.forEach { wires ->
            val next = State2(
                joltage = state.joltage.mapIndexed { index, j ->
                    if (index in wires) {
                        j - 1
                    } else {
                        j
                    }
                },
                presses = state.presses + 1,
            )
            if (next !in queue) {
                queue.add(next)
            }
            queue.add(next)
        }
    }
}


private fun fewestKeyPressesRequired(lights: String, switches: List<List<Int>>): Int {
    val value = Integer.parseInt(lights.reversed(), 2)

    val len = lights.length
    val numbers = switches.map { switch ->
        val buttonsValue = MutableList(len) { 0 }
        switch.forEach { i -> buttonsValue[len - 1 - i] = 1 }
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

    while (true) {
        val state = queue.poll()!!

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
