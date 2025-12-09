package de.rubixdev.aoc

import kotlin.math.max
import kotlin.math.min
import kotlin.sequences.map

fun day9Fast(input: String): Day = sequence {
    val example = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.split(',').map { it.toInt() }.toVec2() }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<Vec2>) = input.asSequence()
    .withIndex()
    .flatMap { (idx1, a) -> input.asSequence().drop(idx1 + 1).map { a to it } }
    .maxOf { (a, b) -> (a - b).abs().plus(Vec2(1)).area() }

/**
 * Calculates a list of ranges of indices which are in bounds at the given x.
 *
 * The idea is that every second horizontal edge which lies on this x switches
 * the state between in-bounds and out-of-bounds. It's sadly not quite as
 * straight forward though and there are a few edge cases, which require us to
 * ignore certain edges based on the next or previous edge.
 *
 * Consider the following examples. Assume we are looking at the column marked
 * with `v`. From top to bottom there are three horizontal edges we come across.
 * One at `y = 1`, the next at `y = 3`, and the last at `y = 4`. However, we
 * don't want to exit the "in-bounds" state at the second edge, because we
 * actually stay in bounds. This happens, because the first and second edge are
 * both "facing" downward and overlap at only exactly the x coordinate we are
 * looking at.
 *
 * ```
 * ....v....   ....v....
 * ....#XX#.   .#XX#....
 * ....XXXX.   .XXXX....
 * .#XX#XXX.   .XXX#XX#.
 * .#XXXXX#.   .#XXXXX#.
 * .........   .........
 * ```
 *
 * Almost the same can happen on the bottom sides as well, but here we want to
 * skip the upper of the two edges instead of the lower one.
 *
 * ```
 * ....v....   ....v....
 * .#XXXXX#.   .#XXXXX#.
 * .XXX#XX#.   .#XX#XXX.
 * .XXXX....   ....XXXX.
 * .#XX#....   ....#XX#.
 * .........   .........
 * ```
 *
 * Beware that during this, we still want to treat these situations correctly:
 *
 * ```
 * .v...   ...v.
 * .#XXX   XXX#.
 * .XXXX   XXXX.
 * .#XXX   XXX#.
 * .....   .....
 * ```
 */
private fun boundsAtX(x: Long, edges: List<Pair<Vec2, Vec2>>) = edges.asSequence()
    .filter { (a, b) -> a.y == b.y && x in min(a.x, b.x)..max(a.x, b.x) }
    .sortedBy { it.first.y }
    // prefix and suffix with null, so `.windowed(3)` still has each element at the middle position once
    .let { sequenceOf(null).plus(it) }
    .plusElement(null)
    .windowed(3)
    .filter { (prev, curr, next) ->
        prev == null || next == null || !(
            (
                // prev and curr open downwards and overlap at one x in the middle
                prev.first.x < prev.second.x && curr!!.first.x < curr.second.x &&
                    max(prev.first.x, curr.first.x) == min(prev.second.x, curr.second.x)
                ) ||
                (
                    // curr and next open upwards and overlap at one x in the middle
                    curr!!.first.x > curr.second.x && next.first.x > next.second.x &&
                        min(curr.first.x, next.first.x) == max(curr.second.x, next.second.x)
                    )
            )
    }
    .map { it[1]!!.first.y }
    .windowed(2, 2) { it[0]..it[1] }
    .toList()

private fun part2(input: List<Vec2>): Long {
    val edges = input.zipWithNext().plus(input.last() to input.first())

    val includedYs = (0..input.maxOf { it.x }).map { boundsAtX(it, edges) }
    // transform the coords to do the calculation for y using `boundsAtX`
    val maxY = input.maxOf { it.y }
    val rotatedEdges = edges.map { edge -> edge.map { p -> p.mapY { it - maxY }.rotateRight() } }
    val includedXs = (0..input.maxOf { it.y }).map { boundsAtX(maxY - it, rotatedEdges) }

    return input.asSequence()
        .withIndex()
        .flatMap { (idx1, a) -> input.asSequence().drop(idx1 + 1).map { a to it } }
        .filter { (a, b) ->
            // check that all edges of the rectangle are fully in bounds
            val xRange = (min(a.x, b.x)..max(a.x, b.x))
            val yRange = (min(a.y, b.y)..max(a.y, b.y))
            includedXs[a.y.toInt()].any { xRange in it } &&
                includedXs[b.y.toInt()].any { xRange in it } &&
                includedYs[a.x.toInt()].any { yRange in it } &&
                includedYs[b.x.toInt()].any { yRange in it }
        }
        .maxOf { (a, b) -> (a - b).abs().plus(Vec2(1)).area() }
}
