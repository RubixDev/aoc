package de.rubixdev.aoc

val LongRange.size: Long get() = last - first + 1

fun LongRange.overlaps(other: LongRange): Boolean =
    first in other || last in other || other.first in this || other.last in this

operator fun LongRange.contains(other: LongRange): Boolean =
    other.first in this && other.last in this
