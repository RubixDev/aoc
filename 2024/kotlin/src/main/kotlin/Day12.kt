package de.rubixdev

import java.io.File

fun runDay12() {
    val example = "RRRRIICCFF\n" +
            "RRRRIICCCF\n" +
            "VVRRRCCFFF\n" +
            "VVRCCCJFFF\n" +
            "VVVVCJJCFE\n" +
            "VVIVCCJJEE\n" +
            "VVIIICJJEE\n" +
            "MIIIIIJJEE\n" +
            "MIIISIJEEE\n" +
            "MMMISSJEEE"
//    val input = example.lines()
    val input = File("inputs/day12.txt").readLines()
        .map { it.toList() }
    println("--- Day 12 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun floodRegion(
    origin: Vec2,
    map: List<List<Char>>,
    visited: MutableSet<Vec2>,
    posFoundCallback: (Vec2, List<Pair<Direction, Vec2>>) -> Unit,
) {
    val queue = ArrayDeque(listOf(origin))
    while (queue.isNotEmpty()) {
        val search = queue.removeFirst()
        if (!visited.add(search)) continue
        Direction.entries.map { it to search + it.vec }
            .filter { it.second.isInBounds(map) && map[it.second] == map[search] }
            .let { neighbours ->
                queue.addAll(neighbours.map { it.second })
                posFoundCallback(search, neighbours)
            }
    }
}

private fun part1(input: List<List<Char>>): Int {
    val visited = mutableSetOf<Vec2>()
    return input.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, _) ->
            val region = mutableListOf<Int>()
            floodRegion(x by y, input, visited) { _, neighbours ->
                region.add(4 - neighbours.size)
            }
            when (region.isNotEmpty()) {
                true -> region.size * region.sum()
                false -> 0
            }
        }
    }
}

private fun part2(input: List<List<Char>>): Int {
    val visited = mutableSetOf<Vec2>()
    return input.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, _) ->
            val region = mutableMapOf<Vec2, List<Direction>>()
            floodRegion(x by y, input, visited) { search, neighbours ->
                region[search] = Direction.entries.filter { dir -> neighbours.none { it.first == dir } }
            }
            if (region.isEmpty()) 0
            else {
                val regionSize = region.size
                region.values.removeAll { it.isEmpty() }
                var sides = 0

                // maybe I'll figure out a cleaner way at some point, but essentially this walks along each border
                // of the region and counts how many turns it does
                val border = mutableSetOf<Pair<Vec2, Direction>>()
                while (region.entries.any { it.value.isNotEmpty() && it.value.any { d -> (it.key to d) !in border } }) {
                    val (startPos, startTile) = region.entries
                        .first { it.value.isNotEmpty() && it.value.any { d -> (it.key to d) !in border } }
                    val startDirection = startTile.first { (startPos to it) !in border }
                    var pos = startPos
                    var direction = startDirection
                    var first = true
                    do {
                        while (region[pos + direction.rotateRight().vec]?.contains(direction) == true) {
                            border.add(pos to direction)
                            pos += direction.rotateRight().vec
                        }

                        if (!first && pos == startPos && direction == startDirection) break
                        first = false

                        sides++
                        border.add(pos to direction)
                        direction = when (direction.rotateRight() in region[pos]!!) {
                            true -> direction.rotateRight()
                            false -> direction.rotateLeft().also {
                                pos += direction.vec + direction.rotateRight().vec
                            }
                        }
                    } while (pos != startPos || direction != startDirection)
                }
                regionSize * sides
            }
        }
    }
}
