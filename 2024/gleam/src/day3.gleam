import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile

pub fn main() {
  let input =
    simplifile.read("inputs/day3.txt")
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  io.println("--- Day 3 ---")
  io.println("Part 1: " <> int.to_string(part1(input)))
  io.println("Part 2: " <> int.to_string(part2(input)))
}

fn part1(input: String) -> Int {
  parse_part1(input, [])
  |> list.map(fn(tup) { tup.0 * tup.1 })
  |> int.sum
}

fn parse_part1(input: String, acc: List(#(Int, Int))) -> List(#(Int, Int)) {
  case input {
    "" -> acc
    "mul(" <> rest ->
      case
        {
          use num1 <- result.try(get_int(rest))
          let rest =
            rest
            |> string.drop_start({ num1 |> int.to_string |> string.length } + 1)
          use num2 <- result.try(get_int(rest))
          case
            rest |> string.drop_start(num2 |> int.to_string |> string.length)
          {
            ")" <> _ -> Ok(#(num1, num2))
            _ -> Error(Nil)
          }
        }
      {
        Ok(nums) -> parse_part1(rest, [nums, ..acc])
        Error(Nil) -> parse_part1(rest, acc)
      }
    _ -> parse_part1(string.drop_start(input, 1), acc)
  }
}

fn get_int(input: String) -> Result(Int, Nil) {
  input
  |> string.to_graphemes
  |> list.take(3)
  |> list.take_while(fn(c) { int.parse(c) |> result.is_ok })
  |> string.join("")
  |> int.parse
}

fn part2(input: String) -> Int {
  parse_part2(input, [], True).0
  |> list.map(fn(tup) { tup.0 * tup.1 })
  |> int.sum
}

fn parse_part2(
  input: String,
  acc: List(#(Int, Int)),
  enabled: Bool,
) -> #(List(#(Int, Int)), Bool) {
  case input {
    "" -> #(acc, enabled)
    "do()" <> rest -> parse_part2(rest, acc, True)
    "don't()" <> rest -> parse_part2(rest, acc, False)
    "mul(" <> rest if enabled ->
      case
        {
          use num1 <- result.try(get_int(rest))
          let rest =
            rest
            |> string.drop_start({ num1 |> int.to_string |> string.length } + 1)
          use num2 <- result.try(get_int(rest))
          case
            rest |> string.drop_start(num2 |> int.to_string |> string.length)
          {
            ")" <> _ -> Ok(#(num1, num2))
            _ -> Error(Nil)
          }
        }
      {
        Ok(nums) -> parse_part2(rest, [nums, ..acc], enabled)
        Error(Nil) -> parse_part2(rest, acc, enabled)
      }
    _ -> parse_part2(string.drop_start(input, 1), acc, enabled)
  }
}
