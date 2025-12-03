struct Slope {
    right: u8,
    down: u8,
}

fn drive_slope(slope: Slope, map: &Vec<Vec<char>>) -> u16 {
    let mut count: u16 = 0;
    let mut pos_x: u16 = 0;
    for (y, line) in map.iter().enumerate() {
        if slope.down != 1 && y % (slope.down as usize) != 0 {
            continue;
        }
        if line[(pos_x as usize) % line.len()] == '#' {
            count += 1
        }
        pos_x += slope.right as u16
    }
    return count;
}

pub fn main(input: &str) {
    println!("--- DAY 03 ---");
    let input: Vec<Vec<char>> = input
        .split('\n')
        .map(|line| line.chars().collect())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<Vec<char>>) {
    println!("{}", drive_slope(Slope { right: 3, down: 1 }, input))
}

fn part2(input: &Vec<Vec<char>>) {
    let slopes: [Slope; 5] = [
        Slope { right: 1, down: 1 },
        Slope { right: 3, down: 1 },
        Slope { right: 5, down: 1 },
        Slope { right: 7, down: 1 },
        Slope { right: 1, down: 2 }
    ];
    println!("{}", slopes.map(|slope| {
        drive_slope(slope, input)
    }).iter().fold(1u32, |acc, &count| acc * (count as u32) )) // .product::<_>()
}
