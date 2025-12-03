use std::cmp::Ordering;

use itertools::Itertools;

pub fn main(input: &str) {
    let (rules, updates) = input.split_once("\n\n").unwrap();
    let rules = rules
        .lines()
        .map(|line| line.split_once('|').unwrap())
        .map(|(a, b)| (a.parse().unwrap(), b.parse().unwrap()))
        .collect_vec();
    let updates = updates
        .lines()
        .map(|line| {
            line.split(',')
                .map(|num| num.parse().unwrap())
                .collect_vec()
        })
        .collect_vec();
    println!("--- Day 5 ---");
    println!("Part 1: {}", part1(&rules, &updates));
    println!("Part 2: {}", part2(&rules, &updates));
}

fn is_ordered_correctly(update: &[u32], rules: &[(u32, u32)]) -> bool {
    update.iter().enumerate().all(|(idx, page)| {
        rules
            .iter()
            .filter(|(a, _)| a == page)
            .all(|(_, b)| !update.contains(b) || update[idx + 1..].contains(b))
    })
}

fn part1(rules: &[(u32, u32)], updates: &[Vec<u32>]) -> u32 {
    updates
        .iter()
        .filter(|update| is_ordered_correctly(update, rules))
        .map(|update| update[update.len() / 2])
        .sum()
}

fn part2(rules: &[(u32, u32)], updates: &[Vec<u32>]) -> u32 {
    updates
        .iter()
        .filter(|update| !is_ordered_correctly(update, rules))
        .map(|update| {
            update
                .iter()
                .sorted_unstable_by(|&&a, &&b| {
                    if rules.iter().any(|&rule| rule == (a, b)) {
                        Ordering::Less
                    } else if rules.iter().any(|&rule| rule == (b, a)) {
                        Ordering::Greater
                    } else {
                        Ordering::Equal
                    }
                })
                .collect_vec()
        })
        .map(|update| update[update.len() / 2])
        .sum()
}
