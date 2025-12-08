package de.rubixdev.aoc

fun day1(input: String): Day = sequence {
    val input = input.map {
        when (it) {
            '(' -> 1
            ')' -> -1
            else -> throw RuntimeException("illegal input")
        }
    }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Int>) = input.sum()

private fun part2(input: List<Int>) = input.scan(0) { acc, n -> acc + n }.indexOf(-1)
