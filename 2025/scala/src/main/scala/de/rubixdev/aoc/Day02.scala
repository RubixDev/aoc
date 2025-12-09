package de.rubixdev.aoc

import scala.util.matching.Regex

object Day02 extends Day {
  override def run(rawInput: String): Unit = {
    val input = rawInput.trim
      .split(',')
      .map(_.split('-').map(_.toLong))
      .map { nums => nums(0) to nums(1) }
      .toList

    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
  }

  private def part(input: List[Iterable[Long]], regex: Regex) =
    input.view.flatten.filter { num => regex.matches(num.toString) }.sum

  private def part1(input: List[Iterable[Long]]) =
    part(input, Regex("^([0-9]+)\\1$"))
  private def part2(input: List[Iterable[Long]]) =
    part(input, Regex("^([0-9]+)\\1+$"))
}
