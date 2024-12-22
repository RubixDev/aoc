package de.rubixdev

import java.io.File
import kotlin.collections.mutableSetOf

private data class Guard(
    var pos: Vec2,
    var facing: Direction,
)

private enum class TileState {
    FREE,
    OBSTACLE,
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
                        TileState.FREE
                    }

                    else -> throw RuntimeException("malformed input")
                }
            }
        }
    println("--- Day 6 ---")
    val visited = part1(map, guard.copy())
    println("Part 1: ${visited.size}")
    println("Part 2: ${part2(map, guard, visited)}")
}

private fun part1(map: List<List<TileState>>, guard: Guard): Set<Vec2> {
    val visited = mutableSetOf(guard.pos)
    while (true) {
        val nextPos = guard.pos + guard.facing.vec
        if (!nextPos.isInBounds(map)) {
            visited.add(guard.pos)
            break
        }
        if (map[nextPos] == TileState.OBSTACLE) {
            guard.facing = guard.facing.rotateRight()
            continue
        }
        visited.add(guard.pos)
        guard.pos = nextPos
    }

    return visited
}

private fun part2(map: List<List<TileState>>, guard: Guard, tryBlocking: Set<Vec2>): Int {
    return tryBlocking.parallelStream().filter { it != guard.pos }.map { pos ->
        val visited = mutableSetOf<Guard>()
        val modifiedMap = map.map { it.toMutableList() }.also { it[pos] = TileState.OBSTACLE }
        val guardClone = guard.copy()
        var count = 0
        while (true) {
            if (!visited.add(guardClone.copy())) {
                count++
                break
            }
            val nextPos = guardClone.pos + guardClone.facing.vec
            if (!nextPos.isInBounds(modifiedMap)) break
            if (modifiedMap[nextPos] == TileState.OBSTACLE) {
                guardClone.facing = guardClone.facing.rotateRight()
                continue
            }
            guardClone.pos = nextPos
        }
        count
    }.reduce(0) { acc, e -> acc + e }
}
