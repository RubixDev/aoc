pub fn run() {
    println!("--- DAY 11 ---");
    let input: Vec<Vec<u8>> = include_str!("resources/input11.txt")
        .split('\n')
        .map(|line| line.chars().map(|num| num.to_digit(10).unwrap() as u8).collect::<Vec<u8>>())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn calculate_step(octopi: &mut Vec<Vec<u8>>) -> u16 {
    for y in 0..octopi.len() {
        for x in 0..octopi[y].len() {
            octopi[y][x] += 1;
        }
    }

    let mut flashed: Vec<(usize, usize)> = vec![];
    while octopi.iter().enumerate().any(|(y, _)| {
        octopi[y].iter().enumerate().any(|(x, octopus)| {
            !flashed.contains(&(x, y)) && octopus > &9
        })
    }) {
        let mut to_flash: Vec<(usize, usize)> = vec![];
        for y in 0..octopi.len() {
            for x in 0..octopi[y].len() {
                if octopi[y][x] > 9 && !flashed.contains(&(x, y)) {
                    to_flash.push((x, y));
                }
            }
        }
        for (x, y) in to_flash {
            flashed.push((x, y));

            if (0..octopi.len()).contains(&(y - 1)) && (0..octopi[y].len()).contains(&(x - 1)) { octopi[y - 1][x - 1] += 1; }
            if (0..octopi.len()).contains(&(y - 1)) && (0..octopi[y].len()).contains(&(x    )) { octopi[y - 1][x    ] += 1; }
            if (0..octopi.len()).contains(&(y - 1)) && (0..octopi[y].len()).contains(&(x + 1)) { octopi[y - 1][x + 1] += 1; }
            if (0..octopi.len()).contains(&(y    )) && (0..octopi[y].len()).contains(&(x - 1)) { octopi[y    ][x - 1] += 1; }
            if (0..octopi.len()).contains(&(y    )) && (0..octopi[y].len()).contains(&(x + 1)) { octopi[y    ][x + 1] += 1; }
            if (0..octopi.len()).contains(&(y + 1)) && (0..octopi[y].len()).contains(&(x - 1)) { octopi[y + 1][x - 1] += 1; }
            if (0..octopi.len()).contains(&(y + 1)) && (0..octopi[y].len()).contains(&(x    )) { octopi[y + 1][x    ] += 1; }
            if (0..octopi.len()).contains(&(y + 1)) && (0..octopi[y].len()).contains(&(x + 1)) { octopi[y + 1][x + 1] += 1; }
        }
    }
    let flashes: usize = flashed.len();

    for (x, y) in flashed {
        octopi[y][x] = 0;
    }
    return flashes as u16;
}

fn part1(input: &Vec<Vec<u8>>) {
    let mut octopi = input.clone();
    let mut flashes: u16 = 0;

    for _ in 0..100 {
        flashes += calculate_step(&mut octopi);
    }

    println!("{}", flashes);
}

fn part2(input: &Vec<Vec<u8>>) {
    let mut octopi = input.clone();
    let mut step: u16 = 0;

    while !octopi.iter().all(|row| row.iter().all(|octopus| octopus == &0)) {
        calculate_step(&mut octopi);
        step += 1;
    }

    println!("{}", step);
}
