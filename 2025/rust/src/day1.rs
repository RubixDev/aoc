pub fn main(input: &str) {
    let input = input
        .replace('L', "-")
        .replace('R', "")
        .lines()
        .map(|line| line.parse())
        .collect::<Result<Vec<_>, _>>()
        .expect("malformed input");
    println!("--- Day 1 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1(input: &[i32]) -> usize {
    input
        .iter()
        .copied()
        .scan(50, |dial, n| {
            *dial += n;
            *dial %= 100;
            Some(*dial)
        })
        .filter(|&dial| dial == 0)
        .count()
}

fn part2(input: &[i32]) -> usize {
    input
        .iter()
        .copied()
        .flat_map(|n| std::iter::repeat_n(n.signum(), n.unsigned_abs() as usize))
        .scan(50, |dial, n| {
            *dial += n;
            *dial %= 100;
            Some(*dial)
        })
        .filter(|&dial| dial == 0)
        .count()
}
