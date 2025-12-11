package de.rubixdev.aoc

fun day11(input: String): Day = sequence {
    val example = """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent()
    val example2 = """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.replace(":", "").split(" ") }
        .associate { list -> list.first() to list.drop(1) }
//    val input2 = example2.lines()
//        .map { line -> line.replace(":", "").split(" ") }
//        .associate { list -> list.first() to list.drop(1) }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: Map<String, List<String>>): Long {
    fun dfs(curr: String): Long = when (curr) {
        "out" -> 1
        else -> input[curr]!!.sumOf { dfs(it) }
    }

    return dfs("you")
}

private fun part2(input: Map<String, List<String>>): Long {
    val dfs: (String, Boolean, Boolean) -> Long = memoize { node, dac, fft ->
        when (node) {
            "out" -> (dac && fft).toLong()

            else -> input[node]?.sumOf { recurse(it, dac || node == "dac", fft || node == "fft") }
                ?: 0L
        }
    }

    return dfs("svr", false, false)
}
