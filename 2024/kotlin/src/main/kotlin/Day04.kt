package de.rubixdev

import java.io.File

fun runDay4() {
    val example = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
    """.trimIndent()
//    val input = example.lines()
    val input = File("inputs/day4.txt").readLines()
        .map { line -> line.toList() }
    println("--- Day 4 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private val DIAGONAL = listOf(
    -1 by -1,
    1 by -1,
    1 by 1,
    -1 by 1,
)
private val DIRECTIONS = DIAGONAL + Direction.entries.map { it.vec }

private fun part1(input: List<List<Char>>): Int =
    input.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, _) ->
            DIRECTIONS.count { dir ->
                "XMAS".withIndex().all { (i, expected) ->
                    input.getOrNull((i * dir) + (x by y)) == expected
                }
            }
        }
    }

private fun part2(input: List<List<Char>>): Int =
    input.withIndex().sumOf { (y, line) ->
        line.withIndex().filter { (_, char) -> char == 'A' }.count { (x, _) ->
            DIAGONAL.map { (x by y) + it }.joinToString("") {
                input.getOrNull(it).toString()
            } in listOf("MMSS", "SMMS", "SSMM", "MSSM")
        }
    }
