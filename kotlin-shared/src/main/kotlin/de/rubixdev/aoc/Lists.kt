package de.rubixdev.aoc

fun Iterable<Int>.product() = fold(1) { acc, e -> acc * e }
fun Sequence<Int>.product() = fold(1) { acc, e -> acc * e }
fun Iterable<Long>.product() = fold(1L) { acc, e -> acc * e }
fun Sequence<Long>.product() = fold(1L) { acc, e -> acc * e }

operator fun <T> List<T>.component6(): T = get(5)

fun <T> List<List<T>>.cartesianProduct(): Sequence<List<T>> = when (size) {
    0 -> sequenceOf()

    1 -> get(0).asSequence().map { listOf(it) }

    else -> get(0).asSequence().flatMap { a ->
        drop(1).cartesianProduct().map { b ->
            listOf(a) + b
        }
    }
}

// may lead to unintended results with unordered collections
fun <T> Collection<T>.permutations(): Sequence<List<T>> = when (isEmpty()) {
    true -> sequenceOf(listOf())
    false -> sequence {
        drop(1).permutations().forEach { perm ->
            (0..perm.size).forEach { i ->
                yield(perm.toMutableList().apply { add(i, this@permutations.first()) })
            }
        }
    }
}
