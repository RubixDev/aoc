package de.rubixdev

import java.io.File

fun runDay11() {
    val example = "125 17"
//    val input = example
    val input = File("inputs/day11.txt").readText().trim()
        .split(' ')
        .map { it.toLong() }
    println("--- Day 11 ---")
    println("Part 1: ${part(input, 25)}")
    println("Part 1 (recursive): ${part1Recursive(input)}")
    println("Part 2: ${part(input, 75)}")
}

private fun part1Recursive(input: List<Long>) =
    input.sumOf { part1Recursive(it) }

private fun part1Recursive(stone: Long, depth: Int = 0): Long =
    when {
        depth == 24 && stone.toString().length % 2 == 0 -> 2L
        depth == 24 -> 1L
        stone == 0L -> part1Recursive(1, depth + 1)
        stone.toString().length % 2 == 0 -> stone.toString().let {
            part1Recursive(it.slice(0..<(it.length / 2)).toLong(), depth + 1) +
                    part1Recursive(it.slice((it.length / 2)..it.lastIndex).toLong(), depth + 1)
        }

        else -> part1Recursive(stone * 2024, depth + 1)
    }

private fun Map<Long, Long>.blink() = flatMap { (stone, count) ->
    when {
        stone == 0L -> listOf(1L to count)
        stone.toString().length % 2 == 0 -> stone.toString().let {
            listOf(
                it.slice(0..<(it.length / 2)).toLong() to count,
                it.slice((it.length / 2)..it.lastIndex).toLong() to count
            )
        }
//        (log10(stone.toFloat()).toInt() + 1) % 2 == 0 -> ...
        else -> listOf(stone * 2024 to count)
    }
}.groupingBy { it.first }.fold(0L) { acc, e -> acc + e.second }

private fun part(input: List<Long>, count: Int) =
    (1..count).fold(
        input.groupingBy { it }.fold(0L) { acc, _ -> acc + 1 },
    ) { stones, _ -> stones.blink() }.values.sum()
