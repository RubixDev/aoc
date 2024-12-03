use regex::Regex;

type Pos = (i64, i64);

pub fn main() {
    let rx = Regex::new(r"x=(-?\d+), y=(-?\d+).*is at x=(-?\d+), y=(-?\d+)").unwrap();
    let input: Vec<(Pos, Pos)> = include_str!("../inputs/day15.txt")
        .lines()
        .map(|line| {
            let captures = rx.captures(line).unwrap();
            (
                (captures[1].parse().unwrap(), captures[2].parse().unwrap()),
                (captures[3].parse().unwrap(), captures[4].parse().unwrap()),
            )
        })
        .collect();

    println!("--- Day 15 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn calc_distance(pos1: Pos, pos2: Pos) -> u64 {
    pos1.0.abs_diff(pos2.0) + pos1.1.abs_diff(pos2.1)
}

fn part1(input: &[(Pos, Pos)]) -> usize {
    const Y: i64 = 2_000_000;
    let distances: Vec<u64> = input
        .iter()
        .map(|(sensor, beacon)| calc_distance(*sensor, *beacon))
        .collect();
    let x_min = input
        .iter()
        .zip(&distances)
        .map(|((sensor, _), distance)| sensor.0 - *distance as i64)
        .min()
        .unwrap();
    let x_max = input
        .iter()
        .zip(&distances)
        .map(|((sensor, _), distance)| sensor.0 + *distance as i64)
        .max()
        .unwrap();
    let mut count = 0;
    for x in x_min..=x_max {
        for (distance, (sensor, beacon)) in distances.iter().zip(input) {
            if *beacon != (x, Y) && calc_distance(*sensor, (x, Y)) <= *distance {
                count += 1;
                break;
            }
        }
    }
    count
}

fn part2(input: &[(Pos, Pos)]) -> i64 {
    let distances: Vec<u64> = input
        .iter()
        .map(|(sensor, beacon)| calc_distance(*sensor, *beacon))
        .collect();

    // // This is way too slow to check (would take ~160 hours to complete)
    // for y in 0..=4000000 {
    //     for x in 0..=4000000 {
    //         if distances
    //             .iter()
    //             .zip(input)
    //             .all(|(distance, (sensor, beacon))| {
    //                 *sensor != (x, y)
    //                     && *beacon != (x, y)
    //                     && calc_distance(*sensor, (x, y)) > *distance
    //             })
    //         {
    //             return x * 4000000 + y;
    //         }
    //     }
    // }

    // // This takes ~40 minutes to run and prints all positions, that are just out of reach for 3
    // // or more sensors
    // let mut counts = HashMap::new();
    // let mut count = 0;
    // for (sensor, beacon) in input {
    //     dbg!(count);
    //     count += 1;
    //     let distance = calc_distance(*sensor, *beacon) as i64;
    //     let mut x_offset = 0;
    //     for y in sensor.1 - distance - 1..=sensor.1 + distance + 1 {
    //         for x in [sensor.0 - x_offset, sensor.0 + x_offset] {
    //             let pos = (x, y);
    //             match counts.get_mut(&pos) {
    //                 Some(count) => *count += 1,
    //                 None => {
    //                     counts.insert(pos, 0);
    //                 }
    //             }
    //         }
    //         match y < sensor.1 {
    //             true => x_offset += 1,
    //             false => x_offset -= 1,
    //         }
    //     }
    // }
    // println!("{:?}", counts.iter().filter(|(_, c)| c > &&2).collect_vec());

    // This is the resulting list of the above code
    let found_positions = [
        ((3963152, 2752489), 3),
        ((1020600, 2000001), 3),
        ((1029739, 1990862), 3),
        ((3270298, 2638237), 3),
        ((4064130, 2651511), 4),
        ((867361, 2153240), 3),
        ((1020601, 2000000), 3),
        ((2946825, 1712604), 3),
        ((760990, 2259611), 3),
        ((2946826, 1712605), 3),
    ];

    // go through those positions and find the one that is out of reach for all sensors
    for (pos, _) in found_positions {
        if !(0..=4000000).contains(&pos.0) || !(0..=4000000).contains(&pos.1) {
            continue;
        }
        if input
            .iter()
            .zip(&distances)
            .all(|((sensor, beacon), distance)| {
                *sensor != pos && *beacon != pos && calc_distance(*sensor, pos) > *distance
            })
        {
            return pos.0 * 4000000 + pos.1;
        }
    }

    panic!("there must be one free tile");
}
