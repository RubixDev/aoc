package de.rubixdev.aoc

import kotlin.math.absoluteValue
import kotlin.math.sign

fun day1(input: String): Day = sequence {
    val input = input.lines()
        .map { line -> line.replace("R", "").replace('L', '-').toInt() }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Int>): Int = input
    .asSequence()
    .scan(50) { acc, i -> (acc + i).mod(100) }
    .count { it == 0 }

private fun part2(input: List<Int>): Int = input
    .asSequence()
    .flatMap { n -> sequence { repeat(n.absoluteValue) { yield(n.sign) } } }
    .scan(50) { acc, i -> (acc + i).mod(100) }
    .count { it == 0 }
