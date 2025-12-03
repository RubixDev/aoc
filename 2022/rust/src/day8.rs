use take_until::TakeUntilExt;

pub fn main(input: &str) {
    let input: Vec<Vec<u8>> = input
        .lines()
        .map(|line| line.as_bytes().iter().map(|tree| tree - b'0').collect())
        .collect();
    println!("--- Day 8 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1(input: &[Vec<u8>]) -> usize {
    let edge_count = 2 * input.len() + 2 * input[0].len() - 4;
    let inside_count: usize = input[1..input.len() - 1]
        .iter()
        .enumerate()
        .map(|(row_idx, row)| {
            row[1..row.len() - 1]
                .iter()
                .enumerate()
                .filter(|(col_idx, tree)| {
                    (0..row_idx + 1).all(|idx| input[idx][*col_idx + 1] < **tree)
                        || (row_idx + 2..input.len()).all(|idx| input[idx][*col_idx + 1] < **tree)
                        || (0..*col_idx + 1).all(|idx| input[row_idx + 1][idx] < **tree)
                        || (col_idx + 2..row.len()).all(|idx| input[row_idx + 1][idx] < **tree)
                })
                .count()
        })
        .sum();
    edge_count + inside_count
}

fn part2(input: &[Vec<u8>]) -> usize {
    input
        .iter()
        .enumerate()
        .flat_map(|(row_idx, row)| {
            row.iter().enumerate().map(move |(col_idx, tree)| {
                (0..row_idx)
                    .rev()
                    .take_until(|idx| input[*idx][col_idx] >= *tree)
                    .count()
                    * (row_idx + 1..input.len())
                        .take_until(|idx| input[*idx][col_idx] >= *tree)
                        .count()
                    * (0..col_idx)
                        .rev()
                        .take_until(|idx| input[row_idx][*idx] >= *tree)
                        .count()
                    * (col_idx + 1..row.len())
                        .take_until(|idx| input[row_idx][*idx] >= *tree)
                        .count()
            })
        })
        .max()
        .unwrap()
}
