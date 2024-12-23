package de.rubixdev

import java.io.File

fun day23(): Day = sequence {
    val example = """
    kh-tc
    qp-kh
    de-cg
    ka-co
    yn-aq
    qp-ub
    cg-tb
    vc-aq
    tb-ka
    wh-tc
    yn-cg
    kh-ub
    ta-co
    de-co
    tc-td
    tb-wq
    wh-td
    ta-ka
    td-qp
    aq-cg
    wq-ub
    ub-vc
    de-ta
    wq-aq
    wq-vc
    wh-yn
    ka-de
    kh-ta
    co-tc
    wh-qp
    tb-vc
    td-yn
    """.trimIndent()
    // val input = example.lines()
    val input = File("inputs/day23.txt").readLines()
        .map { line -> line.split('-').toPair() }
        .flatMap { (a, b) -> listOf(a to b, b to a) }
        .distinct()
        .groupBy({ it.first }, { it.second })

    yield(Unit)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: Map<String, List<String>>): Int =
    input.asSequence()
        .flatMap { (pc, adj) ->
            adj.withIndex().flatMap { (i, a) ->
                adj.drop(i + 1).filter { b -> a in input[b]!! }
                    .map { setOf(pc, a, it) }
            }
        }
        .filter { group -> group.any { it.startsWith('t') } }
        .count() / 3

// Bron-Kerbosch algorithm
private fun findMaximalClique(
    r: Set<String> = setOf(),
    p: Set<String>,
    x: Set<String> = setOf(),
    graph: Map<String, List<String>>,
): List<Set<String>> {
    // r = required
    // p = possible
    // x = excluded
    val cliques = mutableListOf<Set<String>>()
    if (p.isEmpty()) {
        if (x.isEmpty()) cliques.add(r)
        return cliques
    }

    // u = pivot = vertex in p with highest degree
    val u = p.maxBy { graph[it]!!.size }
    // all vertices in p that aren't neighbours of u
    val todo = p.filter { v -> v !in graph[u]!! }.toMutableList()
    val pCopy = p.toMutableSet()
    val xCopy = x.toMutableSet()
    while (!todo.isEmpty()) {
        val v = todo.removeLast()
        pCopy.remove(v)
        val newR = r.toMutableSet().apply { add(v) }
        val newP = pCopy.intersect(graph[v]!!)
        val newX = xCopy.intersect(graph[v]!!)
        cliques.addAll(findMaximalClique(newR, newP, newX, graph))
        xCopy.add(v)
    }

    return cliques
}

private fun part2(input: Map<String, List<String>>): String =
    findMaximalClique(p = input.keys, graph = input)
        .maxBy { it.size }
        .let { clique -> clique.sorted().joinToString(",") }
