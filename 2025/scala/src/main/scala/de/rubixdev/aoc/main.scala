package de.rubixdev.aoc

import scala.collection.immutable.VectorMap

private val DAYS: Days = VectorMap(
  Day01 -> 1,
  Day02 -> 2,
)

@main
def main(): Unit = runDays(DAYS, 2025)
