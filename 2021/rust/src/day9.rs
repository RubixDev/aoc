pub fn run() {
    println!("--- DAY 09 ---");
    let input: Vec<Vec<u8>> = include_str!("resources/input09.txt")
//         "2199943210
// 3987894921
// 9856789892
// 8767896789
// 9899965678"
        .split('\n')
        .map(|it| it.chars().map(|height| height.to_digit(10).unwrap() as u8).collect::<Vec<u8>>())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<Vec<u8>>) {
    let mut low_points: Vec<u16> = vec![];
    for (row, line) in input.iter().enumerate() {
        for (column, num) in line.iter().enumerate() {
            if (row == 0 || &input[row - 1][column] > num)
                && (column == 0 || &input[row][column - 1] > num)
                && (row == input.len() - 1 || &input[row + 1][column] > num)
                && (column == input[row].len() - 1 || &input[row][column + 1] > num)
            {
                low_points.push(*num as u16);
            }
        }
    }
    println!("{}", low_points.iter().sum::<u16>() + low_points.len() as u16);
}

fn get_basin(input: &Vec<Vec<u8>>, x: usize, y: usize, already_included: &Vec<(usize, usize)>) -> Vec<(usize, usize)> {
    let num = input[y][x];
    if num == 9 {
        return vec![];
    }
    let mut next_included: Vec<(usize, usize)> = already_included.clone();
    next_included.push((x, y));

    if x != input[y].len() - 1 && !next_included.contains(&(x + 1, y)) { next_included.append(&mut get_basin(input, x + 1, y, &next_included)) }
    if x != 0                  && !next_included.contains(&(x - 1, y)) { next_included.append(&mut get_basin(input, x - 1, y, &next_included)) }
    if y != input.len() - 1    && !next_included.contains(&(x, y + 1)) { next_included.append(&mut get_basin(input, x, y + 1, &next_included)) }
    if y != 0                  && !next_included.contains(&(x, y - 1)) { next_included.append(&mut get_basin(input, x, y - 1, &next_included)) }

    next_included.sort_unstable();
    next_included.dedup();
    return next_included;
}

fn part2(input: &Vec<Vec<u8>>) {
    let mut basins: Vec<Vec<(usize, usize)>> = vec![];
    for (row, line) in input.iter().enumerate() {
        let mut column: usize = 0;
        while column < line.len() {
            if line[column] == 9 || basins.iter().any(|basin| basin.contains(&(column, row))) {
                column += 1;
                continue;
            }
            basins.push(get_basin(input, column, row, &vec![]));
            while column < line.len() && line[column] != 9 {
                column += 1;
                continue;
            }
        }
    }
    let mut basin_sizes: Vec<u16> = basins.iter().map(|basin| basin.len() as u16).collect();
    basin_sizes.sort_unstable();
    println!("{:?}", &basin_sizes[basin_sizes.len() - 3..basin_sizes.len()].iter().map(|it| *it as u32).product::<u32>());
}
