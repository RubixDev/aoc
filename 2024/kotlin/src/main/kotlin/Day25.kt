package de.rubixdev

import java.io.File

fun day25(): Day = sequence {
    val example = """
    #####
    .####
    .####
    .####
    .#.#.
    .#...
    .....

    #####
    ##.##
    .#.##
    ...##
    ...#.
    ...#.
    .....

    .....
    #....
    #....
    #...#
    #.#.#
    #.###
    #####

    .....
    .....
    #.#..
    ###..
    ###.#
    ###.#
    #####

    .....
    .....
    .....
    #....
    #.#..
    #.#.#
    #####
    """.trimIndent()
    // val input = example.split("\n\n")
    val input = File("inputs/day25.txt").readText().trim().split("\n\n")
    val locks = input.filter { schem -> schem.lines().first() == "#####" }
        .map { schem -> (0..4).map { schem.lines().drop(1).count { line -> line[it] == '#' } } }
    val keys = input.filter { schem -> schem.lines().first() == "....." }
        .map { schem -> (0..4).map { schem.lines().drop(1).count { line -> line[it] == '#' } - 1 } }

    yield(Unit)
    yield(part1(locks, keys))
    yield("Chronicle Delivered :)")
}

private fun part1(locks: List<List<Int>>, keys: List<List<Int>>): Long =
    locks.sumOf { lock ->
        keys.count { key ->
            key.zip(lock).all { (k, l) -> (k + l) < 6 }
        }.toLong()
    }
