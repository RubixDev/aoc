package de.rubixdev

import kotlin.time.measureTime

private fun run(day: () -> Unit) {
    val time = measureTime { day() }
    println("\u001b[37m$time\u001b[0m\n")
}

private val DAYS = listOf(
    ::runDay1,
    ::runDay2,
    ::runDay3,
    ::runDay4,
    ::runDay5,
    ::runDay6,
    ::runDay7,
    ::runDay8,
    ::runDay9,
    ::runDay10,
    ::runDay11,
    ::runDay12,
    ::runDay13,
    ::runDay14,
    ::runDay15,
    ::runDay16,
    ::runDay17,
    ::runDay18,
    ::runDay19,
    ::runDay20,
)

fun main() {
    for (day in DAYS) run(day)
}
