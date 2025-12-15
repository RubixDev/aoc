package de.rubixdev.aoc

fun day10Smart(input: String): Day = sequence {
    val example = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.split(' ') }
        .map { line ->
            Triple(
                line.first().drop(1).dropLast(1).map { it == '#' },
                line.drop(1).dropLast(1).map { btn ->
                    btn.drop(1).dropLast(1).split(',').map { it.toInt() }
                },
                line.last().drop(1).dropLast(1).split(',').map { it.toInt() },
            )
        }

    yield("10 (using recursive algorithm)")
    yield(part1(input))
    yield(part2(input))
}

private fun findValidPaths(buttons: List<Button>, target: Lights): Sequence<List<Button>> =
    buttons.powerset().filter { set ->
        target.withIndex().all { light ->
            (set.count { light.index in it } % 2 != 0) == light.value
        }
    }

private fun part1(input: Input) = input.sumOf { machine ->
    findValidPaths(machine.second, machine.first).minOf { it.size }
}

// based on this very smart writeup: https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
// there are a few ways to make this faster, but 1 minute is fast enough for me here
private fun part2(input: Input) = input.sumOf { machine ->
    val getMinPresses: (Joltage) -> Long? = memoize { joltages ->
        when (joltages.all { it == 0 }) {
            true -> 0L

            false -> findValidPaths(machine.second, joltages.map { it % 2 != 0 })
                .mapNotNull { path ->
                    val subtracted = joltages.mapIndexed { i, j -> j - path.count { i in it } }
                    subtracted.all { it >= 0 }.then {
                        recurse(subtracted.map { it / 2 })?.let { path.size + 2 * it }
                    }
                }
                .minOrNull()
        }
    }
    getMinPresses(machine.third)!!
}
