package de.rubixdev

import java.io.File
import kotlin.math.pow

fun runDay7() {
    val example = "190: 10 19\n" +
            "3267: 81 40 27\n" +
            "83: 17 5\n" +
            "156: 15 6\n" +
            "7290: 6 8 6 15\n" +
            "161011: 16 10 13\n" +
            "192: 17 8 14\n" +
            "21037: 9 7 18 13\n" +
            "292: 11 6 16 20"
//    val input = example.lines()
    val input = File("inputs/day7.txt").readLines()
        .map { line ->
            line.split(": ").let {
                it[0].toLong() to it[1].split(" ").map(String::toLong)
            }
        }
    println("--- Day 7 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun allPermutations(base: Int, digits: Int): List<List<Char>> =
    // a very hacky way to generate all permutations
    (0..<base.toFloat().pow(digits).toInt()).map { perm ->
        perm.toString(base).padStart(digits, '0').toList()
    }

private fun part1(input: List<Pair<Long, List<Long>>>): Long {
    return input.filter { (testValue, nums) ->
        allPermutations(2, nums.lastIndex).any { perm ->
            perm.zip(nums.drop(1))
                .fold(nums.first()) { acc, (bit, num) ->
                    when (bit) {
                        '0' -> acc + num
                        '1' -> acc * num
                        else -> throw IllegalStateException()
                    }
                } == testValue
        }
    }.sumOf { it.first }
}

private fun part2(input: List<Pair<Long, List<Long>>>): Long {
    return input.filter { (testValue, nums) ->
        allPermutations(3, nums.lastIndex).any { perm ->
            perm.zip(nums.drop(1))
                .fold(nums.first()) { acc, (bit, num) ->
                    when (bit) {
                        '0' -> acc + num
                        '1' -> acc * num
                        '2' -> "$acc$num".toLong()
                        else -> throw IllegalStateException()
                    }
                } == testValue
        }
    }.sumOf { it.first }
}
