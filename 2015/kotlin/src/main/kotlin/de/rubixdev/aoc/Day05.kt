package de.rubixdev.aoc

fun day5(input: String): Day = sequence {
    val input = input.lines()

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<String>) = input.count { line ->
    line.count { it in "aeiou" } >= 3 &&
        line.windowed(2).any { it[0] == it[1] } &&
        listOf("ab", "cd", "pq", "xy").all { it !in line }
}

private fun part2(input: List<String>) = input.count { line ->
    line.windowed(2).withIndex().any { (i1, w1) ->
        line.windowed(2).withIndex().any { (i2, w2) ->
            w1 == w2 && i2 > i1 + 1
        }
    } && line.windowed(3).any { it[0] == it[2] }
}
