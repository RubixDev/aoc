package de.rubixdev.aoc

extension [A, B, R](seq: Iterable[(A, B)]) {
  def foldLeftFirst(initial: R)(op: (R, A) => R): (R, Seq[B]) =
    seq.foldLeft(initial) { (acc, e) => op(acc, e._1) } -> seq.map(_._2).toSeq
}
