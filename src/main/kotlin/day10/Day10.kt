package day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import common.Input
import common.day
import java.util.PriorityQueue
import kotlin.text.map

// answer #1: 399
// answer #2: 15631

fun main() {
    day(n = 10) {
        part1 { input ->
            parseInput(input).sumOf { (lights, switches, _) ->
                fewestKeyPressesRequired(lights, switches)
            }
        }
        verify {
            expect result 399
            run test 1 expect 7
        }

        part2 { input ->
            Context().use { context ->
                parseInput(input).sumOf { (_, switches, joltage) ->
                    solveWithZ3(context, joltage, switches)
                }
            }
        }
        verify {
            expect result 15631
            run test 1 expect 33
        }
    }
}

private fun solveWithZ3(
    ctx: Context,
    joltageRequirements: List<Int>,
    buttons: List<Set<Int>>,
): Int {
    val buttonVars = buttons.indices.map { ctx.mkIntConst("Button_$it") }.toTypedArray()

    val countersToButtons = mutableMapOf<Int, MutableList<IntExpr>>()
    buttonVars.forEachIndexed { i, buttonVar ->
        for (flip in buttons[i]) {
            countersToButtons.getOrPut(flip) { mutableListOf() }.add(buttonVar)
        }
    }

    val opt = ctx.mkOptimize()
    countersToButtons.entries.forEach { (counterIndex, counterButtons) ->
        val targetValue = ctx.mkInt(joltageRequirements[counterIndex])
        val sumOfButtonPresses = ctx.mkAdd(*counterButtons.toTypedArray()) as IntExpr
        opt.Add(ctx.mkEq(targetValue, sumOfButtonPresses))
    }

    val zero = ctx.mkInt(0)
    buttonVars.forEach { buttonVar -> opt.Add(ctx.mkGe(buttonVar, zero)) }

    val sumOfAllButtonsVars = ctx.mkAdd(*buttonVars) as IntExpr

    val presses = ctx.mkIntConst("Presses")
    opt.Add(ctx.mkEq(presses, sumOfAllButtonsVars))

    opt.MkMinimize(presses)
    opt.Check()

    return (opt.model.evaluate(presses, false) as (IntNum)).getInt()
}

private fun fewestKeyPressesRequired(lights: String, switches: List<Set<Int>>): Int {
    val lights = lights.map { if (it == '#') '1' else '0' }
        .joinToString("")

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

private data class State(val int: Int, val presses: Int)

private fun findMinPresses(
    target: Int,
    numbers: Set<Int>,
): Int {
    val queue = PriorityQueue<State>(compareBy { it.presses })
    queue.addAll(numbers.map { State(int = it, presses = 1) })

    while (true) {
        val state = queue.poll()

        if (state.int == target) return state.presses

        numbers.forEach { n ->
            queue.add(
                State(
                    int = n xor state.int,
                    presses = state.presses + 1,
                ),
            )
        }
    }
}

private fun parseInput(input: Input) =
    input.lines.map { line ->
        val split = line.split(" ")

        val lights = split
            .first()
            .removeSurrounding("[", "]")

        val switches = split
            .drop(1)
            .dropLast(1)
            .map {
                it.removeSurrounding("(", ")")
                    .split(",")
                    .map(String::toInt)
                    .toSet()
            }

        val joltage = split
            .last()
            .removeSurrounding("{", "}")
            .split(",")
            .map(String::toInt)

        Triple(lights, switches, joltage)
    }
