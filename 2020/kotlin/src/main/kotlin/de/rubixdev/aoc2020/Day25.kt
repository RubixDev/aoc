package de.rubixdev.aoc2020

fun runDay25() {
    println("--- DAY 25 ---")

    val rawInput = object {}.javaClass.getResource("/input25.txt")!!.readText().split('\n')
    val input = Pair(rawInput[0].toInt(), rawInput[1].toInt())

    part1(input)
    println()
}

private fun part1(input: Pair<Int, Int>) {
    var value = 1L
    var loopSize = 0

    while (value != input.first.toLong()) {
        value *= 7
        value %= 20201227
        loopSize++
    }

    value = 1
    for (i in 1..loopSize) {
        value *= input.second
        value %= 20201227
    }
    println(value)
}
