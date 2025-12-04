package de.rubixdev.aoc

fun day9(input: String): Day = sequence {
    val regex = """^(\w+) to (\w+) = (\d+)$""".toRegex()
    val example = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> regex.matchEntire(line)!!.groupValues }
        .associate { (_, from, to, weight) -> (from to to) to weight.toInt() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun allPathDistances(input: Map<Pair<String, String>, Int>): Sequence<Int> {
    val connections = input.mapKeys { it.key.toSet() }
    val nodes = input.keys.flatMap { it.toSet() }.toSet()
    return nodes.permutations().filter { perm -> perm.windowed(2).all { it.toSet() in connections } }
        .map { perm -> perm.windowed(2).sumOf { connections[it.toSet()]!! } }
}

private fun part1(input: Map<Pair<String, String>, Int>) = allPathDistances(input).min()
private fun part2(input: Map<Pair<String, String>, Int>) = allPathDistances(input).max()
