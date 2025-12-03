// #[derive(Debug)]
struct Passport<'a>([&'a str; 7]);

fn get_field<'a>(field: &str, fields: &Vec<Vec<&'a str>>) -> &'a str {
    return fields.iter().find(|it| it[0] == field).and_then(|it| Some(it[1])).unwrap_or("");
}

pub fn main(input: &str) {
    println!("--- DAY 04 ---");

    let input: Vec<Passport> = input
        .split("\n\n")
        .map(|passport| {
            let fields: Vec<Vec<&str>> = passport.split(&[' ', '\n'][..]).map(|it| it.split(':').collect::<Vec<&str>>()).collect::<Vec<Vec<&str>>>();
            Passport([
                get_field("byr", &fields),
                get_field("iyr", &fields),
                get_field("eyr", &fields),
                get_field("hgt", &fields),
                get_field("hcl", &fields),
                get_field("ecl", &fields),
                get_field("pid", &fields),
            ])
        })
        .collect();

    part1(&input);
    println!();
    part2(&input);
    println!();
}

fn part1(input: &Vec<Passport>) {
    println!(
        "{}",
        input
            .iter()
            .map(|passport| {
                passport
                    .0
                    .iter()
                    .map(|field| field != &"")
                    .all(|field| field)
            })
            .filter(|passport| *passport)
            .count()
    );
}

fn part2(input: &Vec<Passport>) {
    let byr_re = regex::Regex::new(r"^19[2-9][0-9]|200[0-2]$").unwrap();
    let iyr_re = regex::Regex::new(r"^201[0-9]|2020$").unwrap();
    let eyr_re = regex::Regex::new(r"^202[0-9]|2030$").unwrap();
    let hgt_re = regex::Regex::new(r"^(1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in$").unwrap();
    let hcl_re = regex::Regex::new(r"^#[0-9a-f]{6}$").unwrap();
    let ecl_re = regex::Regex::new(r"^amb|blu|brn|gry|grn|hzl|oth$").unwrap();
    let pid_re = regex::Regex::new(r"^\d{9}$").unwrap();

    println!(
        "{}",
        input
            .iter()
            .map(|passport| {
                byr_re.is_match(passport.0[0])
                    && iyr_re.is_match(passport.0[1])
                    && eyr_re.is_match(passport.0[2])
                    && hgt_re.is_match(passport.0[3])
                    && hcl_re.is_match(passport.0[4])
                    && ecl_re.is_match(passport.0[5])
                    && pid_re.is_match(passport.0[6])
            })
            .filter(|passport| *passport)
            .count()
    );
}
