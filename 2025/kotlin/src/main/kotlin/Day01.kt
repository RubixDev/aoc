package de.rubixdev

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

fun day1(): Day = sequence {
    val input =
        File("inputs/day1.txt")
            .readLines()
            .map { line -> line.replace("R", "").replace('L', '-').toInt() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Int>): Int = input
    .asSequence()
    .runningFold(50) { acc, i -> (acc + i).mod(100) }
    .count { it == 0 }

private fun part2(input: List<Int>): Int = input
    .asSequence()
    .flatMap { n -> sequence { repeat(n.absoluteValue) { yield(n.sign) } } }
    .runningFold(50) { acc, i -> (acc + i).mod(100) }
    .count { it == 0 }
