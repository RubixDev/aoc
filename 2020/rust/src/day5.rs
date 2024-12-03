pub fn run() {
    println!("--- DAY 05 ---");

    let input: Vec<u16> = include_str!("resources/input05.txt")
        .split('\n')
        .map(|seat| (&seat[..7], &seat[7..]))
        .map(|seat| {
            (
                seat.0.replace("B", "1").replace("F", "0"),
                seat.1.replace("R", "1").replace("L", "0"),
            )
        })
        .map(|seat| {
            (
                u8::from_str_radix(seat.0.as_str(), 2).unwrap(),
                u8::from_str_radix(seat.1.as_str(), 2).unwrap(),
            )
        })
        .map(|seat| (seat.0 as u16) * 8 + (seat.1 as u16))
        .collect::<Vec<u16>>();

    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<u16>) {
    println!("{}", input.iter().max().unwrap());
}

fn part2(input: &Vec<u16>) {
    println!(
        "{}",
        input
            .iter()
            .find(|seat| !input.contains(&(*seat + 1)) && input.contains(&(*seat + 2)))
            .unwrap()
            + 1
    )
}
