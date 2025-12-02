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

run_days!(day1, day2,);
