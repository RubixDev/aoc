use core::str::Split;

#[derive(Clone)]
struct Board {
    board: [[Number; 5]; 5],
}
impl Board {
    pub fn mark_num(&mut self, num: u8) -> bool {
        for (row, numbers) in self.board.clone().iter().enumerate() {
            for (column, number) in numbers.iter().enumerate() {
                if number.value != num { continue; }
                self.board[row][column].marked = true;

                if self.board[row].iter().all(|it| it.marked)
                    || self.board.iter().all(|it| it[column].marked) {
                    return true;
                }
            }
        }
        return false;
    }
}

#[derive(Clone, Copy)]
struct Number {
    value: u8,
    marked: bool,
}

pub fn run() {
    println!("--- DAY 04 ---");
    let mut raw_input: Split<&str> = include_str!("resources/input04.txt")
        .split("\n\n");
    let input: (Vec<u8>, Vec<Board>) = (
        raw_input.next().unwrap().split(',').map(|num| num.parse::<u8>().unwrap()).collect::<Vec<u8>>(),
        raw_input.map(|board| {
            let board: Vec<[Number; 5]> = board.split('\n').map(|line| {
                let row: Vec<Number> = line.split(' ').filter(|it| it != &"").map(|num| {
                    Number {
                        value: num.parse::<u8>().unwrap(),
                        marked: false,
                    }
                }).collect::<Vec<Number>>();
                [row[0], row[1], row[2], row[3], row[4]]
            }).collect::<Vec<[Number; 5]>>();
            Board {
                board: [board[0], board[1], board[2], board[3], board[4]]
            }
        }).collect::<Vec<Board>>()
    );
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &(Vec<u8>, Vec<Board>)) {
    let mut data: (Vec<u8>, Vec<Board>) = (input.0.clone(), input.1.clone());
    for num in data.0.iter() {
        for board in data.1.iter_mut() {
            let has_won: bool = board.mark_num(*num);
            if has_won {
                let sum: u16 = board.board.iter().map(|row| row.iter().filter(|it| !it.marked).map(|it| it.value as u16).sum::<u16>() as u16).sum();
                println!("{}", sum as u32 * *num as u32);
                return;
            }
        }
    }
}

fn part2(input: &(Vec<u8>, Vec<Board>)) {
    let mut scores: Vec<u32> = vec![];
    let mut won_boards: Vec<usize> = vec![];

    let mut data: (Vec<u8>, Vec<Board>) = (input.0.clone(), input.1.clone());
    for num in data.0.iter() {
        for (index, board) in data.1.iter_mut().enumerate() {
            if won_boards.contains(&index) { continue; }
            let has_won: bool = board.mark_num(*num);
            if has_won {
                let sum: u16 = board.board.iter().map(|row| row.iter().filter(|it| !it.marked).map(|it| it.value as u16).sum::<u16>() as u16).sum();
                scores.push(sum as u32 * *num as u32);
                won_boards.push(index);
            }
        }
    }

    println!("{}", scores.last().unwrap())
}
