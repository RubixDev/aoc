package de.rubixdev

import java.io.File

fun day2(): Day = sequence {
    val input = File("inputs/day2.txt").readLines()
        .map { line -> line.split(" ").map { it.toInt() } }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun List<Int>.isSafe() =
    windowed(2).all { it[1] - it[0] in 1..3 }

private fun part1(input: List<List<Int>>): Int =
    input.count { it.isSafe() || it.reversed().isSafe() }

private fun part2(input: List<List<Int>>): Int =
    input.count { report ->
        report.isSafe() || report.reversed().isSafe()
                || report.indices.map { report.toMutableList().apply { removeAt(it) } }
            .any { it.isSafe() || it.reversed().isSafe() }
    }
