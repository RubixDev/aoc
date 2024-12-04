import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile

type Input =
  List(List(Int))

pub fn main() {
  let input =
    simplifile.read("inputs/day2.txt")
    |> result.map_error(fn(_) { Nil })
    |> result.try(fn(text) {
      text
      |> string.split("\n")
      |> list.filter(fn(line) { !string.is_empty(line) })
      |> list.map(fn(line) {
        string.split(line, " ")
        |> list.map(fn(num) { int.parse(num) })
        |> result.all()
      })
      |> result.all()
    })
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  io.println("--- Day 2 ---")
  io.println("Part 1: " <> int.to_string(part1(input)))
  io.println("Part 2: " <> int.to_string(part2(input)))
}

fn is_safe(report: List(Int)) -> Bool {
  report
  |> list.window_by_2()
  |> list.all(fn(window) {
    window.1 <= window.0 + 3 && window.1 >= window.0 + 1
  })
}

fn part1(input: Input) -> Int {
  list.count(input, fn(report) {
    is_safe(report) || is_safe(list.reverse(report))
  })
}

fn part2(input: Input) -> Int {
  list.count(input, fn(report) {
    is_safe(report)
    || is_safe(list.reverse(report))
    || {
      report
      |> list.index_map(fn(_, i) {
        let #(left, right) = list.split(report, i)
        list.append(left, list.drop(right, 1))
      })
      |> list.any(fn(report) {
        is_safe(report) || is_safe(list.reverse(report))
      })
    }
  })
}
