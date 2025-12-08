package de.rubixdev.aoc

import kotlin.math.pow
import kotlin.math.sqrt

fun List<Long>.toVec3(): Vec3 = Vec3(get(0), get(1), get(2))

data class Vec3(val x: Long, val y: Long, val z: Long) {
    constructor(v: Long) : this(v, v, v)

    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Long) = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun times(scalar: Int) = Vec3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Long) = Vec3(x / scalar, y / scalar, z / scalar)
    operator fun div(scalar: Int) = Vec3(x / scalar, y / scalar, z / scalar)
    operator fun rem(other: Vec3) = Vec3(x % other.x, y % other.y, z % other.z)
    infix fun mod(other: Vec3) = Vec3(x.mod(other.x), y.mod(other.y), z.mod(other.z))

    fun isInBounds(size: Vec3) = x in 0..size.x && y in 0..size.y && z in 0..size.z

    fun reduce() = gcd(x, gcd(y, z)).let { Vec3(x / it, y / it, z / it) }

    val length: Double get() =
        sqrt(x.toDouble().pow(2) + y.toDouble().pow(2) + z.toDouble().pow(2))
}

operator fun Long.times(vec: Vec3) = vec * this
operator fun Int.times(vec: Vec3) = vec * this
