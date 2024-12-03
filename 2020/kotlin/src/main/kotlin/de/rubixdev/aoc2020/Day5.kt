package de.rubixdev.aoc2020

fun runDay5() {
    println("--- DAY 05 ---")

    val rawInput = object {}.javaClass.getResource("/input05.txt")!!.readText().split('\n')
    val input: MutableList<Int> = mutableListOf()
    for (line in rawInput) {
        val row = line.substring(0..6).replace('F', '0').replace('B', '1').toInt(2)
        val col = line.substring(7..9).replace('L', '0').replace('R', '1').toInt(2)
        input.add(row * 8 + col)
    }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<Int>) {
    println(input.maxOrNull())
}

private fun part2(input: List<Int>) {
    for (id in input.minOrNull()!!..input.maxOrNull()!!) {
        if (id !in input && id - 1 in input && id + 1 in input) {
            println(id)
            return
        }
    }
}
