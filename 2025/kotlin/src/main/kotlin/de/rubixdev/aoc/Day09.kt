package de.rubixdev.aoc

import kotlin.math.max
import kotlin.math.min
import kotlin.sequences.map

fun day9(input: String): Day = sequence {
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

private fun findC(
    edges: List<Pair<Vec2, Vec2>>,
    a: Vec2,
    b: Vec2,
    inAxis: Vec2.() -> Long,
    crossAxis: Vec2.() -> Long,
    toPositive: Boolean,
): Vec2 {
    // every edge, that we "hit" and not just slide by
    val candidates = edges.filter { (c, d) ->
        c.inAxis() == d.inAxis() && (
            // assumes every edge is at least 3 long
            (min(a.crossAxis(), b.crossAxis())..max(a.crossAxis(), b.crossAxis()))
                .overlaps(
                    (min(c.crossAxis(), d.crossAxis()) + 1)..<max(c.crossAxis(), d.crossAxis()),
                )
            )
    }
    return when (toPositive) {
        true -> candidates.filter { it.first.inAxis() > a.inAxis() }.minBy {
            it.first.inAxis() - a.inAxis()
        }

        false -> candidates.filter { it.first.inAxis() < a.inAxis() }.minBy {
            a.inAxis() - it.first.inAxis()
        }
    }.first // second would be corner d
}

private fun part2(input: List<Vec2>): Long {
    val edges = input.zipWithNext().plus(input.last() to input.first())
    // fill entire area with rectangles (not most optimal tiling and lots of overlaps, but good enough)
    val area = edges.asSequence().map { (a, b) ->
        // horizontal and vertical stripes
        when (a.x == b.x) {
            true -> findC(edges, a, b, Vec2::x::get, Vec2::y::get, a.y > b.y).x by a.y to b
            false -> a.x by findC(edges, a, b, Vec2::y::get, Vec2::x::get, a.x < b.x).y to b
        }
    }
        // normalize to top-left and bottom-right corners
        .map { (a, b) -> (min(a.x, b.x) by min(a.y, b.y)) to (max(a.x, b.x) by max(a.y, b.y)) }
        .toSet()

    val rectString = area.joinToString(", ", "[", "]") { (a, b) ->
        "[(${a.x}, ${a.y}), (${b.x}, ${b.y})]"
    }
    println("area modelled with with these rectangles: $rectString")
    val pairs = input.asSequence()
        .withIndex()
        .flatMap { (idx1, a) -> input.asSequence().drop(idx1 + 1).map { a to it } }
        .toList()

    println("testing ${pairs.size} pairs of corners")
    return pairs.parallelStream()
        .filter { (a, b) ->
            // build edges of rectangle assuming opposite corners.
            // this detection can probably be vastly improved over checking every single point
            sequenceOf(
                (min(a.x, b.x)..max(a.x, b.x)).asSequence().map { it by a.y },
                (min(a.x, b.x)..max(a.x, b.x)).asSequence().map { it by b.y },
                (min(a.y, b.y)..max(a.y, b.y)).asSequence().map { a.x by it },
                (min(a.y, b.y)..max(a.y, b.y)).asSequence().map { b.x by it },
            ).flatten().all { edgePoint -> area.any { edgePoint in it } }
        }
        .map { (a, b) -> (a - b).abs().plus(Vec2(1)).area() }
        // java streams are annoying
        .max(Comparator.comparingLong { it }).orElseThrow()
}
