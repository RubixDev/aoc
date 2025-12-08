package de.rubixdev.aoc

fun day8(input: String): Day = sequence {
    val example = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line -> line.split(',').map { it.toLong() }.toVec3() }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun List<Vec3>.sortedWithDistances() = asSequence()
    .withIndex()
    .flatMap { (idx, a) -> drop(idx + 1).map { b -> Triple(a, b, (a - b).length) } }
    .sortedBy { it.third }

private fun List<Set<Vec3>>.connect(a: Vec3, b: Vec3): List<Set<Vec3>> {
    val aAndB = filter { a in it || b in it }
    return when (aAndB.size) {
        1 -> this
        else -> filter { it !in aAndB }.plusElement(aAndB.reduce { union, set -> union + set })
    }
}

private fun part1(input: List<Vec3>) = input.sortedWithDistances()
    .take(1000)
    .fold(input.map { setOf(it) }) { circuits, (a, b, _) -> circuits.connect(a, b) }
    .map { it.size.toLong() }
    .sortedDescending()
    .take(3)
    .product()

private fun part2(input: List<Vec3>) = input.sortedWithDistances()
    .scan(input.map { setOf(it) } to 0L) { (circuits, _), (a, b, _) ->
        circuits.connect(a, b) to (a.x * b.x)
    }
    .dropWhile { it.first.size > 1 }
    .first()
    .second
