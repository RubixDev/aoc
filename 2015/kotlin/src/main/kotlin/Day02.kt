package de.rubixdev

fun day2(): Day = sequence {
//    val input = listOf("2x3x4", "1x1x10")
    val input = getInput(2).lines()
        .map { line -> line.split('x').toTriple().map { it.toInt() } }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Triple<Int, Int, Int>>) =
    input.map { listOf(it.first * it.second, it.second * it.third, it.third * it.first) }
        .sumOf { sides -> sides.sumOf { it * 2 } + sides.min() }

private fun part2(input: List<Triple<Int, Int, Int>>) = input.sumOf {
    listOf(
        2 * it.first + 2 * it.second,
        2 * it.second + 2 * it.third,
        2 * it.third + 2 * it.first,
    ).min() +
        it.toList().product()
}
