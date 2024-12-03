pub fn run() {
    println!("--- DAY 02 ---");
    let input: Vec<(&str, u8)> = include_str!("resources/input02.txt")
        .split('\n')
        .map(|line| line.split(' '))
        .map(|mut line| (line.next().unwrap(), line.next().unwrap().parse::<u8>().unwrap()))
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<(&str, u8)>) {
    let mut depth: u32 = 0;
    let mut pos:   u32 = 0;
    for (instruction, num) in input.iter() {
        match instruction {
            &"forward" => pos   += *num as u32,
            &"down"    => depth += *num as u32,
            &"up"      => depth -= *num as u32,
            _ => panic!("Illegal instruction")
        }
    }
    println!("{}", depth * pos)
}

fn part2(input: &Vec<(&str, u8)>) {
    let mut depth: u32 = 0;
    let mut pos:   u32 = 0;
    let mut aim:   u32 = 0;
    for (instruction, num) in input.iter() {
        match instruction {
            &"forward" => { pos += *num as u32; depth += aim * *num as u32 },
            &"down"    =>   aim += *num as u32,
            &"up"      =>   aim -= *num as u32,
            _          => panic!("Illegal instruction")
        }
    }
    println!("{}", depth * pos)
}
