package de.rubixdev.aoc

fun day10(input: String): Day = sequence {
    val example = "89010123\n" +
        "78121874\n" +
        "87430965\n" +
        "96549874\n" +
        "45678903\n" +
        "32019012\n" +
        "01329801\n" +
        "10456732"
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.map { it.digitToInt() } }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun findTrails(startPos: Vec2, map: List<List<Int>>): List<Vec2> {
    val targets = mutableListOf<Vec2>()
    val queue = ArrayDeque(Direction.entries.map { it.move(startPos) to 0 })
    while (queue.isNotEmpty()) {
        val (pos, height) = queue.removeFirst()
        if (pos.isInBounds(map) && map[pos] == height + 1) {
            if (height == 8) {
                targets.add(pos)
            } else {
                Direction.entries.map { it.move(pos) to height + 1 }.forEach { queue.add(it) }
            }
        }
    }
    return targets
}

private fun sumTrails(input: List<List<Int>>, distinctTargets: Boolean): Int =
    input.withIndex().sumOf { (y, line) ->
        line.withIndex().filter { it.value == 0 }.sumOf { (x, _) ->
            findTrails(x by y, input).let { if (distinctTargets) it.distinct() else it }.size
        }
    }

private fun part1(input: List<List<Int>>): Int = sumTrails(input, distinctTargets = true)

private fun part2(input: List<List<Int>>): Int = sumTrails(input, distinctTargets = false)
