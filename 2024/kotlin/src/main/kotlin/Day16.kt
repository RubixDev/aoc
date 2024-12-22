package de.rubixdev

import java.io.File
import java.util.PriorityQueue

fun day16(): Day = sequence {
    val example = """
    #################
    #...#...#...#..E#
    #.#.#.#.#.#.#.#.#
    #.#.#.#...#...#.#
    #.#.#.#.###.#.#.#
    #...#.#.#.....#.#
    #.#.#.#.#.#####.#
    #.#...#.#.#.....#
    #.#.#####.#.###.#
    #.#.#.......#...#
    #.#.###.#####.###
    #.#.#...#.....#.#
    #.#.#.#####.###.#
    #.#.#.........#.#
    #.#.#.#########.#
    #S#.............#
    #################
    """.trimIndent()
    var startPos = 0 by 0
    var endPos = 0 by 0
//    val input = example.lines()
    val input = File("inputs/day16.txt").readLines()
        .withIndex()
        .map { (y, line) ->
            line.withIndex().map { (x, tile) ->
                when (tile) {
                    '#' -> true
                    '.' -> false
                    'S' -> false.also { startPos = x by y }
                    'E' -> false.also { endPos = x by y }
                    else -> throw RuntimeException("malformed input")
                }
            }
        }

    yield(Unit)
    yield(part1(input, startPos, endPos))
    yield(part2(input, startPos, endPos))
}

private fun part1(map: List<List<Boolean>>, startPos: Vec2, endPos: Vec2): Int {
    val scores = mutableMapOf<Pair<Vec2, Direction>, Int>().withDefault { Int.MAX_VALUE }
    val queue = PriorityQueue<Pair<Pair<Vec2, Direction>, Int>>(compareBy { it.second })
    queue.add((startPos to Direction.RIGHT) to 0)

    while (queue.isNotEmpty()) {
        val (node, currScore) = queue.poll()
        val (currPos, currDir) = node
        val allAdjacent = mutableListOf<Pair<Pair<Vec2, Direction>, Int>>()
        val nextPos = currPos + currDir.vec
        if (nextPos.isInBounds(map) && !map[nextPos]) {
            allAdjacent.add((nextPos to currDir) to 1)
        }
        allAdjacent.add((currPos to currDir.rotateRight()) to 1000)
        allAdjacent.add((currPos to currDir.rotateLeft()) to 1000)

        for ((adjacent, weight) in allAdjacent) {
            val totalScore = currScore + weight
            if (totalScore < scores.getValue(adjacent)) {
                scores[adjacent] = totalScore
                queue.add(adjacent to totalScore)
            }
        }
    }
    return scores.filter { it.key.first == endPos }.values.min()
}

// there's probably a bug in here, as it doesn't work with the second example,
// but does work with the first example and my input
private fun part2(map: List<List<Boolean>>, startPos: Vec2, endPos: Vec2): Int {
    val scores = mutableMapOf<Pair<Vec2, Direction>, Int>().withDefault { Int.MAX_VALUE }
    val paths = mutableMapOf<Pair<Vec2, Direction>, MutableSet<Pair<Vec2, Direction>>>()
    val queue = PriorityQueue<Pair<Pair<Vec2, Direction>, Int>>(compareBy { it.second })
    queue.add((startPos to Direction.RIGHT) to 0)

    while (queue.isNotEmpty()) {
        val (node, currScore) = queue.poll()
        val (currPos, currDir) = node
        val allAdjacent = mutableListOf<Pair<Pair<Vec2, Direction>, Int>>()
        val nextPos = currPos + currDir.vec
        if (nextPos.isInBounds(map) && !map[nextPos]) {
            allAdjacent.add((nextPos to currDir) to 1)
        }
        allAdjacent.add((currPos to currDir.rotateRight()) to 1000)
        allAdjacent.add((currPos to currDir.rotateLeft()) to 1000)

        for ((adjacent, weight) in allAdjacent) {
            val totalScore = currScore + weight
            if (totalScore <= scores.getValue(adjacent)) {
                scores[adjacent] = totalScore
                queue.add(adjacent to totalScore)
                paths.getOrPut(adjacent) { mutableSetOf() }.add(node)
            }
        }
    }

    val backTrackQueue = ArrayDeque(Direction.entries.map { endPos to it })
    val onPath = Direction.entries.map { endPos to it }.toMutableSet()
    while (backTrackQueue.isNotEmpty()) {
        val currPos = backTrackQueue.removeFirst()
        for (n in paths[currPos] ?: setOf()) {
            if (onPath.add(n)) {
                backTrackQueue.add(n)
            }
        }
    }

    println(map.withIndex().joinToString("\n") { (y, line) ->
        line.withIndex().joinToString("") { (x, tile) ->
            when {
                tile -> "#"
                onPath.any { it.first == x by y } -> "O"
                else -> "."
            }
        }
    })
    return onPath.map { it.first }.toSet().size
}
