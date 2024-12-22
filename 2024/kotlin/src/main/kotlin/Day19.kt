package de.rubixdev

import java.io.File

fun day19(): Day = sequence {
    val example = """
    r, wr, b, g, bwu, rb, gb, br

    brwrr
    bggr
    gbbr
    rrbgbr
    ubwu
    bwurrg
    brgr
    bbrgwb
    """.trimIndent()
//    val (rawTowels, rawPatterns) = example.split("\n\n")
    val (rawTowels, rawPatterns) = File("inputs/day19.txt").readText().trim().split("\n\n")
    val towels = rawTowels.split(", ")
    val patterns = rawPatterns.lines()

    yield(Unit)
    yield(part1(towels, patterns))
    yield(part2(towels, patterns))
}

private fun part1(towels: List<String>, patterns: List<String>): Int {
    // this could easily also be solved almost identically to part 2, but this is more fun :)
    val re = Regex(towels.joinToString("|", "^(", ")+$"))
    return patterns.count { re.matches(it) }
}

private fun part2(towels: List<String>, patterns: List<String>): Long =
    patterns.sumOf { countArrangements(towels, it) }

private var cache = mutableMapOf<String, Long>()
private fun countArrangements(towels: List<String>, pattern: String): Long =
    when (pattern) {
        "" -> 1
        else -> cache.getOrPut(pattern) {
            towels.filter { pattern.startsWith(it) }.sumOf { towel ->
                countArrangements(towels, pattern.removePrefix(towel))
            }
        }
    }
