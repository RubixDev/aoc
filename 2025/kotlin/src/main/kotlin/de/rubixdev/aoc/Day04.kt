package de.rubixdev.aoc

fun day4(input: String): Day = sequence {
    val example = """
    ..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@.
    """.trimIndent().trim()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.map { it == '@' } }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<List<Boolean>>) = input.withIndex().sumOf { (y, line) ->
    line.withIndex().count { (x, tile) ->
        tile && ADJACENT.mapNotNull { input.getOrNull((x by y) + it) }.count { it } < 4
    }
}

private fun part2(input: List<List<Boolean>>) =
    generateSequence(0 to input) { (_, map) -> step(map) }
        .drop(1)
        .map { it.first }
        .takeWhile { it != 0 }
        .sum()

private fun step(map: List<List<Boolean>>) = map.withIndex().map { (y, line) ->
    line.withIndex().map { (x, tile) ->
        when (tile && ADJACENT.mapNotNull { map.getOrNull((x by y) + it) }.count { it } < 4) {
            true -> 1 to false
            false -> 0 to tile
        }
    }.foldFirst(0) { sum, count -> sum + count }
}.foldFirst(0) { sum, count -> sum + count }
