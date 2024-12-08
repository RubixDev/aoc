package de.rubixdev

import java.io.File

private data class Guard(
    var pos: Vec2D,
    var facing: Direction,
)

private enum class TileState {
    FREE,
    OBSTACLE,
    VISITED,
}

fun runDay6() {
    val guard = Guard(
        facing = Direction.UP,
        pos = -1 by -1,
    )
    val map = File("inputs/day6.txt").readLines()
        .withIndex()
        .map { (y, line) ->
            line.withIndex().map { (x, tile) ->
                when (tile) {
                    '.' -> TileState.FREE
                    '#' -> TileState.OBSTACLE
                    '^' -> {
                        guard.pos = x by y
                        TileState.VISITED
                    }

                    else -> throw RuntimeException("malformed input")
                }
            }.toMutableList()
        }
    println("--- Day 6 ---")
    println("Part 1: ${part1(map.map { it.toMutableList() }, guard.copy())}")
    println("Part 2: ${part2(map, guard)}")
}

private fun part1(map: List<MutableList<TileState>>, guard: Guard): Int {
    while (true) {
        val nextPos = guard.facing.move(guard.pos)
        if (!nextPos.isInBounds(map)) {
            map[guard.pos] = TileState.VISITED
            break
        }
        if (map[nextPos] == TileState.OBSTACLE) {
            guard.facing = guard.facing.rotateRight()
            continue
        }
        map[guard.pos] = TileState.VISITED
        guard.pos = nextPos
    }

    return map.sumOf { line -> line.count { it == TileState.VISITED } }
}

private fun part2(map: List<MutableList<TileState>>, guard: Guard): Int {
    var count = 0
    for (pos in map.first().indices.flatMap { x -> map.indices.map { y -> x by y } }.filter { it != guard.pos }) {
        val visited = mutableSetOf<Guard>()
        val modifiedMap = map.map { it.toMutableList() }.also { it[pos] = TileState.OBSTACLE }
        val guardClone = guard.copy()
        while (true) {
            if (!visited.add(guardClone.copy())) {
                count++
                break
            }
            val nextPos = guardClone.facing.move(guardClone.pos)
            if (!nextPos.isInBounds(modifiedMap)) break
            if (modifiedMap[nextPos] == TileState.OBSTACLE) {
                guardClone.facing = guardClone.facing.rotateRight()
                continue
            }
            guardClone.pos = nextPos
        }
    }

    return count
}
