package de.rubixdev.aoc2020

fun runDay7() {
    println("--- DAY 07 ---")

    val input = object {}.javaClass.getResource("/input07.txt")!!.readText().split('\n').associate { line ->
        line.split(" bags")[0] to line.split("contain ")[1]
            .replace("bags", "bag")
            .substringBeforeLast(" bag.")
            .split(" bag, ")
            .mapNotNull { bag ->
                if ("no" in bag)
                    null
                else
                    bag.substringBefore(' ').toInt() to bag.substringAfter(' ')
            }
    }

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: Map<String, List<Pair<Int, String>>>) {
    val inBags = input.map { bag1 ->
        bag1.key to input.mapNotNull { bag2 ->
            if (bag1.key in bag2.value.map { bag3 -> bag3.second }) bag2.key else null
        }
    }.toMap()

    fun findBag(bag: String): List<String> {
        val isInBags = inBags[bag]!!.map { findBag(it) }
        val isInBagsFlat = if (isInBags.isEmpty()) {
            listOf()
        } else {
            isInBags.reduce { acc, list -> acc + list }
        }
        return inBags[bag]!! + isInBagsFlat
    }

    println(findBag("shiny gold").toSet().size)
}

private fun part2(input: Map<String, List<Pair<Int, String>>>) {
    fun numOfBagsIn(bag: String): Int {
        return if (input[bag]!!.isEmpty()) 0 else {
            input[bag]!!.map { it.first + it.first * numOfBagsIn(it.second) }.sum()
        }
    }
    println(numOfBagsIn("shiny gold"))
}
