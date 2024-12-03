use std::collections::HashMap;

pub fn run() {
    println!("--- DAY 10 ---");
    let input: Vec<Vec<char>> = include_str!("resources/input10.txt")
        .split('\n')
        .map(|line| line.chars().collect::<Vec<char>>())
        .collect();
    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn get_pair(input: &[char]) -> (Option<char>, usize) {
    let start_char = input[0];
    let mut offset_index = 1;
    while offset_index < input.len() && !['}', ']', ')', '>'].contains(&input[offset_index]) {
        let res = get_pair(&input[offset_index..]);
        offset_index += res.1;
        if res.0 != None { return (res.0, offset_index); }
    }
    if offset_index == input.len() {
        return (Some(' '), offset_index);
    }
    if !(
        (start_char == '{' && input[offset_index] == '}')
        || (start_char == '[' && input[offset_index] == ']')
        || (start_char == '(' && input[offset_index] == ')')
        || (start_char == '<' && input[offset_index] == '>')
    ) {
        return (Some(input[offset_index]), offset_index);
    }
    return (None, offset_index + 1);
}

fn part1(input: &Vec<Vec<char>>) {
    let score_map: HashMap<char, usize> = HashMap::from([
        (')', 3),
        (']', 57),
        ('}', 1197),
        ('>', 25137),
    ]);
    let scores: Vec<usize> = input.iter()
        .map(|line| get_pair(&line[..]).0.unwrap_or(' '))
        .filter(|it| it != &' ')
        .map(|char| *score_map.get(&char).unwrap())
        .collect();
    println!("{}", scores.iter().sum::<usize>());
}

fn part2(input: &Vec<Vec<char>>) {
    let bracket_pairs: HashMap<char, char> = HashMap::from([
        ('(', ')'),
        ('[', ']'),
        ('{', '}'),
        ('<', '>'),
    ]);
    let bracket_scores: HashMap<char, usize> = HashMap::from([
        ('(', 1),
        ('[', 2),
        ('{', 3),
        ('<', 4),
    ]);

    let mut scores: Vec<usize> = vec![];

    for line in input.iter() {
        let mut is_corrupted_line: bool = false;
        let mut bracket_stack: Vec<char> = vec![];

        for char in line.iter() {
            if ['(', '[', '{', '<'].contains(char) {
                bracket_stack.push(*char);
            } else {
                if bracket_pairs.get(bracket_stack.last().unwrap()).unwrap() == char {
                    bracket_stack.pop();
                } else {
                    is_corrupted_line = true;
                    break;
                }
            }
        }
        if is_corrupted_line { continue; }

        bracket_stack.reverse();
        let mut score: usize = 0;
        for bracket in bracket_stack.iter() {
            score *= 5;
            score += bracket_scores.get(bracket).unwrap();
        }
        scores.push(score);
    }

    scores.sort_unstable();
    println!("{}", scores[scores.len() / 2]);
}
