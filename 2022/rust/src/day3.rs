use std::collections::HashSet;

use itertools::Itertools;

pub fn main() {
    let input: Vec<_> = include_str!("../inputs/day3.txt").lines().collect();
    println!("--- Day 3 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn char_score(char: char) -> u64 {
    match char {
        'a'..='z' => char as u64 - 96,
        _ => char as u64 - 38,
    }
}

fn part1(input: &[&str]) -> u64 {
    input
        .iter()
        .map(|rucksack| rucksack.split_at(rucksack.len() / 2))
        .map(|(left, right)| {
            *left
                .chars()
                .collect::<HashSet<_>>()
                .intersection(&right.chars().collect())
                .next()
                .unwrap()
        })
        .map(char_score)
        .sum()
}

fn part2(input: &[&str]) -> u64 {
    input
        .iter()
        .tuples()
        .map(|(line1, line2, line3)| {
            *line1
                .chars()
                .collect::<HashSet<_>>()
                .intersection(&line2.chars().collect())
                .copied()
                .collect::<HashSet<_>>()
                .intersection(&line3.chars().collect())
                .next()
                .unwrap()
        })
        .map(char_score)
        .sum()
}
