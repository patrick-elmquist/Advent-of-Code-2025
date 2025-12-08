package common.util

import kotlin.math.abs

/**
 * Class representing a point in 2 dimensions
 */
data class Vec2i(val x: Int, val y: Int): Comparable<Vec2i> {

    constructor(xy: List<Int>) : this(xy[0], xy[1])

    override fun compareTo(other: Vec2i): Int =
        compareValuesBy(this, other, { it.y }, { it.x })

    operator fun plus(point: Vec2i) = Vec2i(x + point.x, y + point.y)
    operator fun minus(point: Vec2i) = Vec2i(x - point.x, y - point.y)

    fun distanceTo(other: Vec2i): Int =
        abs(other.x - x) + abs(other.y - y)

    companion object {
        val Zero = Vec2i(0, 0)
        val Origin = Zero
    }
}

fun Vec2i(x: String, y: String) =
    Vec2i(x.toInt(), y.toInt())

fun Vec2i(match: MatchResult.Destructured) =
    match.let { (x, y) -> Vec2i(x.toInt(), y.toInt()) }

operator fun Vec2i.plus(dir: Direction) = this + dir.point
operator fun Vec2i.minus(dir: Direction) = this - dir.point

val Vec2i.leftNeighbour: Vec2i
    get() = this + Direction.Left

val Vec2i.rightNeighbour: Vec2i
    get() = this + Direction.Right

val Vec2i.aboveNeighbour: Vec2i
    get() = this + Direction.Up

val Vec2i.belowNeighbour: Vec2i
    get() = this + Direction.Down

fun Vec2i.nextInDirection(direction: Direction, steps: Int = 1): Vec2i =
    when (direction) {
        Direction.Left -> copy(x = x - steps)
        Direction.Up -> copy(y = y - steps)
        Direction.Right -> copy(x = x + steps)
        Direction.Down -> copy(y = y + steps)
    }

fun Vec2i.neighbors(
    diagonal: Boolean = false,
    includeSelf: Boolean = false,
) = sequence {
    if (diagonal) yield(Vec2i(x - 1, y - 1))
    yield(copy(y = y - 1))
    if (diagonal) yield(Vec2i(x + 1, y - 1))

    yield(copy(x = x - 1))
    if (includeSelf) yield(this@neighbors)
    yield(copy(x = x + 1))

    if (diagonal) yield(Vec2i(x - 1, y + 1))
    yield(copy(y = y + 1))
    if (diagonal) yield(Vec2i(x + 1, y + 1))
}

data class Vec2l(val x: Long, val y: Long) : Comparable<Vec2l> {
    constructor(xy: List<Long>) : this(xy[0], xy[1])
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    override fun compareTo(other: Vec2l): Int =
        compareValuesBy(this, other, { it.y }, { it.x })

    operator fun plus(point: Vec2l) = Vec2l(x + point.x, y + point.y)
    operator fun minus(point: Vec2l) = Vec2l(x - point.x, y - point.y)

    fun distanceTo(other: Vec2l): Long = abs(other.x - x) + abs(other.y - y)

    companion object {
        val Zero = Vec2l(0L, 0L)
        val Origin = Zero
    }
}

fun Vec2l(x: String, y: String) =
    Vec2l(x.toLong(), y.toLong())

fun Vec2l(match: MatchResult.Destructured) =
    match.let { (x, y) -> Vec2l(x.toLong(), y.toLong()) }

operator fun Vec2l.plus(dir: Direction) = this + dir.vec2l
operator fun Vec2l.minus(dir: Direction) = this - dir.vec2l
