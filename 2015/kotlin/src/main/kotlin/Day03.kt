package de.rubixdev

fun day3(): Day = sequence {
    val input = getInput(3).map { it.toDirection() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Direction>) = input.asSequence()
    .scan(0 by 0) { pos, dir -> dir.move(pos) }
    .distinct()
    .count()

private fun part2(input: List<Direction>) = input.asSequence()
    .chunked(2)
    .scan(0 by 0) { pos, dirs -> dirs[0].move(pos) }
    .plus(input.asSequence().chunked(2).scan(0 by 0) { pos, dirs -> dirs[1].move(pos) })
    .distinct()
    .count()
