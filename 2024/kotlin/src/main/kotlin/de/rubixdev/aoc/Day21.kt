package de.rubixdev.aoc

import kotlin.math.abs

fun day21(input: String): Day = sequence {
    val example = """
    029A
    980A
    179A
    456A
    379A
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()

    yield(null)
    yield(part(input, 3))
    yield(part(input, 26))
}

private val KEYPAD = """
789
456
123
 0A
""".trimIndent().lines().map { it.toList() }

private val DIRPAD = """
 ^A
<v>
""".trimIndent().lines().map { it.toList() }

private val KEYPAD_SHORTEST_PATHS = getAllShortestPaths(KEYPAD)
private val DIRPAD_SHORTEST_PATHS = getAllShortestPaths(DIRPAD)

private typealias ShortestPaths = Map<Pair<Char, Char>, List<String>>

private fun getAllShortestPaths(map: List<List<Char>>): ShortestPaths =
    map.withIndex().flatMap { (y, line) ->
        line.withIndex().filter { it.value != ' ' }.flatMap { (x, button) ->
            map.withIndex().flatMap { (targetY, targetLine) ->
                targetLine.withIndex().filter { it.value != ' ' }.map { (targetX, targetButton) ->
                    val dx = targetX - x
                    val dy = targetY - y
                    val horizontal = if (dx < 0) Direction.LEFT else Direction.RIGHT
                    val vertical = if (dy < 0) Direction.UP else Direction.DOWN
                    (button to targetButton) to buildList {
                        repeat(abs(dx)) { add(horizontal) }
                        repeat(abs(dy)) { add(vertical) }
                    }.let { listOf(it, it.reversed()) }.filter { isValidPath(it, map, x by y) }
                        .map { path -> path.joinToString("", postfix = "A") { it.toString() } }
                }
            }
        }
    }.toMap()

private fun isValidPath(path: List<Direction>, map: List<List<Char>>, startPos: Vec2): Boolean {
    var pos = startPos
    for (dir in path) {
        pos += dir.vec
        if (map[pos] == ' ') return false
    }
    return true
}

private fun part(input: List<String>, indirections: Int): Long =
    input.map { code -> computeForNextRobot(code, indirections) }
        .zip(input)
        .sumOf { (sequence, code) -> sequence * code.replace("A", "").toLong() }

private val cache = mutableMapOf<Pair<String, Int>, Long>()
private fun computeForNextRobot(
    code: String,
    depth: Int,
    pad: ShortestPaths = KEYPAD_SHORTEST_PATHS,
): Long = when (depth) {
    0 -> code.length.toLong()

    else -> cache.getOrPut(code to depth) {
        (listOf('A') + code.toList()).windowed(2)
            .map { (start, end) -> pad[start to end]!! }
            .cartesianProduct()
            .minOf { path ->
                path.sumOf { computeForNextRobot(it, depth - 1, DIRPAD_SHORTEST_PATHS) }
            }
    }
}
