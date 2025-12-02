use std::{fs, path::PathBuf};

pub fn get_input(day: u8) -> String {
    let path = PathBuf::from(format!("inputs/day{day}.txt"));
    if path.exists() {
        return fs::read_to_string(path).expect("couldn't read input file");
    }
    _ = fs::create_dir_all(path.parent().unwrap());

    let session_token = dotenv::var("AOC_SESSION").expect("missing AOC_SESSION env variable");
    reqwest::blocking::Client::new()
        .get(format!("https://adventofcode.com/2025/day/{day}/input"))
        .header("Cookie", format!("session={session_token}"))
        .send()
        .expect("couldn't get input from remote")
        .text()
        .expect("coudn't get response text")
}
