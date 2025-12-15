package de.rubixdev.aoc

enum class Op {
    ON,
    OFF,
    TOGGLE,
}

fun day6(input: String): Day = sequence {
    val regex = """^(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)$""".toRegex()
    val input = input.lines().map { line ->
        val (_, op, x1, y1, x2, y2) = regex.matchEntire(line)!!.groupValues
        Triple(
            when (op) {
                "turn on" -> Op.ON
                "turn off" -> Op.OFF
                "toggle" -> Op.TOGGLE
                else -> throw IllegalStateException()
            },
            x1.toInt() by y1.toInt(),
            x2.toInt() by y2.toInt(),
        )
    }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Triple<Op, Vec2, Vec2>>): Int {
    val map = (0..999).map { (0..999).map { false }.toMutableList() }
    for ((op, from, to) in input) {
        for (x in from.x..to.x) {
            for (y in from.y..to.y) {
                map[x by y] = when (op) {
                    Op.ON -> true
                    Op.OFF -> false
                    Op.TOGGLE -> !map[x by y]
                }
            }
        }
    }
    return map.asSequence().flatten().count { it }
}

private fun part2(input: List<Triple<Op, Vec2, Vec2>>): Int {
    val map = (0..999).map { (0..999).map { 0 }.toMutableList() }
    for ((op, from, to) in input) {
        for (x in from.x..to.x) {
            for (y in from.y..to.y) {
                val new = map[x by y] + when (op) {
                    Op.ON -> 1
                    Op.OFF -> -1
                    Op.TOGGLE -> 2
                }
                map[x by y] = new.coerceAtLeast(0)
            }
        }
    }
    return map.asSequence().flatten().sum()
}
