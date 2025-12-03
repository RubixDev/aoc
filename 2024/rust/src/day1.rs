use itertools::Itertools;

type Input = (Vec<u32>, Vec<u32>);

pub fn main(input: &str) {
    let input = input
        .lines()
        .map(|line| {
            line.split_once("   ")
                .and_then(|(a, b)| Some((a.parse::<u32>().ok()?, b.parse::<u32>().ok()?)))
        })
        .collect::<Option<Input>>()
        .expect("malformed input");
    println!("--- Day 1 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1((left, right): &Input) -> u32 {
    left.iter()
        .sorted()
        .zip(right.iter().sorted())
        .map(|(l, r)| l.abs_diff(*r))
        .sum()
}

fn part2((left, right): &Input) -> u32 {
    left.iter()
        .map(|n| n * right.iter().filter(|m| n == *m).count() as u32)
        .sum()
}
