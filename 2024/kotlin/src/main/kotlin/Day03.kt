package de.rubixdev

import java.io.File

fun day3(): Day = sequence {
    val input = File("inputs/day3.txt").readText()

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: String): Int =
    Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        .findAll(input).sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }

private fun part2(input: String): Int {
    var on = true
    var sum = 0
    Regex("""mul\((\d{1,3}),(\d{1,3})\)|do(n't)?\(\)""")
        .findAll(input).forEach {
            when (it.value) {
                "do()" -> on = true
                "don't()" -> on = false
                else -> if (on) {
                    val (a, b) = it.destructured
                    sum += a.toInt() * b.toInt()
                }
            }
        }
    return sum
}
