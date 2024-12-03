package de.rubixdev.aoc2020

fun runDay1() {
    println("--- DAY 01 ---")

    val rawInput: List<String> = object {}.javaClass.getResource("/input01.txt")!!.readText().split('\n')
    val input = rawInput.convertToShorts()

    part1(input)
    println()
    part2(input)
    println()
}

private fun List<String>.convertToShorts(): List<Short> {
    val out: MutableList<Short> = mutableListOf()
    for (e in this) {
        out.add(e.toShort())
    }
    return out
}

private fun part1(input: List<Short>) {
    for ((count1, num1) in input.withIndex()) {
        for (num2 in input.subList(count1, input.size - 1)) {
            if (num1 + num2 == 2020) {
                println("$num1 + $num2 = ${num1 + num2}")
                println("$num1 * $num2 = ${num1 * num2}")
            }
        }
    }
}

private fun part2(input: List<Short>) {
    for ((count1, num1) in input.withIndex()) {
        for ((count2, num2) in input.subList(count1, input.size - 1).withIndex()) {
            for (num3 in input.subList(count2, input.size - 1)) {
                if (num1 + num2 + num3 == 2020) {
                    println("$num1 + $num2 + $num3 = ${num1 + num2 + num3}")
                    println("$num1 * $num2 * $num3 = ${num1 * num2 * num3}")
                }
            }
        }
    }
}
