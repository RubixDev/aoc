package de.rubixdev

import java.security.MessageDigest

fun day4(): Day = sequence {
    val input = getInput(4)

    yield(Unit)
    val part1 = part(input, 5)
    yield(part1)
    yield(part(input, 6, part1))
}

private val MD5 = MessageDigest.getInstance("MD5")

private fun part(input: String, zeros: Int, min: Int = 0) = (min..Int.MAX_VALUE).first { n ->
    MD5.digest("$input$n".toByteArray())
        .joinToString("") { "%02x".format(it) }
        .startsWith("0".repeat(zeros))
}
