use std::{
    cell::RefCell,
    rc::{Rc, Weak},
};

#[derive(Debug)]
struct File {
    kind: FileKind,
    name: String,
    parent: Option<Weak<File>>,
}

#[derive(Debug)]
enum FileKind {
    File { size: usize },
    Directory { children: RefCell<Vec<Rc<File>>> },
}

impl FileKind {
    fn unwrap_dir(&self) -> &RefCell<Vec<Rc<File>>> {
        match self {
            FileKind::Directory { children } => children,
            FileKind::File { .. } => panic!("called `unwrap_dir` on file"),
        }
    }
}

//////////////////////////////////////////////////////

pub fn main(input: &str) {
    let file_system = Rc::new(File {
        kind: FileKind::Directory {
            children: vec![].into(),
        },
        name: "/".into(),
        parent: None,
    });
    let mut current_file = Rc::clone(&file_system);
    for command in input.split("$ ").skip(1) {
        let (command, output) = command.split_once('\n').unwrap();
        match command.split_once(' ') {
            Some(("cd", "/")) => {
                while let Some(parent) = &current_file.parent {
                    current_file = parent.upgrade().unwrap();
                }
            }
            Some(("cd", "..")) => {
                current_file = current_file.parent.as_ref().unwrap().upgrade().unwrap()
            }
            Some(("cd", dir)) => {
                let mut children = current_file.kind.unwrap_dir().borrow_mut();
                match children.iter().find(|c| c.name == dir) {
                    Some(file) => {
                        let file = Rc::clone(file);
                        drop(children);
                        current_file = file
                    }
                    None => {
                        let new_dir = Rc::new(File {
                            kind: FileKind::Directory {
                                children: vec![].into(),
                            },
                            name: dir.to_string(),
                            parent: Some(Rc::downgrade(&current_file)),
                        });
                        children.push(Rc::clone(&new_dir));
                        drop(children);
                        current_file = new_dir;
                    }
                }
            }
            Some(_) => unreachable!(),
            None => {
                for file in output.lines() {
                    let (kind, name) = file.split_once(' ').unwrap();

                    if current_file
                        .kind
                        .unwrap_dir()
                        .borrow()
                        .iter()
                        .any(|file| file.name == name)
                    {
                        continue;
                    }

                    let kind = match kind.parse() {
                        Ok(size) => FileKind::File { size },
                        Err(_) => FileKind::Directory {
                            children: vec![].into(),
                        },
                    };

                    current_file.kind.unwrap_dir().borrow_mut().push(
                        File {
                            kind,
                            name: name.to_string(),
                            parent: Some(Rc::downgrade(&current_file)),
                        }
                        .into(),
                    )
                }
            }
        }
    }
    println!("--- Day 7 ---");
    println!("Part 1: {}", part1(&file_system));
    println!("Part 2: {}", part2(&file_system));
}

//////////////////////////////////////////////////////

fn part1(file: &Rc<File>) -> usize {
    let mut dirs = vec![];
    calculate_size(file, &mut dirs);
    dirs.into_iter().filter(|dir| *dir <= 100_000).sum()
}

fn part2(file: &Rc<File>) -> usize {
    let mut dirs = vec![];
    let root_size = calculate_size(file, &mut dirs);
    let free_space = 70_000_000 - root_size;
    let missing_space = 30_000_000 - free_space;
    dirs.sort_unstable();
    dirs.into_iter()
        .find(|size| *size >= missing_space)
        .unwrap()
}

fn calculate_size(file: &Rc<File>, dirs: &mut Vec<usize>) -> usize {
    match &file.kind {
        FileKind::File { size } => *size,
        FileKind::Directory { children } => {
            let size = children
                .borrow()
                .iter()
                .map(|file| calculate_size(file, dirs))
                .sum();
            dirs.push(size);
            size
        }
    }
}
