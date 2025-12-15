package de.rubixdev.aoc

import scala.annotation.tailrec

object Day03 extends Day {
  override def run(rawInput: String): Unit = {
    val input = rawInput.linesIterator
      .map(_.map(_.asDigit))
      .toList

    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
  }

  private def part1(input: List[Seq[Int]]): Int = input.view.map { line =>
    line.view.zipWithIndex.map { (b1, i1) =>
      line.drop(i1 + 1).map(_ + b1 * 10).maxOption.getOrElse(0)
    }.max
  }.sum

  private def part2(input: List[Seq[Int]]): Long = input.view.map { line =>
    @tailrec
    def findMax(search: Seq[Int], result: List[Int] = Nil): Long =
      result.size match {
        case 12 => result.foldRight(0L) { (d, acc) => acc * 10 + d }
        case _  =>
          val (max, i) =
            search.dropRight(11 - result.size).zipWithIndex.maxBy(_._1)
          findMax(search.drop(i + 1), max :: result)
      }

    findMax(line)
  }.sum
}
