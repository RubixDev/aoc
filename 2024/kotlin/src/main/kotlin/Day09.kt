package de.rubixdev

import java.io.File

fun runDay9() {
    val example = "2333133121414131402"
//    val input = example
    val input = File("inputs/day9.txt").readText().trim()
        .toList()
        .map { it.digitToInt() }
    println("--- Day 9 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<Int>): Long {
    val disk = input.chunked(2).flatMapIndexed { id, chunk ->
        (1..chunk[0]).map { id } + when (val emptySpace = chunk.getOrNull(1)) {
            null -> listOf()
            else -> (1..emptySpace).map { -1 }
        }
    }.toMutableList()
    while (-1 in disk) {
        disk[disk.indexOf(-1)] = disk.removeLast()
    }
    return disk.withIndex().sumOf { (idx, id) -> idx * id.toLong() }
}

private fun part2(input: List<Int>): Long {
    val disk = input.asSequence().chunked(2).flatMapIndexed { id, chunk ->
        listOf(chunk[0] to id) + when (val emptySpace = chunk.getOrNull(1)) {
            null -> listOf()
            else -> listOf(emptySpace to -1)
        }
    }.toMutableList()
    for ((fileSize, fileId) in disk.reversed().filter { it.second != -1 }) {
        disk.indexOfFirst { it.second == -1 && it.first >= fileSize }.let { idx ->
            if (idx >= 0 && idx < disk.indexOf(fileSize to fileId)) {
                disk[idx] = disk[idx].first - fileSize to disk[idx].second
                disk.indexOf(fileSize to fileId).let { oldIdx ->
                    disk[oldIdx] = fileSize to -1
                }
                disk.add(idx, fileSize to fileId)
            }
        }
    }
    var idx = -1
    return disk.sumOf { (size, id) ->
        (1..size).sumOf {
            idx++
            idx * id.coerceAtLeast(0).toLong()
        }
    }
}
