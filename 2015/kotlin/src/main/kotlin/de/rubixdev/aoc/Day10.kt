package de.rubixdev.aoc

fun day10(input: String): Day = sequence {
    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun lookAndSay(input: String) = lookAndSay(input.toList(), StringBuilder()).toString()
private tailrec fun lookAndSay(rest: List<Char>, acc: StringBuilder): StringBuilder =
    when (rest.isEmpty()) {
        true -> acc

        false -> {
            val num = rest.first()
            val count = rest.takeWhile { it == num }.count()
            lookAndSay(
                // subList doesn't copy the list, which is very much needed for performance here
                rest.subList(count, rest.size),
                acc.apply {
                    append(count)
                    append(num)
                },
            )
        }
    }

// private fun lookAndSayIter(input: String): String {
//    var rest = input.toList()
//    return buildString {
//        while (rest.isNotEmpty()) {
//            val num = rest.first()
//            val count = rest.takeWhile { it == num }.count()
//            rest = rest.subList(count, rest.size)
//            append(count)
//            append(num)
//        }
//    }
// }

private fun part(input: String, n: Int) = generateSequence(input) { lookAndSay(it) }
    .drop(n).first().length

private fun part1(input: String) = part(input, 40)
private fun part2(input: String) = part(input, 50)
