package de.rubixdev.aoc

fun day7(input: String): Day = sequence {
    val example = "190: 10 19\n" +
        "3267: 81 40 27\n" +
        "83: 17 5\n" +
        "156: 15 6\n" +
        "7290: 6 8 6 15\n" +
        "161011: 16 10 13\n" +
        "192: 17 8 14\n" +
        "21037: 9 7 18 13\n" +
        "292: 11 6 16 20"
//    val input = example.lines()
    val input = input.lines()
        .map { line ->
            line.split(": ").let {
                it[0].toLong() to it[1].split(" ").map(String::toLong)
            }
        }

    yield(Unit)
    yield(part(input, part2 = false))
    yield(part(input, part2 = true))
}

private fun isPossible(nums: List<Long>, target: Long, current: Long, part2: Boolean): Boolean =
    when {
        nums.isEmpty() -> current == target

        current > target -> false

        else -> isPossible(nums.drop(1), target, current + nums.first(), part2) ||
            isPossible(nums.drop(1), target, current * nums.first(), part2) ||
            (part2 && isPossible(nums.drop(1), target, "$current${nums.first()}".toLong(), true))
    }

private fun part(input: List<Pair<Long, List<Long>>>, part2: Boolean) =
    input.filter { (target, nums) -> isPossible(nums.drop(1), target, nums.first(), part2) }
        .sumOf { it.first }
