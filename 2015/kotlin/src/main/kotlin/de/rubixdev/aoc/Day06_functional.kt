package de.rubixdev.aoc

fun day6Functional(input: String): Day = sequence {
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

    yield("6 (functional)")
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Triple<Op, Vec2, Vec2>>) =
    input.fold((0..999).map { (0..999).map { false } }) { map, (op, from, to) ->
        map.mapIndexed { y, row ->
            row.mapIndexed { x, light ->
                when (x in from.x..to.x && y in from.y..to.y) {
                    true -> when (op) {
                        Op.ON -> true
                        Op.OFF -> false
                        Op.TOGGLE -> !light
                    }

                    false -> light
                }
            }
        }
    }.asSequence().flatten().count { it }

private fun part2(input: List<Triple<Op, Vec2, Vec2>>) =
    input.fold((0..999).map { (0..999).map { 0 } }) { map, (op, from, to) ->
        map.mapIndexed { y, row ->
            row.mapIndexed { x, light ->
                when (x in from.x..to.x && y in from.y..to.y) {
                    true -> when (op) {
                        Op.ON -> light + 1
                        Op.OFF -> light - 1
                        Op.TOGGLE -> light + 2
                    }.coerceAtLeast(0)

                    false -> light
                }
            }
        }
    }.asSequence().flatten().sum()
