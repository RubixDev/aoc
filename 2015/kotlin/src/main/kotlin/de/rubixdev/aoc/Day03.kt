package de.rubixdev.aoc

fun day3(input: String): Day = sequence {
    val input = input.map { it.toDirection()!! }

    yield(null)
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
