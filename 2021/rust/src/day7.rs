pub fn run() {
    println!("--- DAY 07 ---");
    let input: Vec<u16> = include_str!("resources/input07.txt")
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
