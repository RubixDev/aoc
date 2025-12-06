package de.rubixdev.aoc

fun day6(input: String): Day = sequence {
    val example = """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
    """.trimIndent()
//    val ops = example.lines().last().trim().split("""\s+""".toRegex())
    val ops = input.lines().last().trim().split("""\s+""".toRegex())
//    val lines = example.lines().dropLast(1)
    val lines = input.lines().dropLast(1)
    val input = lines.first().indices.map { i -> lines.map { it[i] } }
        .fold(listOf(lines.indices.map { "" })) { acc, c ->
            when (c.all { it == ' ' }) {
                true -> acc.plusElement(lines.indices.map { "" })
                false -> acc.dropLast(1).plusElement(acc.last().zip(c).map { (a, b) -> a + b })
            }
        }

    yield(Unit)
    yield(part1(input, ops))
    yield(part2(input, ops))
}

private fun List<Long>.applyOp(op: String): Long = when (op) {
    "*" -> product()
    "+" -> sum()
    else -> throw IllegalStateException("invalid input")
}

private fun part1(input: List<List<String>>, ops: List<String>) =
    ops.zip(input).sumOf { (op, nums) -> nums.map { it.trim().toLong() }.applyOp(op) }

private fun part2(input: List<List<String>>, ops: List<String>) =
    ops.zip(input).sumOf { (op, nums) ->
        nums.first().indices.map { i ->
            nums.fold(0L) { acc, c ->
                c[i].digitToIntOrNull()?.toLong()?.let { acc * 10 + it } ?: acc
            }
        }.applyOp(op)
    }
