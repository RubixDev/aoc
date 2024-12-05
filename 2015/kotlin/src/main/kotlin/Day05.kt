package de.rubixdev

import java.io.File

fun runDay5() {
    val input = File("inputs/day5.txt").readLines()
    println("--- Day 5 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>) =
    input.count { line ->
        line.count { it in "aeiou" } >= 3
                && line.windowed(2).any { it[0] == it[1] }
                && listOf("ab", "cd", "pq", "xy").all { it !in line }
    }

private fun part2(input: List<String>) =
    input.count { line ->
        line.windowed(2).withIndex().any { (i1, w1) ->
            line.windowed(2).withIndex().any { (i2, w2) ->
                w1 == w2 && i2 > i1 + 1
            }
        } && line.windowed(3).any { it[0] == it[2] }
    }
