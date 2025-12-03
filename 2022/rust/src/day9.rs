use std::{collections::VecDeque, thread, time::Duration};

const DEBUG: bool = false;
const DEBUG_SLEEP_MILLIS: u64 = 5;
const DEBUG_WIDTH: usize = 200;
const DEBUG_HEIGHT: usize = 45;
const DEBUG_PADDING: (usize, usize) = (DEBUG_WIDTH / 6, DEBUG_HEIGHT / 6);
const TEST_INPUT: InputKind = InputKind::Actual;

#[allow(dead_code)]
enum InputKind {
    Test1,
    Test2,
    Actual,
}

#[derive(Debug, PartialEq, Eq)]
enum Motion {
    Up,
    Down,
    Left,
    Right,
}

pub fn main(input: &str) {
    let input: Vec<(Motion, usize)> = match TEST_INPUT {
        InputKind::Test1 => {
            "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2"
        }
        InputKind::Test2 => {
            "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"
        }
        InputKind::Actual => input,
    }
    .lines()
    .map(|line| {
        let (motion, num) = line.split_once(' ').unwrap();
        (
            match motion {
                "U" => Motion::Up,
                "D" => Motion::Down,
                "L" => Motion::Left,
                "R" => Motion::Right,
                _ => panic!("invalid motion `{motion}`"),
            },
            num.parse().unwrap(),
        )
    })
    .collect();
    println!("--- Day 9 ---");
    println!("Part 1: {}", part1(&input));
    println!("Part 2: {}", part2(&input));
}

fn do_motion(
    motion: &Motion,
    positions: &mut [(usize, usize)],
    visited_cells: &mut VecDeque<VecDeque<bool>>,
) -> (bool, bool, bool, bool) {
    let mut new_front_x = false;
    let mut new_front_y = false;
    let mut new_back_x = false;
    let mut new_back_y = false;
    match motion {
        Motion::Up => {
            if visited_cells.get(positions[0].1.wrapping_sub(1)).is_none() {
                new_front_y = true;
                for pos in positions.iter_mut() {
                    pos.1 += 1;
                }
                visited_cells.push_front(VecDeque::from(vec![false; visited_cells[0].len()]));
            }
            positions[0].1 -= 1;
        }
        Motion::Down => {
            if visited_cells.get(positions[0].1 + 1).is_none() {
                new_back_y = true;
                visited_cells.push_back(VecDeque::from(vec![false; visited_cells[0].len()]));
            }
            positions[0].1 += 1;
        }
        Motion::Left => {
            if visited_cells[positions[0].1]
                .get(positions[0].0.wrapping_sub(1))
                .is_none()
            {
                new_front_x = true;
                for pos in positions.iter_mut() {
                    pos.0 += 1;
                }
                for row in visited_cells {
                    row.push_front(false);
                }
            }
            positions[0].0 -= 1;
        }
        Motion::Right => {
            if visited_cells[positions[0].1]
                .get(positions[0].0 + 1)
                .is_none()
            {
                new_back_x = true;
                for row in visited_cells {
                    row.push_back(false);
                }
            }
            positions[0].0 += 1;
        }
    }
    (new_front_x, new_front_y, new_back_x, new_back_y)
}

fn part1(input: &[(Motion, usize)]) -> usize {
    let mut visited_cells = VecDeque::from([VecDeque::from([true])]);
    let mut positions = [(0, 0), (0, 0)];
    for (motion, count) in input {
        for _ in 0..*count {
            if (*motion == Motion::Up && positions[1].1 > positions[0].1)
                || (*motion == Motion::Down && positions[1].1 < positions[0].1)
                || (*motion == Motion::Left && positions[1].0 > positions[0].0)
                || (*motion == Motion::Right && positions[1].0 < positions[0].0)
            {
                positions[1] = positions[0]
            }
            do_motion(motion, &mut positions, &mut visited_cells);
            visited_cells[positions[1].1][positions[1].0] = true;
        }
    }
    visited_cells
        .into_iter()
        .flatten()
        .filter(|cell| *cell)
        .count()
}

