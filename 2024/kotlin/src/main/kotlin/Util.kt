package de.rubixdev

import kotlin.math.abs

fun <A, B> Pair<A, A>.map(mapper: (A) -> B): Pair<B, B> =
    mapper(first) to mapper(second)

fun <A, B> Triple<A, A, A>.map(mapper: (A) -> B): Triple<B, B, B> =
    Triple(mapper(first), mapper(second), mapper(third))

fun <T> List<T>.toPair(): Pair<T, T> =
    this[0] to this[1]

fun <T> List<T>.toTriple(): Triple<T, T, T> =
    Triple(this[0], this[1], this[2])

fun List<Int>.toVec2(): Vec2 =
    this[0] by this[1]

data class Vec2(
    val x: Long,
    val y: Long,
) {
    constructor(v: Long) : this(v, v)

    operator fun plus(other: Vec2) = x + other.x by y + other.y
    operator fun minus(other: Vec2) = x - other.x by y - other.y
    operator fun times(scalar: Long) = x * scalar by y * scalar
    operator fun times(scalar: Int) = x * scalar by y * scalar
    operator fun div(scalar: Long) = x / scalar by y / scalar
    operator fun div(scalar: Int) = x / scalar by y / scalar
    operator fun rem(other: Vec2) = x % other.x by y % other.y
    infix fun mod(other: Vec2) = x.mod(other.x) by y.mod(other.y)

    fun isInBounds(map: Collection<Collection<*>>) =
        x in map.first().indices && y in map.indices

    fun isInBounds(size: Vec2) =
        x in 0..size.x && y in 0..size.y

    fun reduce() = gcd(x, y).let { (x / it) by (y / it) }
}

operator fun Long.times(vec: Vec2) = vec * this
operator fun Int.times(vec: Vec2) = vec * this

tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) abs(a) else gcd(b, a % b)

infix fun Int.by(y: Int): Vec2 = Vec2(this.toLong(), y.toLong())

infix fun Long.by(y: Long): Vec2 = Vec2(this, y)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ;

    val vec get() = when (this) {
        UP -> 0 by -1
        DOWN -> 0 by 1
        LEFT -> -1 by 0
        RIGHT -> 1 by 0
    }

    val isHorizontal get() = when (this) {
        UP, DOWN -> false
        LEFT, RIGHT -> true
    }

    fun rotateLeft() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
    }

    fun rotateRight() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun move(vec2: Vec2) = vec + vec2

    override fun toString() = when (this) {
        UP -> "^"
        DOWN -> "v"
        LEFT -> "<"
        RIGHT -> ">"
    }
}

operator fun <T> List<List<T>>.get(index: Vec2) =
    this[index.y.toInt()][index.x.toInt()]

operator fun <T> List<MutableList<T>>.set(index: Vec2, value: T) {
    this[index.y.toInt()][index.x.toInt()] = value
}

fun <T> List<List<T>>.getOrNull(index: Vec2) =
    getOrNull(index.y.toInt())?.getOrNull(index.x.toInt())

fun <T> Iterable<Iterable<T>>.findPos(predicate: (T) -> Boolean) =
    withIndex().firstNotNullOf { (y, line) ->
        line.withIndex().find { (_, tile) -> predicate(tile) }?.let { it.index by y }
    }

fun Boolean.toInt() = if (this) 1 else 0

fun <T> List<List<T>>.cartesianProduct(): Sequence<List<T>> =
    when (size) {
        0 -> sequenceOf()
        1 -> get(0).asSequence().map { listOf(it) }
        else -> get(0).asSequence().flatMap { a -> drop(1).cartesianProduct().map { b -> listOf(a) + b } }
    }
