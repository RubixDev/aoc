package de.rubixdev

import java.io.File

fun day24(): Day = sequence {
    val example = """
    x00: 1
    x01: 0
    x02: 1
    x03: 1
    x04: 0
    y00: 1
    y01: 1
    y02: 1
    y03: 1
    y04: 1

    ntg XOR fgs -> mjb
    y02 OR x01 -> tnw
    kwq OR kpj -> z05
    x00 OR x03 -> fst
    tgd XOR rvg -> z01
    vdt OR tnw -> bfw
    bfw AND frj -> z10
    ffh OR nrd -> bqk
    y00 AND y03 -> djm
    y03 OR y00 -> psh
    bqk OR frj -> z08
    tnw OR fst -> frj
    gnj AND tgd -> z11
    bfw XOR mjb -> z00
    x03 OR x00 -> vdt
    gnj AND wpb -> z02
    x04 AND y00 -> kjc
    djm OR pbm -> qhw
    nrd AND vdt -> hwm
    kjc AND fst -> rvg
    y04 OR y02 -> fgs
    y01 AND x02 -> pbm
    ntg OR kjc -> kwq
    psh XOR fgs -> tgd
    qhw XOR tgd -> z09
    pbm OR djm -> kpj
    x03 XOR y03 -> ffh
    x00 XOR y04 -> ntg
    bfw OR bqk -> z06
    nrd XOR fgs -> wpb
    frj XOR qhw -> z04
    bqk OR frj -> z07
    y03 OR x01 -> nrd
    hwm AND bqk -> z03
    tgd XOR rvg -> z12
    tnw OR pbm -> gnj
    """.trimIndent()
    // val (rawInputs, rawGates) = example.split("\n\n")
    val (rawInputs, rawGates) = File("inputs/day24.txt").readText().trim().split("\n\n")
    val inputs = rawInputs.lines()
        .map { line -> line.split(": ") }
        .map { (wire, value) -> wire to value.toInt().toBoolean() }
        .toMap()
    val gates = rawGates.lines()
        .map { line -> line.split(" -> ") }
        .map { (calc, output) -> output to calc.split(' ').let { (a, op, b) -> op to setOf(a, b) } }
        .toMap()

    yield(Unit)
    yield(part1(inputs, gates))
    yield(part2(inputs, gates))
}

private fun part1(inputs: Map<String, Boolean>, gates: Map<String, Pair<String, Set<String>>>): Long {
    // val inputss = (0..44).map { bit -> "x${bit.toString().padStart(2, '0')}" to (11806310474565L.shr(bit).and(1) == 1L) }
    //     .plus((0..44).map { bit -> "y${bit.toString().padStart(2, '0')}" to (5785891298490L.shr(bit).and(1) == 1L) })
    //     .toMap()
    val wires = inputs.toMutableMap()
    val gatesToCompute = ArrayDeque(gates.entries.map { (k, v) -> k to v })
    while (gatesToCompute.isNotEmpty()) {
        val (output, compute) = gatesToCompute.removeFirst()
        val (op, ins) = compute
        if (ins.any { it !in wires }) {
            gatesToCompute.add(output to compute)
            continue
        }
        val (a, b) = ins.toList()
        wires[output] = when (op) {
            "AND" -> wires[a]!! and wires[b]!!
            "OR" -> wires[a]!! or wires[b]!!
            "XOR" -> wires[a]!! xor wires[b]!!
            else -> throw RuntimeException("illegal operator '$op'")
        }
    }
    return wires.filter { (k, _) -> k.startsWith('z') }
        .map { (k, v) -> k.drop(1).toInt() to v }
        .map { (bits, v) -> v.toLong() shl bits }
        .fold(0L) { acc, v -> acc or v }
}

private fun part2(inputs: Map<String, Boolean>, gates: Map<String, Pair<String, Set<String>>>): String {
    // z00 = x00 XOR y00
    // z01 = (x00 AND y00) XOR (x01 XOR y01)
    // z02 = ((x00 AND y00) AND (x01 XOR y01)) XOR (x02 XOR y02)
    // z03 = (((x00 AND y00) AND (x01 XOR y01)) AND (x02 XOR y02)) XOR (x03 XOR y03)

    // z27 <-> jcp
    // z18 <-> dhq
    // z22 <-> pdg
    // kfp <-> hbs
    //
    // NOT bch,fwt,mqf,pdg,z18,z22,z27,z45
    //
    // dhq,hbs,jcp,kfp,pdg,z18,z22,z27

    val ands = gates.filter { it.value.first == "AND" }
    val ors = gates.filter { it.value.first == "OR" }
    val xors = gates.filter { it.value.first == "XOR" }

    val wrong = mutableListOf<String>()

    for ((gate, compute) in ors) {
        val (op, ins) = compute
        if (gate.startsWith('z') && gate != "z45")
            println("WRONG:::: incorrect OR (direct output to z) ::: $gate").also { wrong.add(gate) }
        if (ins.any { it.startsWith('x') }) continue
        val (in1, in2) = ins.toList()
        val (carry, new) = when (gates[in1]!!.second.any { it.startsWith('x') }) {
            true -> in2 to in1
            false -> in1 to in2
        }
        val num = when (val a = gates[new]!!.second.first().drop(1).toLongOrNull()) {
            null -> {
                println("WRONG:::: incorrect OR (invalid num) ::: $gate : $new ${gates[new]}")
                // TODO: find which gate is wrong and which should be swapped with
                continue
            }
            else -> a
        }
        if (gates[new]!!.first != "AND")
            println("WRONG:::: incorrect OR (wrong op for new bit)::: $new").also { wrong.add(new) }
    }
    for ((gate, compute) in xors) {
        val (op, ins) = compute
        if (ins.any { it.startsWith('x') }) continue
        val (in1, in2) = ins.toList()
        val (carry, new) = when (gates[in1]!!.second.any { it.startsWith('x') }) {
            true -> in2 to in1
            false -> in1 to in2
        }
        val resNum = when (val a = gate.drop(1).toLongOrNull()) {
            null -> {
                println("WRONG:::: incorrect XOR (invalid num) ::: $gate")
                wrong.add(gate)
                // TODO: also find the z gate that should be swapped with
                continue
            }
            else -> a
        }
        val newNum = when (val a = gates[new]!!.second.first().drop(1).toLongOrNull()) {
            null -> {
                println("WRONG:::: incorrect XOR (invalid num) ::: $gate : $new ${gates[new]}")
                continue
            }
            else -> a
        }
        if (newNum != resNum)
            println("WRONG:::: incorrect XOR (wrong num)::: $gate != $new ${gates[new]}")
        if (gates[new]!!.first != "XOR")
            println("WRONG:::: incorrect XOR (wrong op for new bit)::: $new").also { wrong.add(new) }
    }
    for ((gate, compute) in ands) {
        val (op, ins) = compute
        if (gate.startsWith('z'))
            println("WRONG:::: incorrect AND (direct output to z) ::: $gate").also { wrong.add(gate) }
        if (ins.any { it.startsWith('x') }) continue
        val (in1, in2) = ins.toList()
        val (carry, new) = when (gates[in1]!!.second.any { it.startsWith('x') }) {
            true -> in2 to in1
            false -> in1 to in2
        }
        val num = gates[new]!!.second.first().drop(1).toLong()
        if (ins != gates["z${num.toString().padStart(2, '0')}"]!!.second)
            println("WRONG:::: incorrect AND::: $gate")
    }

    return wrong.sorted().joinToString(",")
}
