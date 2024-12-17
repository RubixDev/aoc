package de.rubixdev

import java.io.File
import kotlin.math.pow

fun runDay17() {
    val example = """
    Register A: 729
    Register B: 0
    Register C: 0

    Program: 0,1,5,4,3,0
    """.trimIndent()
    val example2 = """
    Register A: 2024
    Register B: 0
    Register C: 0

    Program: 0,3,5,4,3,0
    """.trimIndent()
//    val (rawRegisters, rawProgram) = example
//    val (rawRegisters, rawProgram) = example2
    val (rawRegisters, rawProgram) = File("inputs/day17.txt").readText().trim()
        .split("\n\n")
    val registers = rawRegisters.lines().map { line -> line.split(": ")[1].toLong() }.toTriple()
    val instructions = rawProgram.split(": ")[1].split(",").map {
        when (it) {
            "0" -> Instruction.ADV
            "1" -> Instruction.BXL
            "2" -> Instruction.BST
            "3" -> Instruction.JNZ
            "4" -> Instruction.BXC
            "5" -> Instruction.OUT
            "6" -> Instruction.BDV
            "7" -> Instruction.CDV
            else -> throw RuntimeException("malformed input")
        }
    }

    println("--- Day 17 ---")
    println("Part 1: ${part1(instructions, registers)}")
    println("Part 2: ${part2(instructions, registers)}")
}

enum class Instruction(val int: Int) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7),
}

private fun part1(instructions: List<Instruction>, registers: Triple<Long, Long, Long>) =
    run(instructions, registers).joinToString(",")

private fun part2(instructions: List<Instruction>, registers: Triple<Long, Long, Long>) =
    part2(instructions, registers.second, registers.third)

private fun part2(instructions: List<Instruction>, b: Long, c: Long, i: Int = 0, skip: Int = 0): Long {
    if (i >= instructions.size) return 0
    return (0..Int.MAX_VALUE).firstNotNullOf {
        val min = 8 * part2(instructions, b, c, i + 1, it)
        val max = if (i == instructions.lastIndex) 8.0.pow(i).toLong() else min + 8
        val currInstr = instructions[i].int.toLong()
        (min..<max).asSequence()
            .filter { a -> run(instructions, Triple(a, b, c)).first() == currInstr }
            // don't ask
            .drop((skip - (0..<it).sumOf { s ->
                val sMin = 8 * part2(instructions, b, c, i + 1, s)
                val sMax = if (i == instructions.lastIndex) 8.0.pow(i).toLong() else sMin + 8
                val sCurrInstr = instructions[i].int.toLong()
                (sMin..<sMax).asSequence()
                    .filter { a -> run(instructions, Triple(a, b, c)).first() == sCurrInstr }
                    .count()
            }).coerceAtLeast(0))
            .firstOrNull()
    }
}

private fun run(instructions: List<Instruction>, registers: Triple<Long, Long, Long>) = sequence {
    var (a, b, c) = registers

    fun getCombo(operand: Int): Long =
        when (operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw RuntimeException("invalid operand '$operand'")
        }

    var pc = 0
    while (pc in instructions.indices) {
        val operand = instructions[pc + 1].int
        when (instructions[pc]) {
            Instruction.ADV -> a /= 2.0.pow(getCombo(operand).toInt()).toLong()
            Instruction.BXL -> b = b xor operand.toLong()
            Instruction.BST -> b = getCombo(operand) and 0b111
            Instruction.JNZ -> if (a != 0L) {
                pc = operand
                continue
            }

            Instruction.BXC -> b = b xor c
            Instruction.OUT -> yield(getCombo(operand) and 0b111)
            Instruction.BDV -> b = a / 2.0.pow(getCombo(operand).toInt()).toLong()
            Instruction.CDV -> c = a / 2.0.pow(getCombo(operand).toInt()).toLong()
        }
        pc += 2
    }
}
