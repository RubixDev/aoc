package de.rubixdev

import java.io.File

fun runDay5() {
    val (rawRules, rawUpdates) = File("inputs/day5.txt").readText().split("\n\n")
    val rules = rawRules.split("\n").map { line ->
        line.split("|").map { it.toInt() }.toPair()
    }
    val updates = rawUpdates.split("\n").filter { it.isNotBlank() }
        .map { line -> line.split(",").map { it.toInt() } }

    println("--- Day 5 ---")
    println("Part 1: ${part1(rules, updates)}")
    println("Part 2: ${part2(rules, updates)}")
}

private fun List<Int>.isOrderedCorrectly(rules: List<Pair<Int, Int>>): Boolean =
    withIndex().all { (idx, page) ->
        rules.filter { it.first == page }.all { rule ->
            rule.second !in this || rule.second in drop(idx + 1)
        }
    }

private fun part1(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { it.isOrderedCorrectly(rules) }.sumOf { it[it.size / 2] }
}

private fun part2(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { !it.isOrderedCorrectly(rules) }.map { update ->
        update.sortedWith { a, b ->
            if (rules.any { it == a to b }) -1
            else if (rules.any { it == b to a }) 1
            else 0
        }
    }.sumOf { it[it.size / 2] }
}
