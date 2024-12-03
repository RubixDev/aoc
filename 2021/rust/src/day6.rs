pub fn run() {
    println!("--- DAY 06 ---");
    let input: Vec<u8> = include_str!("resources/input06.txt")
        .split(',')
        .map(|it| it.parse::<u8>().unwrap())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<u8>) {
    let mut fishes: Vec<u8> = input.clone();

    for _ in 0..80 {
        for index in 0..fishes.len() {
            if fishes[index] == 0 {
                fishes[index] = 7;
                fishes.push(8);
            }
            fishes[index] -= 1;
        }
    }
    println!("{}", fishes.len())
}

fn part2(input: &Vec<u8>) {
    let mut fish: Vec<usize> = (0..9).map(|num| input.iter().filter(|fish| **fish as i32 == num).count()).collect();
    for _ in 0..256 {
        let num = fish.remove(0);
        fish[6] += num;
        fish.push(num);
    }
    println!("{}", fish.iter().sum::<usize>())
}
