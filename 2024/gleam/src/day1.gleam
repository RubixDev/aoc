import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile

type Input =
  #(List(Int), List(Int))

pub fn main() {
  let input =
    simplifile.read("inputs/day1.txt")
    |> result.map_error(fn(_) { Nil })
    |> result.try(fn(text) {
      string.split(text, "\n")
      |> list.filter(fn(line) { !string.is_empty(line) })
      |> list.map(fn(line) {
        use strings <- result.map(string.split_once(line, "   "))
        #(
          int.parse(strings.0)
            |> result.lazy_unwrap(fn() { panic as "malformed int" }),
          int.parse(strings.1)
            |> result.lazy_unwrap(fn() { panic as "malformed int" }),
        )
      })
      |> result.all
      |> result.map(list.unzip)
    })
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  io.println("--- Day 1 ---")
  io.println("Part 1: " <> int.to_string(part1(input)))
  io.println("Part 2: " <> int.to_string(part2(input)))
}

fn part1(input: Input) -> Int {
  let left = input.0 |> list.sort(by: int.compare)
  let right = input.1 |> list.sort(by: int.compare)
  list.zip(left, right)
  |> list.map(fn(e) { int.absolute_value(e.0 - e.1) })
  |> int.sum()
}

fn part2(input: Input) -> Int {
  input.0
  |> list.map(fn(num) {
    num * { input.1 |> list.count(fn(num2) { num == num2 }) }
  })
  |> int.sum()
}
