package de.rubixdev

import java.io.File

private typealias Pos = Pair<Int, Int>

private data class Guard(
    var pos: Pos,
    var facing: Direction,
)

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ;

    fun rotateRight() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun move(pos: Pos) = when (this) {
        UP -> pos.first to pos.second - 1
        DOWN -> pos.first to pos.second + 1
        LEFT -> pos.first - 1 to pos.second
        RIGHT -> pos.first + 1 to pos.second
    }
}

private enum class TileState {
    FREE,
    OBSTACLE,
    VISITED,
}

fun runDay6() {
    val guard = Guard(
        facing = Direction.UP,
        pos = -1 to -1,
    )
    val map = File("inputs/day6.txt").readLines()
        .withIndex()
        .map { (y, line) ->
            line.withIndex().map { (x, tile) ->
                when (tile) {
                    '.' -> TileState.FREE
                    '#' -> TileState.OBSTACLE
                    '^' -> {
                        guard.pos = x to y
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

private fun Pos.isInMap(map: List<List<TileState>>) =
    first in map.first().indices && second in map.indices

private operator fun <T> List<List<T>>.get(index: Pos) =
    this[index.second][index.first]

private operator fun <T> List<MutableList<T>>.set(index: Pos, value: T) {
    this[index.second][index.first] = value
}

private fun part1(map: List<MutableList<TileState>>, guard: Guard): Int {
    while (true) {
        val nextPos = guard.facing.move(guard.pos)
        if (!nextPos.isInMap(map)) {
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
    for (pos in map.first().indices.flatMap { x -> map.indices.map { y -> x to y } }.filter { it != guard.pos }) {
        val visited = mutableSetOf<Guard>()
        val modifiedMap = map.map { it.toMutableList() }.also { it[pos] = TileState.OBSTACLE }
        val guardClone = guard.copy()
        while (true) {
            if (!visited.add(guardClone.copy())) {
                count++
                break
            }
            val nextPos = guardClone.facing.move(guardClone.pos)
            if (!nextPos.isInMap(modifiedMap)) break
            if (modifiedMap[nextPos] == TileState.OBSTACLE) {
                guardClone.facing = guardClone.facing.rotateRight()
                continue
            }
            guardClone.pos = nextPos
        }
    }

    return count
}
