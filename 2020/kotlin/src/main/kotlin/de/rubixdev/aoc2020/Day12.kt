package de.rubixdev.aoc2020

import kotlin.math.absoluteValue

fun runDay12() {
    println("--- DAY 12 ---")

    val input = object {}.javaClass.getResource("/input12.txt")!!.readText().split('\n').map { it[0] to it.substring(1).toShort() }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input:List<Pair<Char, Short>>) {
    var direction = 90
    var pos = 0 to 0

    for ((instruction, value) in input) {
        when (instruction) {
            'N' -> pos = pos.copy(second = pos.second + value)
            'E' -> pos = pos.copy(first = pos.first + value)
            'S' -> pos = pos.copy(second = pos.second - value)
            'W' -> pos = pos.copy(first = pos.first - value)
            'L' -> direction = Math.floorMod(direction - value, 360)
            'R' -> direction = Math.floorMod(direction + value, 360)
            'F' -> {
                when (direction) {
                    0 -> pos = pos.copy(second = pos.second + value)
                    90 -> pos = pos.copy(first = pos.first + value)
                    180 -> pos = pos.copy(second = pos.second - value)
                    270 -> pos = pos.copy(first = pos.first - value)
                }
            }
        }
    }
    println(pos.first.absoluteValue + pos.second.absoluteValue)
}

private fun part2(input:List<Pair<Char, Short>>) {
    var waypointPos = 10 to 1
    var shipPos = 0 to 0

    for ((instruction, value) in input) {
        when (instruction) {
            'N' -> waypointPos = waypointPos.copy(second = waypointPos.second + value)
            'E' -> waypointPos = waypointPos.copy(first = waypointPos.first + value)
            'S' -> waypointPos = waypointPos.copy(second = waypointPos.second - value)
            'W' -> waypointPos = waypointPos.copy(first = waypointPos.first - value)
            'F' -> shipPos = (shipPos.first + (waypointPos.first * value)) to (shipPos.second + (waypointPos.second * value))
            'L' -> {
                when (value.toInt()) {
                    90 -> waypointPos = (-waypointPos.second) to (waypointPos.first)
                    180 -> waypointPos = (-waypointPos.first) to (-waypointPos.second)
                    270 -> waypointPos = (waypointPos.second) to (-waypointPos.first)
                }
            }
            'R' -> {
                when (value.toInt()) {
                    90 -> waypointPos = (waypointPos.second) to (-waypointPos.first)
                    180 -> waypointPos = (-waypointPos.first) to (-waypointPos.second)
                    270 -> waypointPos = (-waypointPos.second) to (waypointPos.first)
                }
            }
        }
    }
    println(shipPos.first.absoluteValue + shipPos.second.absoluteValue)
}
