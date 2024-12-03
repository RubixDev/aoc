pub fn run() {
    println!("--- DAY 06 ---");

    let input: Vec<Vec<&str>> = include_str!("resources/input06.txt")
        .split("\n\n")
        .map(|group| group.split('\n').collect::<Vec<&str>>())
        .collect::<Vec<Vec<&str>>>();

    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<Vec<&str>>) {
    let mut distinct_answers: Vec<char> = Vec::from([]);
    println!(
        "{}",
        input
            .iter()
            .map(|group| {
                distinct_answers.clear();
                group
                    .join("")
                    .chars()
                    .filter(|answer| {
                        if !distinct_answers.contains(answer) {
                            distinct_answers.push(*answer);
                            true
                        } else {
                            false
                        }
                    })
                    .count() as u16
            })
            .sum::<u16>()
    )
}

fn part2(input: &Vec<Vec<&str>>) {
    println!(
        "{}",
        input
            .iter()
            .map(|group| {
                let mut all_yes: Vec<char> = group[0].chars().collect::<Vec<char>>();
                for answers in group {
                    for answer in all_yes.clone() {
                        if !answers.contains(answer) {
                            all_yes
                                .remove(all_yes.iter().position(|char| char == &answer).unwrap());
                        }
                    }
                }
                all_yes.len() as u16
            })
            .sum::<u16>()
    )
}
