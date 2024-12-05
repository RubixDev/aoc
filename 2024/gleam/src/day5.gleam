import gleam/int
import gleam/io
import gleam/list
import gleam/order
import gleam/result
import gleam/string
import simplifile

pub fn main() {
  let #(rules, updates) =
    simplifile.read("inputs/day5.txt")
    |> result.map_error(fn(_) { Nil })
    |> result.try(fn(text) { text |> string.split_once("\n\n") })
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  let rules =
    rules
    |> string.split("\n")
    |> list.map(fn(line) {
      use #(a, b) <- result.try(line |> string.split_once("|"))
      use a <- result.try(int.parse(a))
      use b <- result.map(int.parse(b))
      #(a, b)
    })
    |> result.all
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  let updates =
    updates
    |> string.split("\n")
    |> list.filter(fn(line) { !string.is_empty(line) })
    |> list.map(fn(line) {
      line
      |> string.split(",")
      |> list.map(fn(n) { int.parse(n) })
      |> result.all
    })
    |> result.all
    |> result.lazy_unwrap(fn() { panic as "malformed input" })
  io.println("--- Day 5 ---")
  io.println("Part 1: " <> int.to_string(part1(rules, updates)))
  io.println("Part 2: " <> int.to_string(part2(rules, updates)))
}

fn is_ordered_correctly(update: List(Int), rules: List(#(Int, Int))) -> Bool {
  update
  |> list.index_map(fn(n, idx) { #(idx, n) })
  |> list.all(fn(tup) {
    rules
    |> list.filter(fn(rule) { rule.0 == tup.1 })
    |> list.all(fn(rule) {
      !{ update |> list.contains(rule.1) }
      || { update |> list.drop(tup.0 + 1) |> list.contains(rule.1) }
    })
  })
}

fn sum_updates(updates: List(List(Int))) -> Int {
  updates
  |> list.map(fn(update) {
    update
    |> list.drop(list.length(update) / 2)
    |> list.first()
    |> result.lazy_unwrap(fn() { panic as "empty update" })
  })
  |> int.sum
}

fn part1(rules, updates) {
  updates
  |> list.filter(is_ordered_correctly(_, rules))
  |> sum_updates
}

fn part2(rules, updates) {
  updates
  |> list.filter(fn(update) { !is_ordered_correctly(update, rules) })
  |> list.map(fn(update) {
    update
    |> list.sort(fn(a, b) {
      case
        { rules |> list.any(fn(rule) { rule == #(a, b) }) },
        { rules |> list.any(fn(rule) { rule == #(b, a) }) }
      {
        True, _ -> order.Lt
        _, True -> order.Gt
        _, _ -> order.Eq
      }
    })
  })
  |> sum_updates
}
