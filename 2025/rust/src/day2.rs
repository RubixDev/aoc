use std::ops::RangeInclusive;

use itertools::Itertools;

pub fn main() {
    let input = include_str!("../inputs/day2.txt")
        .trim()
        .split(',')
        .map(|range| {
            range
                .split_once('-')
                .and_then(|(a, b)| Some(a.parse().ok()?..=b.parse().ok()?))
        })
        .collect::<Option<Vec<_>>>()
        .expect("malformed input");
    println!("--- Day 2 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn is_valid(id: u64, ns: impl Iterator<Item = usize>) -> bool {
    let id = id.to_string();
    for n in ns {
        if n > id.len() {
            break;
        }
        if id.len().is_multiple_of(n) && id.as_bytes().chunks_exact(id.len() / n).all_equal() {
            return true;
        }
    }
    return false;
}

fn part(input: &[RangeInclusive<u64>], ns: impl Iterator<Item = usize> + Clone) -> u64 {
    input
        .iter()
        .cloned()
        .flatten()
        .filter(|&n| is_valid(n, ns.clone()))
        .sum()
}

fn part1(input: &[RangeInclusive<u64>]) -> u64 {
    part(input, 2..=2)
}

fn part2(input: &[RangeInclusive<u64>]) -> u64 {
    part(input, 2..)
}
