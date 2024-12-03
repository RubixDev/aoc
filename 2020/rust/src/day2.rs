pub fn run() {
    let re = regex::Regex::new(r"^(\d{1,2})-(\d{1,2}) ([a-z]): ([a-z]+)$").unwrap();

    println!("--- DAY 02 ---");
    let input = include_str!("resources/input02.txt")
        .split('\n')
        .map(|policy| {
            let caps = re.captures(policy).unwrap();
            (
                caps.get(1).unwrap().as_str().parse::<u8>().unwrap(),
                caps.get(2).unwrap().as_str().parse::<u8>().unwrap(),
                caps.get(3).unwrap().as_str().parse::<char>().unwrap(),
                caps.get(4).unwrap().as_str(),
            )
        })
        .collect::<Vec<(u8, u8, char, &str)>>();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<(u8, u8, char, &str)>) {
    println!(
        "{}",
        input
            .iter()
            .filter(|policy| {
                (policy.0..=policy.1)
                    .contains(&(policy.3.chars().filter(|char| char == &policy.2).count() as u8))
            })
            .count()
    )
}

fn part2(input: &Vec<(u8, u8, char, &str)>) {
    println!(
        "{}",
        input
            .iter()
            .filter(|policy| {
                (policy.3.chars().nth((policy.0 - 1) as usize).unwrap() == policy.2)
                    ^ (policy.3.chars().nth((policy.1 - 1) as usize).unwrap() == policy.2)
            })
            .count()
    )
}
