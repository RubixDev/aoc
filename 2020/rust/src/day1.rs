pub fn run() {
    println!("--- DAY 01 ---");
    let input = include_str!("resources/input01.txt")
        .split('\n')
        .map(|num| num.parse::<u32>().unwrap())
        .collect::<Vec<u32>>();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<u32>) {
    for (index1, num1) in input.iter().enumerate() {
        for num2 in &input[index1..] {
            if num1 + num2 != 2020 { continue; }
            println!("{} + {} = {}", num1, num2, num1 + num2);
            println!("{} * {} = {}", num1, num2, num1 * num2);
        }
    }
}

fn part2(input: &Vec<u32>) {
    for (index1, num1) in input.iter().enumerate() {
        for (index2, num2) in &mut input[index1..].iter().enumerate() {
            for num3 in &input[(index1 + index2)..] {
                if num1 + num2 + num3 != 2020 { continue; }
                println!("{} + {} + {} = {}", num1, num2, num3, num1 + num2 + num3);
                println!("{} * {} * {} = {}", num1, num2, num3, num1 * num2 * num3);
            }
        }
    }
}
