package de.rubixdev.aoc2020

fun runDay6() {
    println("--- DAY 06 ---")

    val input = object {}.javaClass.getResource("/input06.txt")!!.readText().split("\n\n").map { it.split("\n") }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<List<String>>) {
    println(
        input.map { group ->
            group.reduce {acc, str ->
                acc + str
            }.toSet().size
        }.sum()
    )
}

private fun part2(input: List<List<String>>) {
    println(
        input.map { group ->
            group.reduce { acc, str ->
                acc + str
            }.toSet()
        }.mapIndexed { groupIndex, group ->
            group.mapNotNull { answer ->
                if (
                    input[groupIndex].all { personAnswers ->
                        answer in personAnswers
                    }
                ) answer else null
            }.size
        }.sum()
    )
}
