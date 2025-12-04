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

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<List<Boolean>>) = input.withIndex().sumOf { (y, line) ->
    line.withIndex().count { (x, tile) ->
        tile && ADJACENT.mapNotNull { input.getOrNull((x by y) + it) }.count { it } < 4
    }
}

private fun part2(input: List<List<Boolean>>): Int {
    var map = input
    var sum = 0
    while (true) {
        val (changedCount, newMap) = step(map)
        if (changedCount == 0) break
        sum += changedCount
        map = newMap
    }
    return sum
}

private fun step(map: List<List<Boolean>>): Pair<Int, List<List<Boolean>>> {
    var count = 0
    val newMap = map.withIndex().map { (y, line) ->
        line.withIndex().map { (x, tile) ->
            when (tile && ADJACENT.mapNotNull { map.getOrNull((x by y) + it) }.count { it } < 4) {
                true -> {
                    count++
                    false
                }

                false -> tile
            }
        }
    }
    return count to newMap
}
