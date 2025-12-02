package de.rubixdev.aoc

fun day22(input: String): Day = sequence {
    val example = """
    1
    10
    100
    2024
    """.trimIndent()
    val example2 = """
    1
    2
    3
    2024
    """.trimIndent()
    // val input = example.lines()
    // val input = example2.lines()
    val input = input.lines()
        .map { line -> line.toLong() }

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun pseudoRandom(seed: Long): Sequence<Long> = sequence {
    var num = seed
    while (true) {
        yield(num)
        num = (num xor (num shl 6)) and 0xffffff
        num = (num xor (num shr 5)) and 0xffffff
        num = (num xor (num shl 11)) and 0xffffff
    }
}

private fun part1(input: List<Long>): Long =
    input.sumOf { seed -> pseudoRandom(seed).drop(2000).first() }

private fun asIndex(a: Byte, b: Byte, c: Byte, d: Byte): Int =
    (a + 9) * 6859 + (b + 9) * 361 + (c + 9) * 19 + (d + 9)

private fun Sequence<Pair<List<Byte>, Long>>.toLookupTable(): Array<Long?> {
    val lookupTable: Array<Long?> = Array(130_321) { null }
    for ((seq, price) in this) {
        val (a, b, c, d) = seq
        val idx = asIndex(a, b, c, d)
        if (lookupTable[idx] == null) {
            lookupTable[idx] = price
        }
    }
    return lookupTable
}

private fun Sequence<Array<Long?>>.mergeTables(): Array<Long> {
    val lookupTable = Array(130_321) { 0L }
    for (table in this) {
        for ((idx, price) in table.withIndex()) {
            if (price != null) {
                lookupTable[idx] += price
            }
        }
    }
    return lookupTable
}

private fun part2(input: List<Long>): Long = input.asSequence()
    .map { seed ->
        pseudoRandom(seed).take(2000)
            .map { it % 10 }
            .windowed(2)
            .map { (a, b) -> b to (b - a).toByte() }
            .windowed(4)
            .map { (a, b, c, d) ->
                listOf(a.second, b.second, c.second, d.second) to d.first
            }
            .toLookupTable()
    }
    .mergeTables()
    .max()
