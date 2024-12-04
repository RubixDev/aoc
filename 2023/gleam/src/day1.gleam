import gleam/function
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile

pub fn main() {
  let assert Ok(input) =
    simplifile.read("inputs/day1.txt")
    |> result.map(fn(text) {
      string.split(text, "\n")
      // |> list.filter(fn(line) { !string.is_empty(line) })
    })
  io.debug(run_part(input, part1))
  io.debug(run_part(input, part2))
}

fn run_part(
  input: List(String),
  find_digit: fn(String, Bool) -> Int,
) -> Int {
  input
  |> list.map(fn(line) { 10 * find_digit(line, False) + find_digit(line, True) })
  |> int.sum()
}

fn part1(line: String, last from_back: Bool) -> Int {
  line
  |> string.to_graphemes()
  |> case from_back {
    True -> list.reverse(_)
    False -> function.identity(_)
  }
  |> list.find_map(int.parse)
  |> result.unwrap(0)
}

fn part2(line: String, last from_back: Bool) -> Int {
  line
  |> string.to_graphemes()
  // |> enumerate()
  |> list.index_map(fn(a, b) { #(b, a) })
  |> case from_back {
    True -> list.reverse(_)
    False -> function.identity(_)
  }
  |> list.find_map(fn(c) {
    int.parse(c.1)
    |> result.try_recover(fn(_) {
      case string.drop_start(line, c.0) {
        "zero" <> _ -> Ok(0)
        "one" <> _ -> Ok(1)
        "two" <> _ -> Ok(2)
        "three" <> _ -> Ok(3)
        "four" <> _ -> Ok(4)
        "five" <> _ -> Ok(5)
        "six" <> _ -> Ok(6)
        "seven" <> _ -> Ok(7)
        "eight" <> _ -> Ok(8)
        "nine" <> _ -> Ok(9)
        _ -> Error(Nil)
      }
    })
  })
  |> result.unwrap(0)
}
// fn enumerate(list: List(a)) -> List(#(Int, a)) {
//   list.fold(list, from: #([], 0), with: fn(a, e) {
//     #([#(a.1, e), ..a.0], a.1 + 1)
//   }).0
//   |> list.reverse()
// }
