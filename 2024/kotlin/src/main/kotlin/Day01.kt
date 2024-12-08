package de.rubixdev

import java.io.File
import kotlin.math.abs

fun runDay1() {
    val input = File("inputs/day1.txt").readLines()
        .map { line -> line.split("   ").map(String::toInt).let { it[0] to it[1] } }
        .unzip()
        .map { it.sorted() }
    println("--- Day 1 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: Pair<List<Int>, List<Int>>): Int =
    input.first.zip(input.second).sumOf { (a, b) -> abs(a - b) }

private fun part2(input: Pair<List<Int>, List<Int>>): Int =
    input.first.sumOf { num -> num * input.second.count { it == num } }
