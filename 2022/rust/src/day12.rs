use strum::IntoEnumIterator;

use crate::day12_bfs::print_map;

const TEST: bool = false;
const DEBUG: bool = false;
const DEBUG_SLEEP_MILLIS: u64 = 0;

#[derive(Debug, Clone, Copy, strum::EnumIter)]
pub enum Direction {
    Up,
    Right,
    Down,
    Left,
}

pub type Pos = (usize, usize);

pub fn main(input: &str) {
    let (start_pos, end_pos, heightmap) = parse_heightmap(TEST, input);

    let mut step_counts = vec![vec![None; heightmap[0].len()]; heightmap.len()];
    step_counts[end_pos.1][end_pos.0] = Some(0);

    if DEBUG {
        print!("\x1b[2J\x1b[?25l");
    }
    fill_step_counts(end_pos, &mut step_counts, &heightmap, &mut vec![end_pos]);
    if DEBUG {
        print!("\x1b[?25h");
    }

    println!("--- Day 12 ---");
    println!("Part 1: {}", part1(&step_counts, start_pos));
    println!("Part 2: {}", part2(&step_counts, &heightmap));
}

pub fn parse_heightmap(test: bool, input: &str) -> (Pos, Pos, Vec<Vec<u8>>) {
    let input = match test {
        true => {
            "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"
        }
        false => input,
    };
    let mut start_pos = (0, 0);
    let mut end_pos = (0, 0);
    let heightmap = input
        .lines()
        .enumerate()
        .map(|(y, line)| {
            line.as_bytes()
                .iter()
                .enumerate()
                .map(|(x, byte)| match byte {
                    b'S' => {
                        start_pos = (x, y);
                        0
                    }
                    b'E' => {
                        end_pos = (x, y);
                        b'z' - b'a'
                    }
                    _ => byte - b'a',
                })
                .collect()
        })
        .collect();
    (start_pos, end_pos, heightmap)
}

fn fill_step_counts(
    pos: Pos,
    step_counts: &mut [Vec<Option<usize>>],
    heightmap: &[Vec<u8>],
    path: &mut Vec<Pos>,
) {
    let current_steps = step_counts[pos.1][pos.0].unwrap();
    for direction in Direction::iter() {
        let offset = offset(pos, direction);
        if (0..step_counts.len()).contains(&offset.1)
            && (0..step_counts[0].len()).contains(&offset.0)
            && heightmap[offset.1][offset.0].saturating_add(1) >= heightmap[pos.1][pos.0]
            && step_counts[offset.1][offset.0].map_or(true, |count| count > current_steps + 1)
        {
            step_counts[offset.1][offset.0] = Some(current_steps + 1);

            if DEBUG {
                print_map(
                    offset,
                    step_counts,
                    &path.iter().copied().collect(),
                    DEBUG_SLEEP_MILLIS,
                );
                path.push(offset);
            }

            fill_step_counts(offset, step_counts, heightmap, path);
            path.pop();
        }
    }
}

pub fn offset((x, y): Pos, direction: Direction) -> Pos {
    match direction {
        Direction::Up => (x, y.wrapping_sub(1)),
        Direction::Down => (x, y + 1),
        Direction::Left => (x.wrapping_sub(1), y),
        Direction::Right => (x + 1, y),
    }
}

//////////////////////////////////

pub fn part1(step_counts: &[Vec<Option<usize>>], start_pos: Pos) -> usize {
    step_counts[start_pos.1][start_pos.0].unwrap()
}

pub fn part2(step_counts: &[Vec<Option<usize>>], input: &[Vec<u8>]) -> usize {
    input
        .iter()
        .enumerate()
        .flat_map(|(y, row)| {
            row.iter()
                .enumerate()
                .filter(|(_, height)| **height == 0)
                .map(move |(x, _)| step_counts[y][x].unwrap_or(usize::MAX))
        })
        .min()
        .unwrap()
}
