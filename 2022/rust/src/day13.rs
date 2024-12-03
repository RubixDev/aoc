use std::cmp::Ordering;

use itertools::Itertools;
use nom::{
    bytes::complete::tag, character::complete::digit1, combinator::map_res, multi::separated_list0,
    sequence::tuple, IResult,
};

#[derive(Debug, Clone, PartialEq, Eq)]
enum Value {
    Int(u8),
    List(Vec<Value>),
}

////////////////////////////////

fn pair(input: &str) -> IResult<&str, [Value; 2]> {
    let (input, (left, _, right)) = tuple((value, tag("\n"), value))(input)?;
    Ok((input, [left, right]))
}

fn value(input: &str) -> IResult<&str, Value> {
    if input.chars().next().map_or(false, |char| char == '[') {
        let (input, list) = list(input)?;
        Ok((input, Value::List(list)))
    } else {
        let (input, num) = map_res(digit1, |s: &str| s.parse::<u8>())(input)?;
        Ok((input, Value::Int(num)))
    }
}

fn list(input: &str) -> IResult<&str, Vec<Value>> {
    let (input, _) = tag("[")(input)?;
    let (input, list) = separated_list0(tag(","), value)(input)?;
    let (input, _) = tag("]")(input)?;
    Ok((input, list))
}

////////////////////////////////

pub fn main() {
    let (_, input): (_, Vec<[Value; 2]>) =
        separated_list0(tag("\n\n"), pair)(include_str!("../inputs/day13.txt")).unwrap();

    println!("--- Day 13 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

////////////////////////////////

impl PartialOrd for Value {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        match (self, other) {
            (Value::Int(left), Value::Int(right)) => left.partial_cmp(right),
            (Value::List(left), Value::List(right)) => {
                let left_iter = left.iter();
                let mut right_iter = right.iter();
                for left in left_iter {
                    match right_iter.next() {
                        Some(right) => {
                            let ord = left.cmp(right);
                            if ord != Ordering::Equal {
                                return Some(ord);
                            }
                        }
                        None => return Some(Ordering::Greater),
                    }
                }
                if right_iter.next().is_some() {
                    return Some(Ordering::Less);
                }
                Some(Ordering::Equal)
            }
            (left @ Value::Int(_), right @ Value::List(_)) => {
                Value::List(vec![left.clone()]).partial_cmp(right)
            }
            (left @ Value::List(_), right @ Value::Int(_)) => {
                left.partial_cmp(&Value::List(vec![right.clone()]))
            }
        }
    }
}

impl Ord for Value {
    fn cmp(&self, other: &Self) -> Ordering {
        self.partial_cmp(other).unwrap()
    }
}

////////////////////////////////

fn part1(input: &[[Value; 2]]) -> usize {
    input
        .iter()
        .enumerate()
        .map(
            |(index, [left, right])| {
                if left < right {
                    index + 1
                } else {
                    0
                }
            },
        )
        .sum()
}

fn part2(input: &[[Value; 2]]) -> usize {
    let mut packets = input.iter().flatten().collect_vec();
    let divider_packet_1 = Value::List(vec![Value::List(vec![Value::Int(2)])]);
    let divider_packet_2 = Value::List(vec![Value::List(vec![Value::Int(6)])]);
    packets.push(&divider_packet_1);
    packets.push(&divider_packet_2);
    packets.sort_unstable();
    (packets
        .iter()
        .position(|val| val == &&divider_packet_1)
        .unwrap()
        + 1)
        * (packets
            .iter()
            .position(|val| val == &&divider_packet_2)
            .unwrap()
            + 1)
}
