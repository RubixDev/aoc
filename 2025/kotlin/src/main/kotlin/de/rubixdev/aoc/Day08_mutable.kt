package de.rubixdev.aoc

fun day8Mutable(input: String): Day = sequence {
    val input = input.lines()
        .map { line -> line.split(',').map { it.toLong() }.toVec3() }
    val sortedPairs = input.asSequence()
        .withIndex()
        .flatMap { (idx, a) -> input.drop(idx + 1).map { b -> Triple(a, b, (a - b).length) } }
        .sortedBy { it.third }
        .toList()

    yield("8 (with mutable state)")
    yield(part1(input, sortedPairs))
    yield(part2(input, sortedPairs))
}

private fun MutableList<List<Vec3>>.connect(a: Vec3, b: Vec3): MutableList<List<Vec3>> {
    val aAndB = filter { a in it || b in it }
    return when (aAndB.size) {
        1 -> this

        else -> apply {
            removeAll { a in it || b in it }
            add(aAndB.reduce { union, set -> union + set })
        }
    }
}

private fun part1(input: List<Vec3>, sortedPairs: List<Triple<Vec3, Vec3, Double>>) =
    sortedPairs.asSequence()
        .take(1000)
        .fold(input.map { listOf(it) }.toMutableList()) { circuits, (a, b, _) ->
            circuits.connect(a, b)
        }
        .asSequence()
        .map { it.size.toLong() }
        .sortedDescending()
        .take(3)
        .product()

private fun part2(input: List<Vec3>, sortedPairs: List<Triple<Vec3, Vec3, Double>>) =
    sortedPairs.asSequence()
        .scan(input.map { listOf(it) }.toMutableList() to 0L) { (circuits, _), (a, b, _) ->
            circuits.connect(a, b) to (a.x * b.x)
        }
        .dropWhile { it.first.size > 1 }
        .first()
        .second
