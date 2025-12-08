package common.util

import java.util.PriorityQueue
import kotlin.sequences.forEach

fun Map<Vec2i, Char>.findShortestPath(
    start: Vec2i,
    end: Vec2i,
    validTiles: Set<Char> = setOf('.'),
): Pair<Int, List<Vec2i>> {
    return findShortestPath(
        start,
        end,
        validTiles,
        defaultValue = null,
    )!!
}

fun Map<Vec2i, Char>.findShortestPath(
    start: Vec2i,
    end: Vec2i,
    validTiles: Set<Char> = setOf('.'),
    defaultValue: Pair<Int, List<Vec2i>>?,
): Pair<Int, List<Vec2i>>? {
    val distances = mutableMapOf<Vec2i, Int>().withDefault { Int.MAX_VALUE }
    distances[start] = 0

    val queue = PriorityQueue<Pair<Vec2i, List<Vec2i>>>(compareBy { distances[it.first] })
    queue.add(start to emptyList())

    val visited = mutableSetOf<Vec2i>()
    while (queue.isNotEmpty()) {
        val entry = queue.poll()
        val (point, path) = entry
        val distance = distances.getValue(point)

        if (point == end) return distance to path + point

        if (point in visited) continue
        visited += point

        point.neighbors()
            .filter { this[it] in validTiles }
            .forEach { n ->
                val distanceToN = distance + 1
                if (distanceToN < distances.getValue(n)) {
                    distances[n] = distanceToN
                    queue += n to path + point
                }
            }
    }

    return defaultValue
}
