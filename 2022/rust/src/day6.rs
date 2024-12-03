use itertools::Itertools;

pub fn main() {
    let input = include_bytes!("../inputs/day6.txt");
    println!("--- Day 6 ---");
    println!("Part 1: {}", find_unique_window(input, 4));
    println!("Part 2: {}", find_unique_window(input, 14));
}

fn find_unique_window(input: &[u8], size: usize) -> usize {
    input
        .windows(size)
        .take_while(|window| !window.iter().all_unique())
        .count()
        + size
}
