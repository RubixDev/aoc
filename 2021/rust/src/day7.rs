pub fn main(input: &str) {
    println!("--- DAY 07 ---");
    let input: Vec<u16> = input
        .split(',')
        .map(|it| it.parse::<u16>().unwrap())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<u16>) {
    println!(
        "{}",
        (0..=*(input.iter().max().unwrap())).map(|end_pos| {
            input.iter().map(|crab| (crab.max(&end_pos) - crab.min(&end_pos)) as u32).sum::<u32>()
        }).min().unwrap()
    )
}

fn part2(input: &Vec<u16>) {
    println!(
        "{}",
        (0..=*(input.iter().max().unwrap())).map(|end_pos| {
            input.iter()
                .map(|crab| crab.max(&end_pos) - crab.min(&end_pos))
                .map(|crab| crab as u32)
                .map(|n| (n * (n + 1)) / 2)
                .sum::<u32>()
        }).min().unwrap()
    )
}
