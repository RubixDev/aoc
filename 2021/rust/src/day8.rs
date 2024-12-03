use std::collections::{HashSet, HashMap};

macro_rules! arr(
    ($val:expr; $len:expr) => (
        {
            let mut array: [_; $len] = unsafe { std::mem::MaybeUninit::uninit().assume_init() };
            for i in array.iter_mut() {
                unsafe { ::std::ptr::write(i, $val); }
            }
            array
        }
    )
);

pub fn run() {
    println!("--- DAY 08 ---");
    let input: Vec<([&str; 10], [&str; 4])> = include_str!("resources/input08.txt")
        .split('\n')
        .map(|it| it.split(" | "))
        .map(|mut it| (it.next().unwrap().split(' '), it.next().unwrap().split(' ')))
        .map(|mut it| (
            arr![it.0.next().unwrap(); 10],
            arr![it.1.next().unwrap(); 4],
        ))
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<([&str; 10], [&str; 4])>) {
    println!(
        "{}",
        input.iter().map(|it| it.1.iter().filter(|digit| [2, 4, 3, 7].contains(&digit.len())).count()).sum::<usize>()
    );
}

fn part2(input: &Vec<([&str; 10], [&str; 4])>) {
    let zero : HashSet<usize> = HashSet::from([0, 1, 2,    4, 5, 6]); // 6 segments
    let one  : HashSet<usize> = HashSet::from([      2,       5   ]); // 2 segments
    let two  : HashSet<usize> = HashSet::from([0,    2, 3, 4,    6]); // 5 segments
    let three: HashSet<usize> = HashSet::from([0,    2, 3,    5, 6]); // 5 segments
    let four : HashSet<usize> = HashSet::from([   1, 2, 3,    5,  ]); // 4 segments
    let five : HashSet<usize> = HashSet::from([0, 1,    3,    5, 6]); // 5 segments
    let six  : HashSet<usize> = HashSet::from([0, 1,    3, 4, 5, 6]); // 6 segments
    let seven: HashSet<usize> = HashSet::from([0,    2,       5,  ]); // 3 segments
    let eight: HashSet<usize> = HashSet::from([0, 1, 2, 3, 4, 5, 6]); // 7 segments
    let nine : HashSet<usize> = HashSet::from([0, 1, 2, 3,    5, 6]); // 6 segments
    let digits: [HashSet<usize>; 10] = [zero, one, two, three, four, five, six, seven, eight, nine];

    let mut out_values: Vec<u32> = vec![];

    for measurement in input.iter() {
        let mut possible: [Vec<char>; 7] = arr![vec!['a', 'b', 'c', 'd', 'e', 'f', 'g']; 7];
        for digit in [1, 4, 7, 8] {
            let chars_of_digit: Vec<char> = measurement.0.iter().find(|it| it.len() == digits[digit].len()).unwrap().chars().collect();

            for segment in digits[digit].iter().map(|it| *it) {
                let filtered: Vec<char> = possible[segment].iter().filter(|it| chars_of_digit.contains(it)).map(|it| *it).collect();
                possible[segment] = filtered;
            }
            for segment in [0, 1, 2, 3, 4, 5, 6].iter().filter(|num| !digits[digit].contains(num)).map(|it| *it) {
                let filtered: Vec<char> = possible[segment].iter().filter(|it| !chars_of_digit.contains(it)).map(|it| *it).collect();
                possible[segment] = filtered;
            }
        }

        let mut digit_index = 0;
        while possible.iter().any(|segment| segment.len() > 1) {
            let digit = &measurement.0[digit_index];

            if ![5, 6].contains(&digit.len()) {
                digit_index += 1;
                continue;
            }

            let mut possible_digits: Vec<usize> = vec![];
            for test_digit in [0usize, 2, 3, 5, 6, 9] {
                let mut used_chars: Vec<char> = vec![];
                let mut possible_digit: bool = true;

                for segment in digits[test_digit].iter().map(|it| *it) {
                    let mut segment_included: bool = false;
                    for char in digit.chars() {
                        if possible[segment].contains(&char) && !used_chars.contains(&char) {
                            used_chars.push(char);
                            segment_included = true;
                            break;
                        }
                    }
                    if !segment_included {
                        possible_digit = false;
                        break;
                    }
                }
                if possible_digit { possible_digits.push(test_digit); }
            }

            let correct_digit: usize = *possible_digits.iter().find(|it| digits[**it].len() == digit.len()).unwrap();
            for segment in digits[correct_digit].iter().map(|it| *it) {
                let filtered: Vec<char> = possible[segment].iter().filter(|it| digit.contains(it.to_string().as_str())).map(|it| *it).collect();
                possible[segment] = filtered;
            }
            for segment in [0, 1, 2, 3, 4, 5, 6].iter().filter(|num| !digits[correct_digit].contains(num)).map(|it| *it) {
                let filtered: Vec<char> = possible[segment].iter().filter(|it| !digit.contains(it.to_string().as_str())).map(|it| *it).collect();
                possible[segment] = filtered;
            }

            digit_index += 1;
        }

        let mut relinked_indices: HashMap<char, usize> = HashMap::new();
        for (index, chars) in possible.iter().enumerate() {
            relinked_indices.insert(chars[0], index);
        }

        let unmangled_digits: Vec<String> = measurement.1.iter()
            .map(|num| num.chars().map(|char| *relinked_indices.get(&char).unwrap()).collect::<HashSet<usize>>())
            .map(|num| digits.iter().position(|digit| digit == &num).unwrap())
            .map(|num| num.to_string())
            .collect();
        out_values.push(
            unmangled_digits.join("").parse::<u32>().unwrap()
        );
    }
    println!("{:?}", out_values.iter().sum::<u32>());
}
