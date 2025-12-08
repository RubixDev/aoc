package de.rubixdev.aoc

def day1(rawInput: String): Unit = {
  val input = rawInput.linesIterator
    .map(_.replace("R", "").replace('L', '-').toInt)
    .toList

  println("Part 1: " + part1(input))
  println("Part 2: " + part2(input))
}

private def part1(input: List[Int]): Int = input.view
  .scan(50) { (acc, i) => (acc + i) % 100 }
  .count(_ == 0)

private def part2(input: List[Int]): Int = input.view
  .flatMap { n => LazyList.fill(n.abs) { n.sign } }
  .scan(50) { (acc, i) => (acc + i) % 100 }
  .count(_ == 0)
