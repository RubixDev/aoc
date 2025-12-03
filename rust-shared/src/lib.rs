use std::{fs, path::PathBuf};

pub fn get_input(year: u16, day: u8) -> String {
    let path = PathBuf::from(format!("inputs/{year}/day{day}.txt"));
    if path.exists() {
        return fs::read_to_string(path).expect("couldn't read input file");
    }
    _ = fs::create_dir_all(path.parent().unwrap());

    let session_token = dotenv::var("AOC_SESSION").expect("missing AOC_SESSION env variable");
    let text = reqwest::blocking::Client::new()
        .get(format!("https://adventofcode.com/{year}/day/{day}/input"))
        .header("Cookie", format!("session={session_token}"))
        .send()
        .expect("couldn't get input from remote")
        .text()
        .expect("coudn't get response text")
        .trim_end()
        .to_owned();
    _ = fs::write(path, &text);
    text
}

#[macro_export]
macro_rules! run_days {
    ($year:literal; $($module:ident: $day:literal),* $(,)?) => {
        $(mod $module;)*
        fn main() {
            let start_total = ::std::time::Instant::now();
            $(
                let start = ::std::time::Instant::now();
                $module::main(&::rust_shared::get_input($year, $day));
                println!("\x1b[90m{:?}\x1b[0m\n", start.elapsed());
            )*
            println!("\x1b[1mTotal: {:?}\x1b[0m", start_total.elapsed());
        }
    };
}
