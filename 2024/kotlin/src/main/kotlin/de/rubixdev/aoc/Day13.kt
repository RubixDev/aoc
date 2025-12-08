package de.rubixdev.aoc

fun day13(input: String): Day = sequence {
    val example = "Button A: X+94, Y+34\n" +
        "Button B: X+22, Y+67\n" +
        "Prize: X=8400, Y=5400\n" +
        "\n" +
        "Button A: X+26, Y+66\n" +
        "Button B: X+67, Y+21\n" +
        "Prize: X=12748, Y=12176\n" +
        "\n" +
        "Button A: X+17, Y+86\n" +
        "Button B: X+84, Y+37\n" +
        "Prize: X=7870, Y=6450\n" +
        "\n" +
        "Button A: X+69, Y+23\n" +
        "Button B: X+27, Y+71\n" +
        "Prize: X=18641, Y=10279"
//    val input = example
    val input = input
        .split("\n\n")
        .map { claw ->
            claw.lines().take(3).map {
                Regex("""X[+=](\d+), Y[+=](\d+)""").find(it)!!.destructured.let { (x, y) ->
                    x.toLong() by y.toLong()
                }
            }.toTriple()
        }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

// Prompted Wolfram Alpha with:
// solve i*{{Subscript[a,x]},{Subscript[a,y]}}+j*{{Subscript[b,x]},{Subscript[b,y]}}={{Subscript[t,x]},{Subscript[t,y]}} , for i and j
private fun part1(input: List<Triple<Vec2, Vec2, Vec2>>): Long = input.sumOf { (a, b, t) ->
    val i = (b.y * t.x - b.x * t.y) / (a.x * b.y - a.y * b.x)
    val j = (a.y * t.x - a.x * t.y) / (a.y * b.x - a.x * b.y)
    if (i * a + j * b == t) 3 * i + j else 0
}

private fun part2(input: List<Triple<Vec2, Vec2, Vec2>>): Long =
    part1(input.map { (a, b, t) -> Triple(a, b, t + Vec2(10000000000000)) })

// private fun part1(input: List<Triple<Vec2, Vec2, Vec2>>): Long =
//    input.sumOf { (a, b, target) ->
//        (min(target.x / b.x, target.y / b.y) downTo 0).firstNotNullOfOrNull { i ->
//            val diff = target - b * i
//            val j = diff.x / a.x
//            when (a.x * j == diff.x && a.y * j == diff.y) {
//                true -> i + 3 * j
//                false -> null
//            }
//        } ?: 0
//    }
