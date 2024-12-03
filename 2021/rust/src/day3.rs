pub fn run() {
    println!("--- DAY 03 ---");
    let input: Vec<&str> = include_str!("resources/input03.txt")
        .split('\n')
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<&str>) {
    let mut one_counts:  Vec<u16> = vec![0; input[0].len()];
    for line in input.iter() {
        for (index, char) in line.chars().enumerate() {
            if char == '1' {
                one_counts[index] += 1;
            }
        }
    }
    let gamma:   String = one_counts.iter().map(|count| if *count as usize >  input.len() / 2 { '1' } else { '0' }).collect::<String>();
    let epsilon: String = one_counts.iter().map(|count| if *count as usize <= input.len() / 2 { '1' } else { '0' }).collect::<String>();
    println!("{}", usize::from_str_radix(gamma.as_str(), 2).unwrap() * usize::from_str_radix(epsilon.as_str(), 2).unwrap())
}

fn rating<'a>(numbers: Vec<&'a str>, index: u16, is_oxygen_rating: bool) -> Vec<&'a str> {
    if numbers.len() == 1 { return numbers; }
    let count: usize = numbers.iter().filter(|num| num.chars().nth(index as usize).unwrap() == '1').count();
    let most_common: char = if is_oxygen_rating {
        if count >= numbers.len() / 2 { '1' } else { '0' }
    } else {
        if count <  numbers.len() / 2 { '1' } else { '0' }
    };
    let filtered_nums: Vec<&str> = numbers.iter().filter(|num| num.chars().nth(index as usize).unwrap() == most_common).map(|it| *it).collect();
    return rating(
        filtered_nums,
        index + 1,
        is_oxygen_rating
    );
}

fn part2(input: &Vec<&str>) {
    let oxygen_rating = rating(input.clone(), 0, true )[0];
    let co2_rating    = rating(input.clone(), 0, false)[0];
    println!("{}", usize::from_str_radix(oxygen_rating, 2).unwrap() * usize::from_str_radix(co2_rating, 2).unwrap());
}
