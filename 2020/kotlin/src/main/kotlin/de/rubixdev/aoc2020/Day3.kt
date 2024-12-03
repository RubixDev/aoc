package de.rubixdev.aoc2020

fun runDay3() {
    println("--- DAY 03 ---")

    val input = object {}.javaClass.getResource("/input03.txt")!!.readText().split('\n')

    part1(input)
    println()
    part2(input)
    println()
}

private fun countTrees(forest: List<String>, right: Byte, down: Byte): Long {
    var count = 0L
    var posX = 0
    var posY = 0
    val forestWidth = forest[0].length

    while(posY < forest.size) {
        if (forest[posY][posX % forestWidth] == '#') {
            count++
        }
        posX += right
        posY += down
    }

    return count
}

private fun part1(input: List<String>) {
    println(countTrees(input, 3, 1))
}

private fun part2(input: List<String>) {
    val slopes: List<Pair<Byte, Byte>> = listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2),
    )
    val treeCounts = mutableListOf<Long>()

    for (slope in slopes) {
        treeCounts.add(countTrees(input, slope.first, slope.second))
    }
    println(treeCounts.reduce { acc, e -> acc * e })
}
