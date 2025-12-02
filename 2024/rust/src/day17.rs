use rayon::prelude::*;

fn get_combo(operand: u8, a: u64, b: u64, c: u64) -> u64 {
    match operand {
        0..=3 => operand as u64,
        4 => a,
        5 => b,
        6 => c,
        _ => panic!("illegal operand"),
    }
}

pub fn main() {
    // let mut a = 28066687u64;
    let instr = [2u8, 4, 1, 1, 7, 5, 4, 6, 0, 3, 1, 4, 5, 5, 3, 0];
    let result = (146_900_000_000u64..=146_900_000_000_000u64).into_par_iter().find_first(|&(mut a)| {
        if a % 100_000_000 == 0 { println!("{a}") }
        let mut b = 0u64;
        let mut c = 0u64;

        let mut pc = 0;
        let mut out = 0;
        while pc < instr.len() {
            let operand = instr[pc + 1];
            match instr[pc] {
                0 => a /= 2u64.pow(get_combo(operand, a, b, c) as u32),
                1 => b ^= operand as u64,
                2 => b = get_combo(operand, a, b, c) & 0b111,
                3 => {
                    if a != 0 {
                        pc = operand as usize;
                        continue;
                    }
                }
                4 => b ^= c,
                5 => {
                    let v = get_combo(operand, a, b, c) & 0b111;
                    if v != instr[out] as u64 { return false; }
                    out += 1;
                }
                6 => b = a / 2u64.pow(get_combo(operand, a, b, c) as u32),
                7 => c = a / 2u64.pow(get_combo(operand, a, b, c) as u32),
                _ => panic!("illegal opcode"),
            }
            pc += 2;
        }
        out == instr.len()
    });
    println!("{result:?}");
}
