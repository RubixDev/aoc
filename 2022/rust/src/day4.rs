use std::ops::RangeInclusive;

use itertools::Itertools;

pub fn main() {
    let input: Vec<RangeInclusive<u8>> = include_str!("../inputs/day4.txt")
        .lines()
        .flat_map(|line| line.split(',').flat_map(|split| split.split('-')))
        .tuples()
        .map(|(start, end)| start.parse().unwrap()..=end.parse().unwrap())
        .collect();
    println!("--- Day 4 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1(input: &[RangeInclusive<u8>]) -> usize {
    input
        .iter()
        .tuples()
        .filter(|(left, right)| {
            (left.contains(right.start()) && left.contains(right.end()))
                || (right.contains(left.start()) && right.contains(left.end()))
        })
        .count()
}

fn part2(input: &[RangeInclusive<u8>]) -> usize {
    input
        .iter()
        .tuples()
        .filter(|(left, right)| {
            left.contains(right.start())
                || left.contains(right.end())
                || right.contains(left.start())
        })
        .count()
}
