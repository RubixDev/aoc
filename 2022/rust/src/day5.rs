pub fn main(input: &str) {
    let (stack_input, instruction_input) = input
        .split_once("\n\n")
        .unwrap();
    let indices: Vec<usize> = (1..).step_by(4).take(9).collect();
    let stack_input: Vec<Vec<&str>> = indices
        .iter()
        .map(|idx| {
            stack_input
                .lines()
                .rev()
                .skip(1)
                .map(|line| &line[*idx..=*idx])
                .take_while(|crate_| *crate_ != " ")
                .collect()
        })
        .collect();
    let instruction_input: Vec<(usize, usize, usize)> = instruction_input
        .lines()
        .map(|line| {
            let split: Vec<&str> = line.split(' ').collect();
            (
                split[1].parse().unwrap(),
                split[3].parse().unwrap(),
                split[5].parse().unwrap(),
            )
        })
        .collect();
    println!("--- Day 5 ---");
    println!("Part 1: {}", part1(&stack_input, &instruction_input));
    println!("Part 2: {}", part2(&stack_input, &instruction_input));
}

fn part1(stack_input: &[Vec<&str>], instructions: &[(usize, usize, usize)]) -> String {
    let mut stacks = stack_input.to_vec();
    for (count, from, to) in instructions {
        for _ in 0..*count {
            let crate_ = stacks[*from - 1].pop().unwrap();
            stacks[*to - 1].push(crate_);
        }
    }
    stacks
        .into_iter()
        .map(|mut stack| stack.pop().unwrap())
        .collect()
}

fn part2(stack_input: &[Vec<&str>], instructions: &[(usize, usize, usize)]) -> String {
    let mut stacks = stack_input.to_vec();
    for (count, from, to) in instructions {
        let mut buffer = vec![];
        for _ in 0..*count {
            let crate_ = stacks[*from - 1].pop().unwrap();
            buffer.push(crate_);
        }
        for crate_ in buffer.into_iter().rev() {
            stacks[*to - 1].push(crate_);
        }
    }
    stacks
        .into_iter()
        .map(|mut stack| stack.pop().unwrap())
        .collect()
}
