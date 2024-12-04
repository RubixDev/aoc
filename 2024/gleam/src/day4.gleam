import gleam/bool
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile

pub fn main() {
  let input =
    simplifile.read("inputs/day4.txt")
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
    |> string.split("\n")
    |> list.filter(fn(line) { !string.is_empty(line) })
    |> list.map(string.to_graphemes)
  io.println("--- Day 4 ---")
  io.println("Part 1: " <> int.to_string(part1(input)))
  io.println("Part 2: " <> int.to_string(part2(input)))
}

fn part1(input: List(List(String))) -> Int {
  input
  |> list.index_map(fn(line, y) {
    line
    |> list.index_map(fn(char, x) {
      bool.to_int(
        char == "X"
        && test_coord(input, x + 1, y, "M")
        && test_coord(input, x + 2, y, "A")
        && test_coord(input, x + 3, y, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x + 1, y + 1, "M")
        && test_coord(input, x + 2, y + 2, "A")
        && test_coord(input, x + 3, y + 3, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x, y + 1, "M")
        && test_coord(input, x, y + 2, "A")
        && test_coord(input, x, y + 3, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x - 1, y + 1, "M")
        && test_coord(input, x - 2, y + 2, "A")
        && test_coord(input, x - 3, y + 3, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x - 1, y, "M")
        && test_coord(input, x - 2, y, "A")
        && test_coord(input, x - 3, y, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x - 1, y - 1, "M")
        && test_coord(input, x - 2, y - 2, "A")
        && test_coord(input, x - 3, y - 3, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x, y - 1, "M")
        && test_coord(input, x, y - 2, "A")
        && test_coord(input, x, y - 3, "S"),
      )
      + bool.to_int(
        char == "X"
        && test_coord(input, x + 1, y - 1, "M")
        && test_coord(input, x + 2, y - 2, "A")
        && test_coord(input, x + 3, y - 3, "S"),
      )
    })
    |> int.sum
  })
  |> int.sum
}

fn test_coord(
  input: List(List(String)),
  x: Int,
  y: Int,
  expected: String,
) -> Bool {
  case
    x < 0
    || y < 0
    || y >= list.length(input)
    || x >= list.length(list.first(input) |> result.unwrap([]))
  {
    True -> False
    False -> {
      input
      |> list.drop(y)
      |> list.first
      |> result.lazy_unwrap(fn() {
        panic as "input must have at least one line"
      })
      |> list.drop(x)
      |> list.first
      |> result.lazy_unwrap(fn() {
        panic as "input must have at least one column"
      })
      == expected
    }
  }
}

fn part2(input: List(List(String))) -> Int {
  input
  |> list.index_map(fn(line, y) {
    line
    |> list.index_map(fn(char, x) {
      bool.to_int(
        char == "A"
        && test_coord(input, x - 1, y - 1, "M")
        && test_coord(input, x + 1, y - 1, "M")
        && test_coord(input, x + 1, y + 1, "S")
        && test_coord(input, x - 1, y + 1, "S"),
      )
      + bool.to_int(
        char == "A"
        && test_coord(input, x - 1, y - 1, "S")
        && test_coord(input, x + 1, y - 1, "M")
        && test_coord(input, x + 1, y + 1, "M")
        && test_coord(input, x - 1, y + 1, "S"),
      )
      + bool.to_int(
        char == "A"
        && test_coord(input, x - 1, y - 1, "S")
        && test_coord(input, x + 1, y - 1, "S")
        && test_coord(input, x + 1, y + 1, "M")
        && test_coord(input, x - 1, y + 1, "M"),
      )
      + bool.to_int(
        char == "A"
        && test_coord(input, x - 1, y - 1, "M")
        && test_coord(input, x + 1, y - 1, "S")
        && test_coord(input, x + 1, y + 1, "S")
        && test_coord(input, x - 1, y + 1, "M"),
      )
    })
    |> int.sum
  })
  |> int.sum
}
