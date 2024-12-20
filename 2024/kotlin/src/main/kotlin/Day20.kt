package de.rubixdev

import java.io.File

fun runDay20() {
    var startPos = 0 by 0
    var endPos = 0 by 0
    val input = File("inputs/day20.txt").readLines()
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
    val distances = findPath(input, startPos, endPos)
    println("--- Day 20 ---")
    println("Part 1: ${part1(input, distances)}")
    println("Part 2: ${part2(input, distances)}")
}

private fun findPath(map: List<List<Boolean>>, startPos: Vec2, endPos: Vec2): Map<Vec2, Int> {
    val visited = mutableSetOf(startPos)
    val queue = ArrayDeque(listOf(startPos))
    val distances = mutableMapOf(startPos to 0)
    while (queue.isNotEmpty()) {
        val pos = queue.removeFirst()
        if (pos == endPos) break
        for (dir in Direction.entries) {
            val nextPos = pos + dir.vec
            if (map.getOrNull(nextPos) == false && visited.add(nextPos)) {
                queue.add(nextPos)
                distances[nextPos] = distances[pos]!! + 1
            }
        }
    }
    return distances
}

private fun part1(map: List<List<Boolean>>, distances: Map<Vec2, Int>): Int =
    part2(map, distances, maxSteps = 2)

private fun part2(map: List<List<Boolean>>, distances: Map<Vec2, Int>, maxSteps: Int = 20): Int =
    distances.keys.parallelStream().map { pos ->
        Direction.entries.sumOf { dir ->
            (1..maxSteps).sumOf { i ->
                (0..maxSteps - i).count { j ->
                    val cheatPos = pos + i * dir.vec + j * dir.rotateRight().vec
                    map.getOrNull(cheatPos) == false && distances[cheatPos]!! >= distances[pos]!! + 100 + i + j
                }
            }
        }
    }.reduce(0) { acc, n -> acc + n }

// My original part 1 solution. Runs for about 16 minutes :)
//
//private data class Node(
//    val pos: Vec2,
//    val score: Long = 0,
//    val cheatPos: Vec2? = null,
//)
//
//private fun part1(map: List<List<Boolean>>, startPos: Vec2, endPos: Vec2): Int {
//    val normalVisited = mutableSetOf(startPos)
//    val normalQueue = ArrayDeque(listOf(startPos))
//    val normalScores = mutableMapOf(startPos to 0)
//    while (normalQueue.isNotEmpty()) {
//        val pos = normalQueue.removeFirst()
//        for (dir in Direction.entries) {
//            val nextPos = pos + dir.vec
//            if (map.getOrNull(nextPos) == false && normalVisited.add(nextPos)) {
//                normalQueue.add(nextPos)
//                normalScores[nextPos] = normalScores[pos]!! + 1
//            }
//        }
//    }
//
//    val visited = mutableSetOf<Pair<Vec2, Vec2?>>(startPos to null)
//    val queue = ArrayDeque(listOf(Node(startPos)))
//    val end = mutableListOf<Node>()
//    while (queue.isNotEmpty()) {
//        val node = queue.removeFirst()
//        if (node.pos == endPos) {
//            end.add(node)
//            println("-- $node")
//            continue
//        }
//        if (node.score > normalScores[endPos]!!) break
//        if (normalScores[node.pos]?.let { node.score > it } == true) continue
//        for (dir in Direction.entries) {
//            val nextPos = node.pos + dir.vec
//            if (map.getOrNull(nextPos) == false && visited.add(nextPos to node.cheatPos)) {
//                queue.add(Node(nextPos, node.score + 1, node.cheatPos))
//            } else if (node.cheatPos == null && map.getOrNull(nextPos) == true && map.getOrNull(nextPos + dir.vec) == false && visited.add(nextPos to nextPos) && visited.add(nextPos + dir.vec to nextPos)) {
//                queue.add(Node(nextPos + dir.vec, node.score + 2, nextPos))
//            }
//        }
//    }
//    println(end)
//    val normalTime = end.first { it.cheatPos == null }.score
//    println(end.takeWhile { it.score < normalTime }.map { normalTime - it.score }.groupingBy { it }.eachCount())
//    return end.takeWhile { it.score < normalTime }.map { normalTime - it.score }.count { it >= 100 }
//}
