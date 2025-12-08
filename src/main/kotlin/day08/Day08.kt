package day08

import common.Input
import common.day
import common.util.Vec3l

// answer #1: 123234
// answer #2: 9259958565

fun main() {
    day(n = 8) {
        part1 { input ->
            val junctionBoxes = parseJunctionBoxes(input)
            val count = when {
                input.lines.size == 20 -> 10
                else -> 1000
            }
            val (circuits, _) = makeConnections(
                junctionBoxes = junctionBoxes,
                count = count,
            )

            circuits.map { it.size.toLong() }.sortedDescending().take(3).reduce(Long::times)
        }
        verify {
            expect result 123234L
            run test 1 expect 40L
        }

        part2 { input ->
            val junctionBoxes = parseJunctionBoxes(input)
            val (_, lastPair) = makeConnections(junctionBoxes = junctionBoxes)

            lastPair.first.x * lastPair.second.x
        }
        verify {
            expect result 9259958565L
            run test 1 expect 25272L
        }
    }
}

private fun makeConnections(
    junctionBoxes: List<Vec3l>,
    count: Int? = null,
): Pair<List<Set<Vec3l>>, Pair<Vec3l, Vec3l>> {
    val closestPairs = calculateClosestPairs(junctionBoxes)
    val circuits = junctionBoxes.map { mutableSetOf(it) }.toMutableList()
    val boxPairsToConnect = closestPairs
        .take(count ?: closestPairs.size)
        .toMutableList()

    var lastPair: Pair<Vec3l, Vec3l>
    do {
        lastPair = boxPairsToConnect.removeFirst()
        val (b1, b2) = lastPair

        val b1Circuit = circuits.firstOrNull { b1 in it }
        val b2Circuit = circuits.firstOrNull { b2 in it }

        when {
            b1Circuit == null && b2Circuit == null -> circuits += mutableSetOf(b1, b2)

            b1Circuit == b2Circuit -> Unit // already connected

            b1Circuit != null && b2Circuit != null -> {
                circuits.remove(b2Circuit)
                b1Circuit.addAll(b2Circuit)
            }

            b1Circuit != null -> b1Circuit += b2
            b2Circuit != null -> b2Circuit += b1
        }
    } while (boxPairsToConnect.isNotEmpty() && circuits.size > 1)

    return circuits to lastPair
}

private fun calculateClosestPairs(
    junctionBoxes: List<Vec3l>,
): List<Pair<Vec3l, Vec3l>> {
    val distance = mutableMapOf<Pair<Vec3l, Vec3l>, Float>()
    junctionBoxes.forEach { j1 ->
        junctionBoxes.forEach { j2 ->
            if (j1 != j2) {
                val pair = listOf(j1, j2).sorted().let { (p1, p2) -> p1 to p2 }
                distance[pair] = j1.distanceTo(j2)
            }
        }
    }
    return distance.entries.sortedBy { it.value }.map { it.key }
}

private fun parseJunctionBoxes(input: Input): List<Vec3l> =
    input.lines.map { it.split(',').map(String::toLong).let(::Vec3l) }
