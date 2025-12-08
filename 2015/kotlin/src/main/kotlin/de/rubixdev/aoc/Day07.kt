package de.rubixdev.aoc

// line = expr , '->' , wire ;
// expr = [ 'NOT' ] , atom
//      | atom , op , atom ;
// op   = 'AND' | 'OR' | 'LSHIFT' | 'RSHIFT' ;
// atom = wire | /\d+/ ;
// wire = /[a-z]+/ ;

sealed class Expr {
    val atoms get() = when (this) {
        is Atom -> listOf(this)
        is Not -> listOf(rhs)
        is And -> listOf(lhs, rhs)
        is Or -> listOf(lhs, rhs)
        is LShift -> listOf(lhs, rhs)
        is RShift -> listOf(lhs, rhs)
    }
}

sealed class Atom : Expr()
data class Wire(val name: String) : Atom()
data class Literal(val value: UShort) : Atom()
data class Not(val rhs: Atom) : Expr()
data class And(val lhs: Atom, val rhs: Atom) : Expr()
data class Or(val lhs: Atom, val rhs: Atom) : Expr()
data class LShift(val lhs: Atom, val rhs: Atom) : Expr()
data class RShift(val lhs: Atom, val rhs: Atom) : Expr()

private fun parseAtom(input: String): Atom =
    input.toUShortOrNull()?.let { Literal(it) } ?: Wire(input)

fun day7(input: String): Day = sequence {
    val example = """
        123 -> x
        456 -> y
        x AND y -> d
        x OR y -> e
        x LSHIFT 2 -> f
        y RSHIFT 2 -> g
        NOT x -> h
        NOT y -> i
    """.trimIndent()
//    val input = example.lines()
    val input = input.lines()
        .map { line ->
            val (expr, dest) = line.split(" -> ")
            val exprParts = expr.split(' ')
            val src = when (exprParts.size) {
                1 -> parseAtom(expr)

                2 -> Not(parseAtom(exprParts[1]))

                3 -> when (exprParts[1]) {
                    "AND" -> And(parseAtom(exprParts[0]), parseAtom(exprParts[2]))
                    "OR" -> Or(parseAtom(exprParts[0]), parseAtom(exprParts[2]))
                    "LSHIFT" -> LShift(parseAtom(exprParts[0]), parseAtom(exprParts[2]))
                    "RSHIFT" -> RShift(parseAtom(exprParts[0]), parseAtom(exprParts[2]))
                    else -> throw IllegalStateException("invalid input")
                }

                else -> throw IllegalStateException("invalid input")
            }
            src to dest
        }

    yield(null)
    val a = part1(input).also { yield(it) }
    yield(part2(input, a))
}

private fun run(
    instructions: List<Pair<Expr, String>>,
    state: Map<String, UShort> = mutableMapOf(),
): Map<String, UShort> {
    val instrs = instructions.toMutableList()
    val wires = state.toMutableMap()

    fun interpretAtom(atom: Atom) = when (atom) {
        is Literal -> atom.value
        is Wire -> wires[atom.name]!!
    }

    while (instrs.isNotEmpty()) {
        instrs.removeAll { (expr, dest) ->
            (expr.atoms.all { it is Literal || (it is Wire && it.name in wires) }).also {
                if (it) {
                    wires[dest] = when (expr) {
                        is Atom -> interpretAtom(expr)
                        is Not -> interpretAtom(expr.rhs).inv()
                        is And -> interpretAtom(expr.lhs) and interpretAtom(expr.rhs)
                        is Or -> interpretAtom(expr.lhs) or interpretAtom(expr.rhs)
                        is LShift -> interpretAtom(expr.lhs) shl interpretAtom(expr.rhs)
                        is RShift -> interpretAtom(expr.lhs) shr interpretAtom(expr.rhs)
                    }
                }
            }
        }
    }
    return wires
}

private fun part1(input: List<Pair<Expr, String>>) = run(input)["a"]!!
private fun part2(input: List<Pair<Expr, String>>, a: UShort) = run(
    input.filter { it.second != "b" } + (Literal(a) to "b"),
)["a"]!!
