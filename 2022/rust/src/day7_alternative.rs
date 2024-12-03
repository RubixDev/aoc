use std::{
    collections::HashMap,
    path::{Path, PathBuf},
};

#[derive(Debug)]
enum Entry {
    Dir(Vec<PathBuf>),
    File(usize),
}

impl Entry {
    fn unwrap_dir(&mut self) -> &mut Vec<PathBuf> {
        match self {
            Self::Dir(dir) => dir,
            Self::File(_) => panic!("called `unwrap_dir` on file"),
        }
    }
}

pub fn main() {
    let mut cwd = PathBuf::from("/");
    let mut file_system: HashMap<PathBuf, Entry> =
        HashMap::from([(cwd.clone(), Entry::Dir(vec![]))]);
    for line in include_str!("../inputs/day7.txt").lines() {
        match &line[..4] {
            "$ cd" => match &line[5..] {
                "/" => cwd = PathBuf::from("/"),
                ".." => cwd = cwd.parent().unwrap().into(),
                name => cwd = cwd.join(name),
            },
            "$ ls" => {}
            "dir " => {
                file_system
                    .get_mut(&cwd)
                    .unwrap()
                    .unwrap_dir()
                    .push(cwd.join(&line[4..]));
                file_system.insert(cwd.join(&line[4..]), Entry::Dir(vec![]));
            }
            _ => {
                let (size, name) = line.split_once(' ').unwrap();
                file_system
                    .get_mut(&cwd)
                    .unwrap()
                    .unwrap_dir()
                    .push(cwd.join(name));
                file_system.insert(cwd.join(name), Entry::File(size.parse().unwrap()));
            }
        }
    }

    let mut sizes: Vec<usize> = vec![];
    calculate_sizes(&mut sizes, &file_system, Path::new("/"));

    println!("--- Day 7 (alternative) ---");
    println!("Part 1: {}", part1(&sizes));
    println!("Part 2: {}", part2(&mut sizes));
}

fn calculate_sizes(
    sizes: &mut Vec<usize>,
    file_system: &HashMap<PathBuf, Entry>,
    path: &Path,
) -> usize {
    match &file_system[path] {
        Entry::File(size) => *size,
        Entry::Dir(children) => {
            let size = children
                .iter()
                .map(|child| calculate_sizes(sizes, file_system, child))
                .sum();
            sizes.push(size);
            size
        }
    }
}

fn part1(sizes: &[usize]) -> usize {
    sizes.iter().filter(|dir| **dir <= 100_000).sum()
}

fn part2(sizes: &mut [usize]) -> usize {
    let root_size = sizes.last().unwrap();
    let free_space = 70_000_000 - root_size;
    let missing_space = 30_000_000 - free_space;
    sizes.sort_unstable();
    *sizes.iter().find(|size| **size >= missing_space).unwrap()
}
