pub fn run() {
    println!("--- DAY 01 ---");
    let input: Vec<u16> = include_str!("resources/input01.txt")
        .split('\n')
        .map(|num| num.parse::<u16>().unwrap())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<u16>) {
    println!(
        "{}",
        input
            .iter()
            .enumerate()
            .filter(|(index, num)| index != &0 && num > &&input[index - 1])
            .count()
    );
}

fn part2(input: &Vec<u16>) {
    let processed: Vec<u16> = input
        .iter()
        .enumerate()
        .map(|(index, num)| {
            if index > input.len() - 3 {
                0
            } else {
                num + input[index + 1] + input[index + 2]
            }
        })
        .collect();
    println!(
        "{}",
        processed
            .iter()
            .enumerate()
            .filter(|(index, num)| index != &0 && num > &&processed[index - 1])
            .count()
    );
}
