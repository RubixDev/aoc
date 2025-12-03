pub fn main(input: &str) {
    let mut input = input
        .split("\n\n")
        .map(|inv| inv.lines().map(|cnt| cnt.parse::<u64>().unwrap()).sum())
        .collect::<Vec<_>>();
    println!("--- Day 1 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&mut input));
}

fn part1(input: &[u64]) -> u64 {
    *input.iter().max().unwrap()
}

fn part2(input: &mut [u64]) -> u64 {
    input.sort_unstable();
    input.iter().rev().take(3).sum()
}
