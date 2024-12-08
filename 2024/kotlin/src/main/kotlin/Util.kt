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

data class Vec2d(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Vec2d) =
        x + other.x by y + other.y

    operator fun minus(other: Vec2d) =
        x - other.x by y - other.y

    fun isInBounds(map: Collection<Collection<*>>) =
        x in map.first().indices && y in map.indices

    fun reduce() = gcd(x, y).let { (x / it) by (y / it) }
}

tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)

infix fun Int.by(y: Int): Vec2d = Vec2d(this, y)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ;

    fun rotateRight() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun move(vec2d: Vec2d) = when (this) {
        UP -> vec2d.x by vec2d.y - 1
        DOWN -> vec2d.x by vec2d.y + 1
        LEFT -> vec2d.x - 1 by vec2d.y
        RIGHT -> vec2d.x + 1 by vec2d.y
    }
}

operator fun <T> List<List<T>>.get(index: Vec2d) =
    this[index.y][index.x]

operator fun <T> List<MutableList<T>>.set(index: Vec2d, value: T) {
    this[index.y][index.x] = value
}

fun Boolean.toInt() = if (this) 1 else 0
