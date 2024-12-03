use std::mem;

use itertools::Itertools;
use nom::{
    branch::alt,
    bytes::complete::tag,
    character::complete::digit1,
    combinator::{map_res, opt},
    multi::separated_list1,
    IResult,
};
use num_integer::Integer;

#[derive(Debug)]
struct Monkey {
    number: usize,
    starting_items: Vec<usize>,
    operator: Operator,
    operand: Value,
    divisible_by: usize,
    if_true: usize,
    if_false: usize,
}

#[derive(Debug)]
enum Operator {
    Add,
    Mul,
}

#[derive(Debug)]
enum Value {
    Old,
    Num(usize),
}

///////////////////////////////////////////

fn monkey(input: &str) -> IResult<&str, Monkey> {
    let (input, _) = tag("Monkey ")(input)?;
    let (input, number) = usize(input)?;
    let (input, _) = tag(":\n  Starting items: ")(input)?;
    let (input, starting_items) = separated_list1(tag(", "), usize)(input)?;
    let (input, _) = tag("\n  Operation: new = old ")(input)?;
    let (input, operator) = operator(input)?;
    let (input, _) = tag(" ")(input)?;
    let (input, operand) = value(input)?;
    let (input, _) = tag("\n  Test: divisible by ")(input)?;
    let (input, divisible_by) = usize(input)?;
    let (input, _) = tag("\n    If true: throw to monkey ")(input)?;
    let (input, if_true) = usize(input)?;
    let (input, _) = tag("\n    If false: throw to monkey ")(input)?;
    let (input, if_false) = usize(input)?;
    let (input, _) = tag("\n")(input)?;
    Ok((
        input,
        Monkey {
            number,
            starting_items,
            operator,
            operand,
            divisible_by,
            if_true,
            if_false,
        },
    ))
}

fn usize(input: &str) -> IResult<&str, usize> {
    map_res(digit1, |s: &str| s.parse::<usize>())(input)
}

fn operator(input: &str) -> IResult<&str, Operator> {
    let (input, op_str) = alt((tag("+"), tag("*")))(input)?;
    Ok((
        input,
        match op_str {
            "+" => Operator::Add,
            "*" => Operator::Mul,
            _ => unreachable!("only `+` and `*` return `Ok` above"),
        },
    ))
}

fn value(input: &str) -> IResult<&str, Value> {
    if let (input, Some(_)) = opt(tag("old"))(input)? {
        Ok((input, Value::Old))
    } else {
        let (input, number) = usize(input)?;
        Ok((input, Value::Num(number)))
    }
}

///////////////////////////////////////////

pub fn main() {
    let (_, input) =
        separated_list1(tag("\n"), monkey)(include_str!("../inputs/day11.txt")).unwrap();

    for (index, monke) in input.iter().enumerate() {
        assert_eq!(index, monke.number);
    }

    println!("--- Day 11 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn run(input: &[Monkey], rounds: usize, limiter: impl Fn(&mut usize)) -> usize {
    let mut inspect_counts = vec![0; input.len()];
    let mut monkey_items = input.iter().map(|m| m.starting_items.clone()).collect_vec();
    for _ in 0..rounds {
        for monke in input {
            for mut worry_level in mem::take(&mut monkey_items[monke.number]) {
                inspect_counts[monke.number] += 1;
                match (&monke.operator, &monke.operand) {
                    (Operator::Add, Value::Old) => worry_level += worry_level,
                    (Operator::Add, Value::Num(num)) => worry_level += num,
                    (Operator::Mul, Value::Old) => worry_level *= worry_level,
                    (Operator::Mul, Value::Num(num)) => worry_level *= num,
                }
                limiter(&mut worry_level);
                match worry_level % monke.divisible_by == 0 {
                    true => monkey_items[monke.if_true].push(worry_level),
                    false => monkey_items[monke.if_false].push(worry_level),
                }
            }
        }
    }
    // sort in reverse order
    inspect_counts.sort_unstable_by(|a, b| b.cmp(a));
    inspect_counts[0] * inspect_counts[1]
}

fn part1(input: &[Monkey]) -> usize {
    run(input, 20, |worry_level| *worry_level /= 3)
}

fn part2(input: &[Monkey]) -> usize {
    // lowest common multiple of all `divisible_by`s
    let lcm = input
        .iter()
        .map(|m| m.divisible_by)
        .reduce(|acc, num| acc.lcm(&num))
        .unwrap();
    run(input, 10_000, |worry_level| *worry_level %= lcm)
}
