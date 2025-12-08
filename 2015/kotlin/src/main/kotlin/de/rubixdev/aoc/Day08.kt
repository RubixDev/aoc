package de.rubixdev.aoc

fun day8(input: String): Day = sequence {
    val example = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<String>) = input.sumOf { line ->
    line.length - line.replace("\\\\", "/")
        .replace("\\\"", "'")
        .replace("""\\x[a-fA-F0-9]{2}""".toRegex(), "x")
        .length + 2
}

private fun part2(input: List<String>) = input.sumOf { line ->
    line.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .length + 2 - line.length
}
