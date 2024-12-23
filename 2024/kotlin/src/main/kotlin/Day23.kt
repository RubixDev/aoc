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

    yield(Unit)
    val graph = makeGraph(input)
    yield(part1(graph))
    yield(part2(graph))
}

private fun makeGraph(input: List<Pair<String, String>>): Map<String, List<String>> =
    input.flatMap { (a, b) -> listOf(a to b, b to a) }
        .distinct()
        .groupBy({ it.first }, { it.second })

private fun part1(graph: Map<String, List<String>>): Int =
        graph.asSequence()
            .flatMap { (pc, adj) ->
                adj.flatMap { a ->
                    adj.filter { b -> a != b && b in graph[a]!! && a in graph[b]!! }
                        .map { setOf(pc, a, it) }
                }
            }
            .toSet()
            .filter { group -> group.any { it.startsWith('t') } }
            .size

// Bron-Kerbosch algorithm
private fun findMaximalClique(
    r: Set<String> = setOf(),
    p: Set<String>,
    x: Set<String> = setOf(),
    graph: Map<String, List<String>>,
): Set<Set<String>> {
    val cliques = mutableSetOf<Set<String>>()
    if (p.isEmpty() && x.isEmpty()) {
        return cliques.apply { add(r) }
    }

    val pCopy = p.toMutableSet()
    val xCopy = x.toMutableSet()
    while (!pCopy.isEmpty()) {
        val v = pCopy.first()
        val newR = r.toMutableSet().apply { add(v) }
        val newP = pCopy.toMutableSet().apply { retainAll(graph[v]!!) }
        val newX = xCopy.toMutableSet().apply { retainAll(graph[v]!!) }
        cliques.addAll(findMaximalClique(newR, newP, newX, graph))
        pCopy.remove(v)
        xCopy.add(v)
    }

    return cliques
}

private fun part2(graph: Map<String, List<String>>): String =
    findMaximalClique(p = graph.keys, graph = graph)
        .maxBy { it.size }
        .let { clique -> clique.sorted().joinToString(",") }
