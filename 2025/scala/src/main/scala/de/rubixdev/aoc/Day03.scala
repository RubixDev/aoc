package de.rubixdev.aoc

object Day03 extends Day {
  override def run(rawInput: String): Unit = {
    val input = rawInput.linesIterator
      .map(_.map(_.asDigit))
      .toList

    println("Part 1: " + part1(input))
    // TODO: part 2
//    println("Part 2: " + part2(input))
  }

  private def part1(input: List[Seq[Int]]): Int = input.view.map { line =>
    line.view.zipWithIndex.map { (b1, i1) =>
      line.drop(i1 + 1).map(_ + b1 * 10).maxOption.getOrElse(0)
    }.max
  }.sum
}
