package de.rubixdev

import java.io.File
import kotlin.math.abs

fun day1(): Day = sequence {
    val input = File("inputs/day1.txt").readLines()
        .map { line -> line.split("   ").map(String::toInt).let { it[0] to it[1] } }
        .unzip()
        .map { it.sorted() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: Pair<List<Int>, List<Int>>): Int =
    input.first.zip(input.second).sumOf { (a, b) -> abs(a - b) }

private fun part2(input: Pair<List<Int>, List<Int>>): Int =
    input.first.sumOf { num -> num * input.second.count { it == num } }
