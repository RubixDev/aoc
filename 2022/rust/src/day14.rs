use std::{
    fmt::{self, Display, Formatter},
    thread,
    time::Duration,
};

use itertools::Itertools;

const TEST: bool = false;
const DEBUG_PART_1: bool = false;
const DEBUG_PART_2: bool = false;
const DEBUG_SLEEP_MILLIS: u64 = 0;

type Pos = (usize, usize);

#[derive(Clone, Copy, PartialEq, Eq)]
enum Tile {
    Air,
    Rock,
    Sand,
}

impl Display for Tile {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Tile::Air => write!(f, "."),
            Tile::Rock => write!(f, "\x1b[47m \x1b[0m"),
            Tile::Sand => write!(f, "\x1b[43m \x1b[0m"),
        }
    }
}

pub fn main() {
    let input = match TEST {
        true => {
            "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9"
        }
        false => include_str!("../inputs/day14.txt"),
    };
    let splines: Vec<Vec<Pos>> = input
        .lines()
        .map(|line| {
            line.split(" -> ")
                .map(|pos| {
                    let (x, y) = pos.split_once(',').unwrap();
                    (x.parse().unwrap(), y.parse().unwrap())
                })
                .collect()
        })
        .collect();
    // width = max x idx + 1
    let width = splines.iter().flatten().map(|(x, _)| *x).max().unwrap() + 1;
    // height = max y idx + 1 + 2 for floor in part two
    let height = splines.iter().flatten().map(|(_, y)| *y).max().unwrap() + 3;
    // create empty map
    let mut map: Vec<Vec<Tile>> = vec![vec![Tile::Air; width]; height];
    // fill map with rocks
    for spline in splines {
        for (knot1, knot2) in spline.into_iter().tuple_windows() {
            for row in map
                .iter_mut()
                .take(knot1.1.max(knot2.1) + 1)
                .skip(knot1.1.min(knot2.1))
            {
                for tile in row
                    .iter_mut()
                    .take(knot1.0.max(knot2.0) + 1)
                    .skip(knot1.0.min(knot2.0))
                {
                    *tile = Tile::Rock;
                }
            }
        }
    }

    if DEBUG_PART_1 || DEBUG_PART_2 {
        // clear screen
        print!("\x1b[2J");
    }

    println!("--- Day 14 ---");
    println!("Part 1: {}", part1(&map));
    println!("Part 2: {}", part2(&map));
}

fn try_move(map: &[Vec<Tile>], pos: &mut Pos) -> bool {
    if map
        .get(pos.1 + 1)
        .and_then(|row| row.get(pos.0))
        .map_or(true, |tile| tile == &Tile::Air)
    {
        pos.1 += 1;
    } else if map
        .get(pos.1 + 1)
        .and_then(|row| row.get(pos.0.wrapping_sub(1)))
        .map_or(true, |tile| tile == &Tile::Air)
    {
        pos.0 -= 1;
        pos.1 += 1;
    } else if map
        .get(pos.1 + 1)
        .and_then(|row| row.get(pos.0 + 1))
        .map_or(true, |tile| tile == &Tile::Air)
    {
        pos.0 += 1;
        pos.1 += 1;
    } else {
        return false;
    }
    true
}

fn part1(map: &[Vec<Tile>]) -> usize {
    let mut map = map.to_vec();
    let mut count = 0;
    'outer: loop {
        let mut pos = (500, 0);
        loop {
            if !(0..map[0].len()).contains(&pos.0) || !(0..map.len()).contains(&pos.1) {
                break 'outer;
            } else if !try_move(&map, &mut pos) {
                break;
            }
        }
        map[pos.1][pos.0] = Tile::Sand;
        count += 1;
        if DEBUG_PART_1 {
            print_map(&map);
        }
    }
    if DEBUG_PART_2 {
        print_map(&map);
    }
    count
}

fn part2(map: &[Vec<Tile>]) -> usize {
    let mut map = map.to_vec();
    for tile in map.last_mut().unwrap() {
        *tile = Tile::Rock;
    }
    let mut count = 0;
    let mut reached_end = false;
    while !reached_end {
        let mut pos = (500, 0);
        loop {
            // extend map to the right if necessary
            if pos.0 + 1 == map[0].len() {
                for row in &mut map {
                    row.push(Tile::Air);
                }
                *map.last_mut().unwrap().last_mut().unwrap() = Tile::Rock;
            }

            // move sand unit
            if !try_move(&map, &mut pos) {
                reached_end = pos == (500, 0);
                break;
            }
        }
        if DEBUG_PART_2 {
            print_map(&map);
        }
        map[pos.1][pos.0] = Tile::Sand;
        count += 1;
    }
    if DEBUG_PART_2 {
        print_map(&map);
    }
    count
}

fn print_map(map: &[Vec<Tile>]) {
    // go to top left
    print!("\x1b[H");
    // get left-most non-air tile
    let x_min = map
        .iter()
        .rev()
        .skip(1) // do not consider infinite floor for x bounds
        .map(|row| row.iter().take_while(|tile| tile == &&Tile::Air).count())
        .min()
        .unwrap();
    for row in map {
        for tile in &row[x_min..] {
            print!("{tile}");
        }
        println!();
    }
    thread::sleep(Duration::from_millis(DEBUG_SLEEP_MILLIS));
}
