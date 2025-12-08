package de.rubixdev.aoc

import scala.collection.immutable.VectorMap

private val DAYS: Days = VectorMap(
  Day01 -> 1,
  Day02 -> 2,
  Day03 -> 3,
  Day04 -> 4,
//  Day05 -> 5,
//  Day06 -> 6,
//  Day07 -> 7,
//  Day08 -> 8,
//  Day09 -> 9,
//  Day10 -> 10,
//  Day11 -> 11,
//  Day12 -> 12,
)

@main
def main(): Unit = runDays(DAYS, 2025)
