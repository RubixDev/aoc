package de.rubixdev

import java.io.File
import kotlin.math.abs

fun runDay21() {
    val example = """
    029A
    980A
    179A
    456A
    379A
    """.trimIndent()
//    val input = example.lines()
    val input = File("inputs/day21.txt").readLines()
    println("--- Day 21 ---")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
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

private typealias ShortestPaths = Map<Pair<Char, Char>, List<List<Direction>>>

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

private fun getForIndirections(input: List<String>, indirections: Int): Long {
    val keypadShortestPaths = getAllShortestPaths(KEYPAD)
    val dirpadShortestPaths = getAllShortestPaths(DIRPAD)

    val lengths = input.map { code ->
        computeForNextRobot(code, buildList {
            add(keypadShortestPaths)
            repeat(indirections) { add(dirpadShortestPaths) }
        })
    }

    return lengths.zip(input).sumOf { (sequence, code) ->
        sequence * code.replace("A", "").toLong()
    }
}

private val cache = mutableMapOf<Pair<String, Int>, Long>()
private fun computeForNextRobot(code: String, pads: List<ShortestPaths>): Long =
    when (val pad = pads.firstOrNull()) {
        null -> code.length.toLong()
        else -> cache.getOrPut(code to pads.size) {
            (listOf('A') + code.toList()).windowed(2).map { (start, end) ->
                pad[start to end]!!.map { path -> path.joinToString("", postfix = "A") { it.toString() } }
            }.cartesianProduct().map { it.joinToString("") }.minOf { path ->
                path.split('A').dropLast(1)
                    .sumOf { computeForNextRobot("${it}A", pads.drop(1)) }
            }
        }
    }


private fun part1(input: List<String>) = getForIndirections(input, 2)
private fun part2(input: List<String>) = getForIndirections(input, 25)
