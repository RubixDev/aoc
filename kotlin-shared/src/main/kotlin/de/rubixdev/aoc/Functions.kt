package de.rubixdev.aoc

// I miss Rust macros
class MemoizedFun1<X, R>(val recurse: (X) -> R)
fun <X, R> memoize(impl: MemoizedFun1<X, R>.(X) -> R): (X) -> R {
    val cache = mutableMapOf<X, R>()
    fun f(x: X): R = cache.getOrPut(x) {
        MemoizedFun1(::f).impl(x)
    }
    return ::f
}

class MemoizedFun2<X, Y, R>(val recurse: (X, Y) -> R)
fun <X, Y, R> memoize(impl: MemoizedFun2<X, Y, R>.(X, Y) -> R): (X, Y) -> R {
    val cache = mutableMapOf<Pair<X, Y>, R>()
    fun f(x: X, y: Y): R = cache.getOrPut(x to y) {
        MemoizedFun2(::f).impl(x, y)
    }
    return ::f
}

class MemoizedFun3<X, Y, Z, R>(val recurse: (X, Y, Z) -> R)
fun <X, Y, Z, R> memoize(impl: MemoizedFun3<X, Y, Z, R>.(X, Y, Z) -> R): (X, Y, Z) -> R {
    val cache = mutableMapOf<Triple<X, Y, Z>, R>()
    fun f(x: X, y: Y, z: Z): R = cache.getOrPut(Triple(x, y, z)) {
        MemoizedFun3(::f).impl(x, y, z)
    }
    return ::f
}
