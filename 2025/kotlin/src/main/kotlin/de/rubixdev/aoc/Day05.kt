package de.rubixdev.aoc

fun day5(input: String): Day = sequence {
    val example = """
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent().trim()
//    val (rawRanges, rawInv) = example.split("\n\n")
    val (rawRanges, rawInv) = input.split("\n\n")
    val ranges = rawRanges.lines().map { line ->
        line.split('-').map { it.toLong() }.toPair().let { (a, b) -> a..b }
    }
    val inv = rawInv.lines().map { it.toLong() }

    yield(Unit)
    yield(part1(ranges, inv))
    yield(part2(ranges))
}

private fun part1(ranges: List<LongRange>, inv: List<Long>) =
    inv.count { id -> ranges.any { id in it } }

// private fun part2(ranges: List<LongRange>) =
//    ranges.asSequence().flatten().distinct().count()

// TODO: improve
private fun part2(ranges: List<LongRange>): Long {
    var ranges = ranges
    var withoutOverlaps = mutableListOf<LongRange>()
    while (true) {
        for (range in ranges) {
            if (withoutOverlaps.any { range.first in it && range.last in it }) continue
            val idx = withoutOverlaps.indexOfFirst { it.first in range || it.last in range }
            if (idx == -1) {
                withoutOverlaps.add(range)
            } else {
                val existing = withoutOverlaps[idx]
                val start = if (existing.first in range) range.first else existing.first
                val end = if (existing.last in range) range.last else existing.last
                withoutOverlaps[idx] = start..end
            }
        }
        if (ranges == withoutOverlaps) break
        ranges = withoutOverlaps
        withoutOverlaps = mutableListOf()
    }
    return withoutOverlaps.sumOf { it.last - it.first + 1 }
}
