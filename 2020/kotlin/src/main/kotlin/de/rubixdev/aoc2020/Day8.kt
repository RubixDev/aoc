package de.rubixdev.aoc2020

fun runDay8() {
    println("--- DAY 08 ---")

    val input = object {}.javaClass.getResource("/input08.txt")!!.readText().split('\n').map {
        it.substring(0..2) to it.substring(4).toInt()
    }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<Pair<String, Int>>) {
    println(runInstructions(input).first)
}

private fun part2(input: List<Pair<String, Int>>) {
    val possibleInstructions = mutableListOf<List<Pair<String, Int>>>()
    for ((index, instruction) in input.withIndex()) {
        if (instruction.first != "acc") {
            possibleInstructions.add(input.mapIndexed { i, it ->
                (if (i == index) (if (it.first == "nop") "jmp" else "nop") else it.first) to it.second
            })
        }
    }

    val executedInstructions = possibleInstructions.map { runInstructions(it) }
    println(executedInstructions.find { it.second }!!.first)
}

private fun runInstructions(instructions: List<Pair<String, Int>>): Pair<Int, Boolean> {
    var acc = 0
    var pos = 0
    val executedIndices = mutableListOf<Int>()

    while (pos !in executedIndices && pos < instructions.size) {
        executedIndices.add(pos)
        val (instruction, value) = instructions[pos]

        when (instruction) {
            "acc" -> {
                acc += value
                pos++
            }
            "jmp" -> pos += value
            "nop" -> pos++
        }
    }
    return acc to (pos == instructions.size)
}
