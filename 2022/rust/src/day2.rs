pub fn main(input: &str) {
    let input: Vec<[i64; 2]> = input
        .lines()
        .map(|l| {
            let (elve, me) = l.split_once(' ').unwrap();
            [elve.as_bytes()[0] as i64 - 64, me.as_bytes()[0] as i64 - 87]
        })
        .collect();

    println!("--- Day 2 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn part1(input: &[[i64; 2]]) -> i64 {
    // win situations:
    // 3 1
    // 1 2
    // 2 3

    // win: (elve - me).rem_euclid(3) == 2
    // draw: elve == me
    // lost: otherwise
    input
        .iter()
        .map(|[elve, me]| {
            me + if (elve - me).rem_euclid(3) == 2 {
                6
            } else if elve == me {
                3
            } else {
                0
            }
        })
        .sum()
}

fn part2(input: &[[i64; 2]]) -> i64 {
    // win shape: (elve % 3) + 1
    // lose shape: ((elve + 1) % 3) + 1
    input
        .iter()
        .map(|[elve, result]| match result {
            1 => ((elve + 1) % 3) + 1, // lose shape + 0 points
            2 => elve + 3,             // draw shape + 3 points
            3 => (elve % 3) + 1 + 6,   // win shape + 6 points
            _ => unreachable!(),
        })
        .sum()
}
