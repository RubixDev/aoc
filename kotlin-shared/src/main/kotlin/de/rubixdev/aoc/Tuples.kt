package de.rubixdev.aoc

fun <A, B> Pair<A, A>.map(mapper: (A) -> B): Pair<B, B> = mapper(first) to mapper(second)
fun <A, B, C> Pair<A, B>.mapFirst(mapper: (A) -> C): Pair<C, B> = mapper(first) to second
fun <A, B, C> Pair<A, B>.mapSecond(mapper: (B) -> C): Pair<A, C> = first to mapper(second)

fun <A, B> Triple<A, A, A>.map(mapper: (A) -> B): Triple<B, B, B> =
    Triple(mapper(first), mapper(second), mapper(third))
fun <A, B, C, D> Triple<A, B, C>.mapFirst(mapper: (A) -> D): Triple<D, B, C> =
    Triple(mapper(first), second, third)
fun <A, B, C, D> Triple<A, B, C>.mapSecond(mapper: (B) -> D): Triple<A, D, C> =
    Triple(first, mapper(second), third)
fun <A, B, C, D> Triple<A, B, C>.mapThird(mapper: (C) -> D): Triple<A, B, D> =
    Triple(first, second, mapper(third))

fun <T> List<T>.toPair(): Pair<T, T> = this[0] to this[1]

fun <T> List<T>.toTriple(): Triple<T, T, T> = Triple(this[0], this[1], this[2])

fun <T> Pair<T, T>.toList() = listOf(first, second)
fun <T> Pair<T, T>.toSet() = setOf(first, second)
fun <T> Triple<T, T, T>.toList() = listOf(first, second, third)
fun <T> Triple<T, T, T>.toSet() = setOf(first, second, third)

fun <A, B, R> Iterable<Pair<A, B>>.foldFirst(initial: R, operation: (R, A) -> R) =
    fold(initial) { acc, e -> operation(acc, e.first) } to map { it.second }

fun <A, B, R> Sequence<Pair<A, B>>.foldFirst(initial: R, operation: (R, A) -> R) =
    fold(initial) { acc, e -> operation(acc, e.first) } to map { it.second }
