package de.rubixdev

fun day2(): Day = sequence {
    val example = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
        "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
        "824824821-824824827,2121212118-2121212124"
//    val input = example
    val input = getInput(2)
        .split(',')
        .map { range -> range.split('-').map { it.toLong() }.toPair() }
        .map { (start, end) -> start..end }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part(input: List<LongRange>, regex: Regex): Long = input
    .asSequence()
    .flatten()
    .filter { regex.matches(it.toString()) }
    .sum()

private fun part1(input: List<LongRange>) = part(input, "^([0-9]+)\\1$".toRegex())
private fun part2(input: List<LongRange>) = part(input, "^([0-9]+)\\1+$".toRegex())
