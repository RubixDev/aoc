package de.rubixdev.aoc

private val DAYS: Days =
    mapOf(
        ::day1 to 1,
        ::day2 to 2,
        ::day3 to 3,
        ::day4 to 4,
        ::day5 to 5,
        ::day6 to 6,
        ::day7 to 7,
        ::day8 to 8,
        ::day8Mutable to 8,
//        ::day9 to 9,
        ::day9Fast to 9,
        ::day10 to 10,
        ::day11 to 11,
//        ::day12 to 12,
    )

fun main() = runDays(DAYS, 2025)
