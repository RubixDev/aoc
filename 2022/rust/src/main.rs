use std::time::Instant;

macro_rules! run_days {
    ($($module:ident),* $(,)?) => {
        $(mod $module;)*
        fn main() {
            let start_total = Instant::now();
            $(
                let start = Instant::now();
                $module::main();
                println!("\x1b[90m{:?}\x1b[0m\n", start.elapsed());
            )*
            println!("\x1b[1mTotal: {:?}\x1b[0m", start_total.elapsed());
        }
    };
}

run_days!(
    day1,
    day2,
    day3,
    day4,
    day5,
    day6,
    day7,
    day7_alternative,
    day8,
    day9,
    day10,
    day11,
    day12,
    day12_bfs,
    day13,
    day14,
    // day14_queue,
    day15,
);
