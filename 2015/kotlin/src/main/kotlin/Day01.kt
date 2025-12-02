package de.rubixdev

fun day1(): Day = sequence {
    val input = getInput(1).map {
        when (it) {
            '(' -> 1
            ')' -> -1
            else -> throw RuntimeException("illegal input")
        }
    }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Int>) = input.sum()

private fun part2(input: List<Int>) = input.scan(0) { acc, n -> acc + n }.indexOf(-1)
