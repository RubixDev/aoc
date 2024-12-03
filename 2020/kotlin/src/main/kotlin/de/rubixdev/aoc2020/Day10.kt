package de.rubixdev.aoc2020

fun runDay10() {
    println("--- DAY 10 ---")

    val input = object {}.javaClass.getResource("/input10.txt")!!.readText().split('\n').map { it.toInt() }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<Int>) {
    var oneDiffs = 0
    var threeDiffs = 1
    var prevAdapter = 0

    for (adapter in input.sorted()) {
        when (adapter - prevAdapter) {
            1 -> oneDiffs++
            3 -> threeDiffs++
        }
        prevAdapter = adapter
    }
    println(oneDiffs * threeDiffs)
}

private fun part2(input: List<Int>) {
    val possibilitiesPerAdapter = mutableMapOf(Pair(0, 1L))

    for (adapter in input.sorted()) {
        possibilitiesPerAdapter[adapter] = (1..3).map { possibilitiesPerAdapter.getOrDefault(adapter - it, 0) }.sum()
    }

    println(possibilitiesPerAdapter[input.maxOrNull()])
}
