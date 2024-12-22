package de.rubixdev

import java.io.File

fun day11(): Day = sequence {
    val example = "125 17"
//    val input = example
    val input = File("inputs/day11.txt").readText().trim()
        .split(' ')
        .map { it.toLong() }

    yield(Unit)
    yield(part(input, 25))
    yield(part(input, 75))
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
        else -> listOf(stone * 2024 to count)
    }
}.groupingBy { it.first }.fold(0L) { acc, e -> acc + e.second }

private fun part(input: List<Long>, count: Int) =
    (1..count).fold(
        input.groupingBy { it }.fold(0L) { acc, _ -> acc + 1 },
    ) { stones, _ -> stones.blink() }.values.sum()
