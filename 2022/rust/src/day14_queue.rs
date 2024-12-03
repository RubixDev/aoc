use std::{
    collections::{HashSet, VecDeque},
    fmt::{self, Display, Formatter},
    thread,
    time::Duration,
};

use itertools::Itertools;

const TEST: bool = false;
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

    // clear screen
    print!("\x1b[2J");
    part2(&map);
}

fn try_move(map: &mut [Vec<Tile>], pos: &mut Pos) -> bool {
    if pos.0 + 1 == map[0].len() {
        for row in map.iter_mut() {
            row.push(Tile::Air);
        }
        *map.last_mut().unwrap().last_mut().unwrap() = Tile::Rock;
    }
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

fn move_all(map: &mut [Vec<Tile>], queue: &mut VecDeque<Pos>) -> bool {
    let mut pop_count = 0;
    for pos in queue.iter_mut() {
        if !try_move(map, pos) {
            pop_count += 1;
        }
    }
    if pop_count > 0 && pop_count == queue.len() {
        queue.clear();
        map[0][500] = Tile::Sand;
        return false;
    }
    for pos in queue.drain(..pop_count) {
        map[pos.1][pos.0] = Tile::Sand;
    }
    queue.push_back((500, 0));
    true
}

fn part2(map: &[Vec<Tile>]) {
    let mut map = map.to_vec();
    for tile in map.last_mut().unwrap() {
        *tile = Tile::Rock;
    }
    let mut queue = VecDeque::new();
    while move_all(&mut map, &mut queue) {
        print_map(&map, &queue)
    }
    print_map(&map, &queue);
}

fn print_map(map: &[Vec<Tile>], queue: &VecDeque<Pos>) {
    let queue: HashSet<_> = queue.iter().collect();
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
    for (y, row) in map.iter().enumerate() {
        for (x, tile) in row.iter().enumerate().skip(x_min) {
            if queue.contains(&(x, y)) {
                print!("\x1b[41m \x1b[0m");
            } else {
                print!("{tile}");
            }
        }
        println!();
    }
    thread::sleep(Duration::from_millis(DEBUG_SLEEP_MILLIS));
}
