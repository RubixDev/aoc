package de.rubixdev.aoc

object Day04 extends Day {
  override def run(rawInput: String): Unit = {
    val input = rawInput.linesIterator
      .map(_.map(_ == '@'))
      .toList

    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
  }

  private def shouldRemove(
      map: Seq[Seq[Boolean]],
      tile: Boolean,
      x: Int,
      y: Int,
  ): Boolean =
    tile && ADJACENT
      .flatMap { offset => map.lift2d((x by y) + offset) }
      .count(identity) < 4

  private def part1(input: List[Seq[Boolean]]) = input.view.zipWithIndex.map {
    (line, y) =>
      line.zipWithIndex.count { (tile, x) => shouldRemove(input, tile, x, y) }
  }.sum

  private def part2(input: List[Seq[Boolean]]) =
    LazyList
      .iterate(0 -> (input: Seq[Seq[Boolean]])) { (_, map) => step(map) }
      .drop(1)
      .map(_._1)
      .takeWhile(_ != 0)
      .sum

  private def step(map: Seq[Seq[Boolean]]) = map.view.zipWithIndex
    .map { (line, y) =>
      line.view.zipWithIndex
        .map { (tile, x) =>
          if (shouldRemove(map, tile, x, y)) 1 -> false
          else 0 -> tile
        }
        .foldLeftFirst(0)(_ + _)
    }
    .foldLeftFirst(0)(_ + _)
}
