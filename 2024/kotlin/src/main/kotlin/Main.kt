package de.rubixdev

import kotlin.time.measureTime

typealias Day = Sequence<Any>

private val DAYS: List<() -> Day> = listOf(
    ::day1,
    ::day2,
    ::day3,
    ::day4,
    ::day5,
    ::day6,
    ::day7,
    ::day8,
    ::day9,
    ::day10,
    ::day11,
    ::day12,
    ::day13,
    ::day14,
    ::day15,
    ::day16,
    ::day17,
    ::day18,
    ::day19,
    ::day20,
    ::day21,
    ::day22,
    ::day23,
    ::day24,
    ::day25,
)

fun main() {
    val total = measureTime {
        for ((idx, day) in DAYS.withIndex()) {
            println("--- Day ${idx + 1} ---")
            val total = measureTime {
                val iter = day().iterator()
                val time0 = measureTime { iter.next() }
                val time1 = measureTime { println("Part 1: ${iter.next()}") }
                val time2 = measureTime { println("Part 2: ${iter.next()}") }
                println("\u001b[90mParsing $time0\u001b[0m")
                println("\u001b[90mPart 1  $time1\u001b[0m")
                println("\u001b[90mPart 2  $time2\u001b[0m")
            }
            println("\u001b[90mTotal   $total\u001b[0m\n")
        }
    }
    println("\u001b[90mTotal Execution Time: $total\u001b[0m")
}
