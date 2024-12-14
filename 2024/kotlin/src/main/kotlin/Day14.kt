package de.rubixdev

import java.io.File

//private val SIZE = 11 by 7
private val SIZE = 101 by 103

fun runDay14() {
    val example = "p=0,4 v=3,-3\n" +
            "p=6,3 v=-1,-3\n" +
            "p=10,3 v=-1,2\n" +
            "p=2,0 v=2,-1\n" +
            "p=0,0 v=1,3\n" +
            "p=3,0 v=-2,-2\n" +
            "p=7,6 v=-1,-3\n" +
            "p=3,0 v=-1,-2\n" +
            "p=9,3 v=2,3\n" +
            "p=7,3 v=-1,2\n" +
            "p=2,4 v=2,-3\n" +
            "p=9,5 v=-3,-3"
//    val input = example.lines()
    val input = File("inputs/day14.txt").readLines()
        .map { line ->
            val (px, py, vx, vy) = Regex("-?\\d+").findAll(line).toList().map { it.value.toInt() }
            (px by py) to (vx by vy)
        }
    println("--- Day 14 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun moveRobots(robots: MutableList<Pair<Vec2, Vec2>>) {
    for ((idx, robot) in robots.withIndex()) {
        robots[idx] = robot.copy(first = (robot.first + robot.second) % SIZE)
    }
}

private fun part1(input: List<Pair<Vec2, Vec2>>): Int {
    val robots = input.toMutableList()
    for (i in 1..100) moveRobots(robots)
    val robotCounts = robots.groupingBy { it.first }.eachCount()
    val rightHalf = when (SIZE.x % 2 != 0L) {
        true -> SIZE.x / 2 + 1
        false -> SIZE.x / 2
    }
    val bottomHalf = when (SIZE.y % 2 != 0L) {
        true -> SIZE.y / 2 + 1
        false -> SIZE.y / 2
    }
    return robotCounts.entries.filter { it.key.x < SIZE.x / 2 && it.key.y < SIZE.y / 2 }.sumOf { it.value } *
            robotCounts.entries.filter { it.key.x >= rightHalf && it.key.y < SIZE.y / 2 }.sumOf { it.value } *
            robotCounts.entries.filter { it.key.x < SIZE.x / 2 && it.key.y >= bottomHalf }.sumOf { it.value } *
            robotCounts.entries.filter { it.key.x >= rightHalf && it.key.y >= bottomHalf }.sumOf { it.value }
}

private fun part2(input: List<Pair<Vec2, Vec2>>): Int {
    val robots = input.toMutableList()
    for (i in 1..Int.MAX_VALUE) {
        moveRobots(robots)
        val robotCounts = robots.groupingBy { it.first }.eachCount()
        // look for 16 robots in a row
        if (robotCounts.keys.any { pos -> (0..15).all { pos + (it by 0) in robotCounts } }) {
            printMap(robotCounts)
            return i
        }
    }
    throw RuntimeException("no christmas tree found")
}

private fun printMap(robotCounts: Map<Vec2, Int>) {
    var out = ""
    for (y in 0..<SIZE.y) {
        for (x in 0..<SIZE.x) {
            out += when (val count = robotCounts[x by y]) {
                null -> "."
                else -> when {
                    "$count".length > 1 -> "X"
                    else -> "$count"
                }
            }
        }
        out += "\n"
    }
    print(out)
}
