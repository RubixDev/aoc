package de.rubixdev.aoc2020

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    val time = measureTime {
        runDay1()
        runDay2()
        runDay3()
        runDay4()
        runDay5()
        runDay6()
        runDay7()
        runDay8()
        runDay10()
        runDay11()
        runDay12()
        runDay13()
        runDay25()
    }
    println("--------------\nExecution took $time")
}
