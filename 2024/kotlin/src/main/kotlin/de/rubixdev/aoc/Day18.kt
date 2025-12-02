package de.rubixdev.aoc

private val SIZE = 70 by 70

fun day18(input: String): Day = sequence {
    val input = input.lines()
        .map { line -> line.split(",").map { it.toInt() }.toVec2() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Vec2>): Int =
    findPath(input.take(1024).toSet()) ?: throw RuntimeException("no path found")

private fun findPath(corrupted: Set<Vec2>): Int? {
    // bfs once again
    val visited = mutableSetOf(0 by 0)
    val queue = ArrayDeque(listOf(0 to (0 by 0)))
    while (queue.isNotEmpty()) {
        val (moves, pos) = queue.removeFirst()
        if (pos == SIZE) return moves
        for (dir in Direction.entries) {
            val nextPos = pos + dir.vec
            if (nextPos.isInBounds(SIZE) && nextPos !in corrupted && visited.add(nextPos)) {
                queue.add(moves + 1 to nextPos)
            }
        }
    }
    return null
}

private fun part2(input: List<Vec2>): String {
    // binary search
    var low = 0
    var high = input.lastIndex
    while (low <= high) {
        val mid = (low + high) ushr 1
        when (findPath(input.take(mid).toSet())) {
            null -> high = mid - 1
            else -> low = mid + 1
        }
    }
    return input[low].let { "${it.x},${it.y}" }
}
