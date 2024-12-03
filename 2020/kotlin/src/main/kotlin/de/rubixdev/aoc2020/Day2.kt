package de.rubixdev.aoc2020

fun runDay2() {
    println("--- DAY 02 ---")

    val rawInput = object {}.javaClass.getResource("/input02.txt")!!.readText().split('\n')
    val input: MutableList<List<Any>> = mutableListOf()
    for (line in rawInput) {
        input.add(mutableListOf(
            line.split("-")[0].toByte(),
            line.split("-")[1].split(" ")[0].toByte(),
            line.split(": ")[0].last(),
            line.split(": ")[1]
        ))
    }
    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<List<Any>>) {
    var count = 0
    for (pass in input) {
        if (pass[3].toString().count { it == pass[2] } in (pass[0] as Byte)..(pass[1] as Byte)) {
            count++
        }
    }
    println(count)
}

private fun part2(input: List<List<Any>>) {
    var count = 0
    for (pass in input) {
        val password = pass[3].toString()
        if ((password[(pass[0] as Byte) - 1] == pass[2]) xor (password[(pass[1] as Byte) - 1] == pass[2])) {
            count++
        }
    }
    println(count)
}
