package de.rubixdev.aoc

fun day9(input: String): Day = sequence {
    val example = "2333133121414131402"
//    val input = example
    val input = input
        .map { it.digitToInt() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Int>): Long {
    val disk = input.asSequence().chunked(2).flatMapIndexed { id, chunk ->
        (1..chunk[0]).map { id } + when (val emptySpace = chunk.getOrNull(1)) {
            null -> listOf()
            else -> (1..emptySpace).map { -1 }
        }
    }.toMutableList()
    while (true) {
        val idx = disk.indexOf(-1)
        if (idx < 0) break
        disk[idx] = disk.removeLast()
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
                disk[idx] = disk[idx].run { copy(first = first - fileSize) }
                disk.indexOf(fileSize to fileId).let { oldIdx ->
                    disk[oldIdx] = fileSize to -1
                }
                disk.add(idx, fileSize to fileId)
            }
        }
    }
    return disk.asSequence().flatMap { (size, id) -> (1..size).asSequence().map { id } }
        .withIndex()
        .sumOf { (idx, id) -> idx * id.coerceAtLeast(0).toLong() }
}
