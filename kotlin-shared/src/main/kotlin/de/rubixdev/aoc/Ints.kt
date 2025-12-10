package de.rubixdev.aoc

fun Boolean.toInt() = if (this) 1 else 0
fun Boolean.toLong() = if (this) 1L else 0L
fun Int.toBoolean() = this != 0

infix fun UShort.shl(other: UShort) = toUInt().shl(other.toInt()).toUShort()
infix fun UShort.shr(other: UShort) = toUInt().shr(other.toInt()).toUShort()

val Long.big get() = toBigInteger()
val Int.big get() = toBigInteger()
