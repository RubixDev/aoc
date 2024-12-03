pub fn run() {
    println!("--- DAY 05 ---");
    let input: Vec<[[u16; 2]; 2]> = include_str!("resources/input05.txt")
        .split('\n')
        .map(|line| line.split(" -> "))
        .map(|line| line.map(|coord| coord.split(',').collect::<Vec<&str>>()).collect::<Vec<Vec<&str>>>())
        .map(|line| [
            [line[0][0].parse::<u16>().unwrap(), line[0][1].parse::<u16>().unwrap()],
            [line[1][0].parse::<u16>().unwrap(), line[1][1].parse::<u16>().unwrap()],
        ])
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<[[u16; 2]; 2]>) {
    let lines: Vec<&[[u16; 2]; 2]> = input
        .iter()
        .filter(|line| line[0][0] == line[1][0] || line[0][1] == line[1][1])
        .collect();
    let biggest_x: u16 = lines.iter().map(|line| line[0][0].max(line[1][0])).max().unwrap() + 1;
    let biggest_y: u16 = lines.iter().map(|line| line[0][1].max(line[1][1])).max().unwrap() + 1;
    let mut floor: Vec<Vec<u8>> = vec![vec![0; biggest_x.into()]; biggest_y.into()];

    for line in lines.iter() {
        if line[0][0] == line[1][0] {
            for y in line[0][1].min(line[1][1])..=line[0][1].max(line[1][1]) {
                floor[y as usize][line[0][0] as usize] += 1;
            }
        } else {
            for x in line[0][0].min(line[1][0])..=line[0][0].max(line[1][0]) {
                floor[line[0][1] as usize][x as usize] += 1;
            }
        }
    }

    println!("{}", floor.iter().flatten().filter(|field| field >= &&2).count());
}

fn part2(input: &Vec<[[u16; 2]; 2]>) {
    let biggest_x: u16 = input.iter().map(|line| line[0][0].max(line[1][0])).max().unwrap() + 1;
    let biggest_y: u16 = input.iter().map(|line| line[0][1].max(line[1][1])).max().unwrap() + 1;
    let mut floor: Vec<Vec<u8>> = vec![vec![0; biggest_x.into()]; biggest_y.into()];

    for line in input.iter() {
        if line[0][0] == line[1][0] {
            for y in line[0][1].min(line[1][1])..=line[0][1].max(line[1][1]) {
                floor[y as usize][line[0][0] as usize] += 1;
            }
        } else if line[0][1] == line[1][1] {
            for x in line[0][0].min(line[1][0])..=line[0][0].max(line[1][0]) {
                floor[line[0][1] as usize][x as usize] += 1;
            }
        } else {
            let x_start: u16 = line[0][0];
            let x_step: i8 = if x_start < line[1][0] { 1 } else { -1 };
            let mut x: i16 = x_start as i16;
            let y_start: u16 = line[0][1];
            let y_step: i8 = if y_start < line[1][1] { 1 } else { -1 };
            let mut y: i16 = y_start as i16;

            while x != line[1][0] as i16 + x_step as i16 {
                floor[y as usize][x as usize] += 1;
                x += x_step as i16;
                y += y_step as i16;
            }
        }
    }

    println!("{}", floor.iter().flatten().filter(|field| field >= &&2).count());
}

