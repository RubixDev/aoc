package de.rubixdev

import java.io.File

fun runDay8() {
    val example = "............\n" +
            "........0...\n" +
            ".....0......\n" +
            ".......0....\n" +
            "....0.......\n" +
            "......A.....\n" +
            "............\n" +
            "............\n" +
            "........A...\n" +
            ".........A..\n" +
            "............\n" +
            "............"
    val rawInput = File("inputs/day8.txt").readLines()
//    val bounds = example.lines().map { it.toList() }
    val bounds = rawInput.map { it.toList() }
//    val input = example.lines()
    val input = rawInput
        .withIndex().flatMap { (y, line) ->
            line.withIndex().map { (x, char) ->
                char to (x by y)
            }
        }
        .groupBy { it.first }
        .filterKeys { it != '.' }
        .values
        .map { l -> l.map { it.second } }
    println("--- Day 8 ---")
    println("Part 1: ${part1(input, bounds)}")
    println("Part 2: ${part2(input, bounds)}")
}

private fun part1(input: List<List<Vec2d>>, bounds: Collection<Collection<*>>): Int {
    return input.flatMap { positions ->
        positions.withIndex().flatMap { (i, a) ->
            positions.drop(i + 1).flatMap { b ->
                val diff = a - b
                mutableListOf<Vec2d>().apply {
                    (a + diff).let { if (it.isInBounds(bounds)) add(it) }
                    (b - diff).let { if (it.isInBounds(bounds)) add(it) }
                }
            }
        }
    }.distinct().size
}

private fun part2(input: List<List<Vec2d>>, bounds: Collection<Collection<*>>): Int {
    return input.flatMap { positions ->
        positions.withIndex().flatMap { (i, a) ->
            positions.drop(i + 1).flatMap { b ->
                val diff = (a - b).reduce()
                mutableListOf<Vec2d>().apply {
                    var search = a.copy()
                    while (search.isInBounds(bounds)) {
                        add(search.copy())
                        search += diff
                    }
                    search = a.copy() - diff
                    while (search.isInBounds(bounds)) {
                        add(search.copy())
                        search -= diff
                    }
                }
            }
        }
    }.distinct().size
}
