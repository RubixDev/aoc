enum Intruction {
    Addx(i32),
    Noop,
}

pub fn main() {
    let input: Vec<Intruction> = include_str!("../inputs/day10.txt")
        .lines()
        .map(|line| match &line[..4] {
            "addx" => Intruction::Addx(line[5..].parse().unwrap()),
            "noop" => Intruction::Noop,
            other => panic!("invalid instruction `{other}`"),
        })
        .collect();

    println!("--- Day 10 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1(input: &[Intruction]) -> usize {
    let mut x: i32 = 1;
    let mut cycle = 0;
    let mut total_signal_strength = 0;

    for instruction in input {
        match instruction {
            Intruction::Addx(num) => {
                part1_next_cycle(&mut cycle, x, &mut total_signal_strength);
                part1_next_cycle(&mut cycle, x, &mut total_signal_strength);
                x += num;
            }
            Intruction::Noop => {
                part1_next_cycle(&mut cycle, x, &mut total_signal_strength);
            }
        }
    }

    total_signal_strength
}

fn part1_next_cycle(cycle: &mut usize, x: i32, total_signal_strength: &mut usize) {
    const CYCLES: [usize; 6] = [20, 60, 100, 140, 180, 220];
    *cycle += 1;
    if CYCLES.contains(cycle) {
        *total_signal_strength += *cycle * x as usize
    }
}

fn part2(input: &[Intruction]) -> String {
    let mut screen = "\n".to_owned();
    let mut x: i32 = 1;
    let mut cycle = 0;

    for instruction in input {
        match instruction {
            Intruction::Addx(num) => {
                part2_next_cycle(&mut cycle, x, &mut screen);
                part2_next_cycle(&mut cycle, x, &mut screen);
                x += num;
            }
            Intruction::Noop => {
                part2_next_cycle(&mut cycle, x, &mut screen);
            }
        }
    }

    screen
}

fn part2_next_cycle(cycle: &mut usize, x: i32, screen: &mut String) {
    const CYCLES: [usize; 6] = [40, 80, 120, 160, 200, 240];
    match (x - 1..=x + 1).contains(&(*cycle as i32 % 40)) {
        true => *screen += "\x1b[42m \x1b[0m",
        false => *screen += " ",
    }
    *cycle += 1;
    if CYCLES.contains(cycle) {
        *screen += "\n";
    }
}