fn part2(input: &[(Motion, usize)]) -> usize {
    let mut visited_cells = VecDeque::from([VecDeque::from([true])]);
    let mut positions: [(usize, usize); 10] = [(0, 0); 10];
    if DEBUG {
        // clear screen and hide cursor
        print!("\x1b[2J\x1b[?25l");
    }
    let mut dbg_x_offset = positions[0].0.wrapping_sub(DEBUG_WIDTH / 2);
    let mut dbg_y_offset = positions[0].1.wrapping_sub(DEBUG_HEIGHT / 2);

    for (motion, count) in input {
        for _ in 0..*count {
            let (new_front_x, new_front_y, new_back_x, new_back_y) =
                do_motion(motion, &mut positions, &mut visited_cells);
            for idx in 0..positions.len() - 1 {
                let (moved, to_move) = (positions[idx], &mut positions[idx + 1]);

                let up = to_move.1 > moved.1 + 1;
                let down = to_move.1 + 1 < moved.1;
                let left = to_move.0 > moved.0 + 1;
                let right = to_move.0 + 1 < moved.0;
                let smaller_x = to_move.0 < moved.0;
                let smaller_y = to_move.1 < moved.1;
                let bigger_x = to_move.0 > moved.0;
                let bigger_y = to_move.1 > moved.1;

                if right || (smaller_x && (up || down)) {
                    to_move.0 += 1;
                } else if left || (bigger_x && (up || down)) {
                    to_move.0 -= 1;
                }
                if down || (smaller_y && (left || right)) {
                    to_move.1 += 1;
                } else if up || (bigger_y && (left || right)) {
                    to_move.1 -= 1;
                }
            }
            let (tail_x, tail_y) = positions.last().unwrap();
            visited_cells[*tail_y][*tail_x] = true;

            if DEBUG {
                // go to top left corner
                print!("\x1b[H");

                let border = "-".repeat(DEBUG_WIDTH);
                println!("\x1b[1m+{border}+\x1b[0m");

                let (head_x, head_y) = positions[0];

                if new_front_x {
                    dbg_x_offset = dbg_x_offset.wrapping_add(1);
                } else if new_front_y {
                    dbg_y_offset = dbg_y_offset.wrapping_add(1);
                }

                if head_x.wrapping_sub(dbg_x_offset) <= DEBUG_PADDING.0 {
                    dbg_x_offset = dbg_x_offset.wrapping_sub(1);
                } else if head_x.wrapping_sub(dbg_x_offset) >= DEBUG_WIDTH - DEBUG_PADDING.0 {
                    dbg_x_offset = dbg_x_offset.wrapping_add(1);
                } else if head_y.wrapping_sub(dbg_y_offset) <= DEBUG_PADDING.1 {
                    dbg_y_offset = dbg_y_offset.wrapping_sub(1);
                } else if head_y.wrapping_sub(dbg_y_offset) >= DEBUG_HEIGHT - DEBUG_PADDING.1 {
                    dbg_y_offset = dbg_y_offset.wrapping_add(1);
                }

                for y in 0..DEBUG_HEIGHT {
                    print!("\x1b[1m|\x1b[0m");
                    for x in 0..DEBUG_WIDTH {
                        let (pos_x, pos_y) =
                            (x.wrapping_add(dbg_x_offset), y.wrapping_add(dbg_y_offset));

                        match visited_cells.get(pos_y).and_then(|row| row.get(pos_x)) {
                            Some(visited) => {
                                match positions.iter().position(|pos| *pos == (pos_x, pos_y)) {
                                    // bold green `H` for head
                                    Some(0) => print!("\x1b[1;32mH\x1b[0m"),
                                    // bold white number
                                    Some(idx) => print!("\x1b[1m{idx}\x1b[0m"),
                                    None => match visited {
                                        // light gray `#` for visited cells
                                        true => print!("\x1b[90m#\x1b[0m"),
                                        // bold green `×` for new cells
                                        false
                                            if (new_front_x && pos_x == 0)
                                                || (new_front_y && pos_y == 0)
                                                || (new_back_x
                                                    && pos_x == visited_cells[0].len() - 1)
                                                || (new_back_y
                                                    && pos_y == visited_cells.len() - 1) =>
                                        {
                                            print!("\x1b[1;32m×\x1b[0m")
                                        }
                                        // dark gray `×` for everything else
                                        false => print!("\x1b[30m×\x1b[0m"),
                                    },
                                }
                            }
                            None if ((pos_x == usize::MAX || pos_x == visited_cells[0].len())
                                && (0..visited_cells.len()).contains(&pos_y))
                                || ((pos_y == usize::MAX || pos_y == visited_cells.len())
                                    && (0..visited_cells[0].len() + 2)
                                        .contains(&pos_x.wrapping_add(1))) =>
                            {
                                print!("#")
                            }
                            None => print!(" "),
                        }
                    }
                    println!("\x1b[1m|\x1b[0m");
                }

                println!("\x1b[1m+{border}+\x1b[0m");
                thread::sleep(Duration::from_millis(DEBUG_SLEEP_MILLIS));
            }
        }
    }
    if DEBUG {
        // show cursor
        print!("\x1b[?25h");
    }
    visited_cells
        .into_iter()
        .flatten()
        .filter(|cell| *cell)
        .count()
}
