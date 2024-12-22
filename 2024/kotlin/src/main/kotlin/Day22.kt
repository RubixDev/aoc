package de.rubixdev

import java.io.File;

fun runDay22() {
    val example = """
    1
    10
    100
    2024
    """.trimIndent()
    // val input = example.lines()
    val input = File("inputs/day22.txt").readLines()
        .map { line -> line.toLong() }
    println("--- Day 22 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun pseudoRandom(seed: Long): Sequence<Long> = sequence {
    var num = seed
    while (true) {
        yield(num)
        num = (num xor (num shl 6)).mod(16777216L)
        num = (num xor (num shr 5)).mod(16777216L)
        num = (num xor (num shl 11)).mod(16777216L)
    }
}

private fun part1(input: List<Long>): Long =
    input.sumOf { seed -> pseudoRandom(seed).drop(2000).first() }

// once again, works for my input but not the example...
private fun part2(input: List<Long>): Long =
    input
        .flatMap { seed ->
            pseudoRandom(seed).take(2000)
                .map { it % 10 }
                .windowed(2)
                .map { (a, b) -> b to (b - a) }
                .windowed(4)
                .map { (a, b, c, d) ->
                    listOf(a.second, b.second, c.second, d.second) to d.first
                }
                .groupingBy { it.first }
                // only take the first of each key
                .aggregate { _, acc: Long?, v, _ -> acc ?: v.second }
                .entries
        }
        .groupingBy { it.key }
        .fold(0L) { acc, price -> acc + price.value }
        .maxOf { it.value }
