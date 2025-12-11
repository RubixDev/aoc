package de.rubixdev.aoc

fun day3(input: String): Day = sequence {
    val example = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent().trim()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.map { it.digitToInt() } }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: List<List<Int>>) = input.sumOf { line ->
    line.withIndex().maxOf { (i1, b1) ->
        line.drop(i1 + 1).maxOfOrNull { it + b1 * 10 } ?: 0
    }
}

private fun part2(input: List<List<Int>>) = input.sumOf { line ->
    fun findMax(search: List<Int>, result: List<Int> = listOf()): Long = when (result.size) {
        12 -> result.fold(0L) { acc, d -> acc * 10 + d }

        else -> search.dropLast(11 - result.size).withIndex().maxBy { it.value }.let {
            findMax(search.drop(it.index + 1), result + it.value)
        }
    }
    findMax(line)
}

/**
 * My original solution to part 2, which I've not seen anywhere else yet, but
 * also isn't very pretty.
 */
private fun part2Old(input: List<List<Int>>) = input.sumOf { line ->
    val num = MutableList<Int?>(line.size) { null }
    var count = 0
    outer@for (n in 9 downTo 0) {
        for ((i, _) in line.withIndex().filter { it.value == n }.asReversed()) {
            val restNum = num.drop(i)
            val nextBiggerIdx = restNum.indexOfFirst { it != null && it > n }
            if (nextBiggerIdx != -1 &&
                restNum.drop(nextBiggerIdx).count { it == null } + count >= 12
            ) {
                break
            }
            num[i] = n
            count++
            if (count >= 12) break@outer
        }
    }
    num.filterNotNull().fold(0L) { acc, d -> acc * 10 + d }
}
