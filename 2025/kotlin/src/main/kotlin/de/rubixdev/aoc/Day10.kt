package de.rubixdev.aoc

import hm.binkley.math.fixed.toBigRational
import hm.binkley.math.isPositive
import hm.binkley.math.isZero
import hm.binkley.math.fixed.FixedBigRational as Ratio

typealias Lights = List<Boolean>
typealias Button = List<Int>
typealias Joltage = List<Int>
typealias Machine = Triple<Lights, List<Button>, Joltage>
typealias Input = List<Machine>
private typealias Matrix = List<List<Ratio>>

fun day10(input: String): Day = sequence {
    val example = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()
//    val input: Input = example.lines()
    val input: Input = input.lines()
        .map { line -> line.split(' ') }
        .map { line ->
            Triple(
                line.first().drop(1).dropLast(1).map { it == '#' },
                line.drop(1).dropLast(1).map { btn ->
                    btn.drop(1).dropLast(1).split(',').map { it.toInt() }
                },
                line.last().drop(1).dropLast(1).split(',').map { it.toInt() },
            )
        }

    yield(null)
    yield(part1(input))
    yield(part2(input))
}

private fun part1(input: Input) = input.sumOf { machine ->
    machine.second.powerset().filter { set ->
        set.isNotEmpty() && machine.first.withIndex().all { light ->
            (set.count { light.index in it } % 2 != 0) == light.value
        }
    }.minOf { it.size }
}

private fun Matrix.showMatrix(): String = joinToString("\n") { line ->
    line.joinToString(" ") { it.toString().padStart(5, ' ') }
}

/**
 * Turn this matrix into reduced row echelon form.
 *
 * With help from [Rosetta Code][https://rosettacode.org/wiki/Gauss-Jordan_matrix_inversion#Kotlin],
 * because I just want to get this done.
 */
private fun MutableList<MutableList<Ratio>>.toRref() {
    var skipCount = 0
    val rowCount = size
    val colCount = this[0].size
    for (r in 0..<rowCount) {
        if (colCount <= skipCount) return
        var i = r
        while (this[i][skipCount].isZero()) {
            i++
            if (rowCount == i) {
                i = r
                skipCount++
                if (colCount == skipCount) return
            }
        }

        // swap rows
        val tmp = this[i]
        this[i] = this[r]
        this[r] = tmp

        if (!this[r][skipCount].isZero()) {
            val div = this[r][skipCount]
            for (j in 0..<colCount) {
                this[r][j] /= div
            }
        }

        for (k in 0..<rowCount) {
            if (k != r) {
                val mul = this[k][skipCount]
                for (j in 0..<colCount) this[k][j] -= this[r][j] * mul
            }
        }

        skipCount++
    }
}

private fun Matrix.rref(): Matrix = map { it.toMutableList() }.toMutableList().apply { toRref() }

private sealed class X
private data object FreeX : X()
private data class FixedX(val calc: (getter: (Int) -> Ratio) -> Ratio) : X()

// perf can probably be improved by using doubles and partial pivoting instead of BigRationals
// and cleaning up my dumbass "find smallest x" code
private fun part2(input: Input) = input.sumOf { machine ->
    val matrix = machine.third.mapIndexed { idx, joltage ->
        machine.second.map { (idx in it).toInt().toBigRational() } + joltage.toBigRational()
    }
    val rref = matrix.rref()
    val transposed = rref.transpose()

    val xs = transposed.dropLast(1).mapIndexed { x, col ->
        when (col.count { !it.isZero() } > 1) {
            true -> FreeX

            false -> FixedX { getter ->
                val row = rref.first { row -> row.indexOfFirst { !it.isZero() } == x }
                row.last() - row.withIndex().drop(x + 1).dropLast(1)
                    .fold(Ratio.ZERO) { acc, (idx, mul) ->
                        when (mul) {
                            Ratio.ZERO -> acc
                            else -> acc + getter(idx) * mul
                        }
                    }
            }
        }
    }

    if (xs.all { it is FixedX }) return@sumOf transposed.last().sumOf { it.toLong() }
    xs.withIndex()
        .filter { it.value is FreeX }
        .map { (idx, _) -> 0..(machine.second[idx].maxOf { machine.third[it] }) }
        .cartesianProduct()
        .mapNotNull { freeXValues ->
            val xValues = MutableList(transposed.size - 1) { Ratio.ZERO }

            xs.withIndex().filter { it.value is FreeX }.withIndex().forEach { (valueIdx, e) ->
                val (xIdx, _) = e
                xValues[xIdx] = freeXValues[valueIdx].toBigRational()
            }
            for ((idx, x) in xs.withIndex()) {
                if (x is FixedX) {
                    xValues[idx] = x.calc(xValues::get)
                }
            }

            when (xValues.all { (it.isZero() || it.isPositive()) && it.isWhole() }) {
                true -> xValues.sumOf { it.toLong() }
                false -> null
            }
        }
        .min()
}
