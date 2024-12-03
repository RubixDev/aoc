package de.rubixdev.aoc2020

fun runDay13() {
    println("--- DAY 13 ---")

    val rawInput = object {}.javaClass.getResource("/input13.txt")!!.readText().split('\n')
    val input = rawInput[0].toInt() to rawInput[1]

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: Pair<Int, String>) {
    val busIds = input.second.replace("x,", "").split(',').map { it.toInt() }
    val minTimes = busIds.map { (input.first / it.toFloat()).toInt() + 1 }
    val idsByTimeDiffs = minTimes.mapIndexed { i, it -> (busIds[i] * it - input.first) to busIds[i] }.toMap()
    val minDiff = idsByTimeDiffs.keys.minOrNull()!!
    println(minDiff * idsByTimeDiffs[minDiff]!!)
}

private fun part2(input: Pair<Int, String>) {
    val busses = input.second.split(',').mapIndexedNotNull { i, it -> if (it == "x") null else i to it.toLong() }
    var stepSize = busses[0].second
    var minute = 0L

    for ((offset, id) in busses.drop(1)) {
        while ((minute + offset) % id != 0L) {
            minute += stepSize
        }
        stepSize *= id
    }
    println(minute)
}
