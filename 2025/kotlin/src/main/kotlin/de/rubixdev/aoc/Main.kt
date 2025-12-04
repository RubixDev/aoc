package de.rubixdev.aoc

private val DAYS: Days =
    listOf(
        ::day1,
        ::day2,
        ::day3,
        ::day4,
//        ::day5,
//        ::day6,
//        ::day7,
//        ::day8,
//        ::day9,
//        ::day10,
//        ::day11,
//        ::day12,
    )

fun main() = runDays(DAYS, 2025)
