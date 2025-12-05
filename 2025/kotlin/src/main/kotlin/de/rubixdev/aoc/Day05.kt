package de.rubixdev.aoc

import kotlin.math.max

fun day5(input: String): Day = sequence {
    val example = """
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent().trim()
//    val (rawRanges, rawInv) = example.split("\n\n")
    val (rawRanges, rawInv) = input.split("\n\n")
    val ranges = rawRanges.lines().map { line ->
        line.split('-').map { it.toLong() }.toPair().let { (a, b) -> a..b }
    }
    val inv = rawInv.lines().map { it.toLong() }

    yield(Unit)
    yield(part1(ranges, inv))
    yield(part2(ranges))
}

private fun part1(ranges: List<LongRange>, inv: List<Long>) =
    inv.count { id -> ranges.any { id in it } }

private fun part2(ranges: List<LongRange>) = ranges.asSequence()
    .sortedBy { it.first }
    .fold(mutableListOf<LongRange>()) { acc, range ->
        acc.apply {
            when (acc.isNotEmpty() && range.first in acc.last()) {
                true -> set(lastIndex, last().first..max(range.last, last().last))
                false -> add(range)
            }
        }
    }
    .sumOf { it.last - it.first + 1 }
