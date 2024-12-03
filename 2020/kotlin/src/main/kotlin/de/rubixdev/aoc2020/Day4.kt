package de.rubixdev.aoc2020

fun runDay4() {
    println("--- DAY 04 ---")

    val rawInput = object {}.javaClass.getResource("/input04.txt")!!.readText()
    val input: MutableList<MutableMap<String, String>> = mutableListOf()
    for ((index, passport) in rawInput.split("\n\n").withIndex()) {
        val details = passport.replace(" ", "\n").split("\n")
        input.add(mutableMapOf())
        for (rawDetail in details) {
            val detail = rawDetail.split(':')
            input[index][detail[0]] = detail[1]
        }
    }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<Map<String, String>>) {
    var count = 0
    for (passport in input) {
        if (
            "byr" in passport
            && "iyr" in passport
            && "eyr" in passport
            && "hgt" in passport
            && "hcl" in passport
            && "ecl" in passport
            && "pid" in passport
        ) {
            count++
        }
    }
    println(count)
}

private fun part2(input: List<Map<String, String>>) {
    var count = 0
    for (passport in input) {
        val hgt = passport["hgt"] ?: "  "
        val hgtUnit = hgt.substring(hgt.lastIndex - 1)
        if (
            "byr" in passport && passport["byr"]!!.toInt() in 1920..2002
            && "iyr" in passport && passport["iyr"]!!.toInt() in 2010..2020
            && "eyr" in passport && passport["eyr"]!!.toInt() in 2020..2030
            && "hgt" in passport && hgtUnit in arrayOf("cm", "in") && hgt.substringBefore(hgtUnit).toInt() in (if (hgt.substring(hgt.lastIndex - 1) == "cm") 150..193 else 59..76)
            && "hcl" in passport && passport["hcl"]!!.matches("-?^#[0-9a-fA-F]{6}+".toRegex())
            && "ecl" in passport && passport["ecl"]!! in arrayOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            && "pid" in passport && passport["pid"]!!.matches("-?[0-9]{9}+".toRegex())
        ) {
            count++
        }
    }
    println(count)
}
