package de.rubixdev.aoc

fun day7(input: String): Day = sequence {
    val example = """
        .......S.......
        ...............
        .......^.......
        ...............
        ......^.^......
        ...............
        .....^.^.^.....
        ...............
        ....^.^...^....
        ...............
        ...^.^...^.^...
        ...............
        ..^...^.....^..
        ...............
        .^.^.^.^.^...^.
        ...............
    """.trimIndent()
    var startPos = 0 by 0
//    val input = example.lines()
    val input = input.lines()
        .withIndex()
        .map { (y, line) ->
            line.withIndex().map { (x, tile) ->
                when (tile) {
                    '.' -> false
                    '^' -> true
                    'S' -> false.also { startPos = x by y }
                    else -> throw IllegalStateException("invalid input")
                }
            }
        }

    yield(null)
    yield(part1(input, startPos))
    yield(part2(input, startPos))
}

private fun part1(input: List<List<Boolean>>, startPos: Vec2): Int {
    var count = 0
    input.fold(setOf(startPos.x.toInt())) { beams, line ->
        beams.flatMap { beam ->
            when (line[beam]) {
                true -> listOf(beam - 1, beam + 1).also { count++ }
                false -> listOf(beam)
            }
        }.toSet()
    }
    return count
}

private fun part2(input: List<List<Boolean>>, startPos: Vec2) =
    input.fold(mapOf(startPos.x.toInt() to 1L)) { beams, line ->
        beams.flatMap { (beam, count) ->
            when (line[beam]) {
                true -> listOf((beam - 1) to count, (beam + 1) to count)
                false -> listOf(beam to count)
            }
        }.groupingBy { it.first }.fold(0L) { sum, e -> sum + e.second }
    }.values.sum()
