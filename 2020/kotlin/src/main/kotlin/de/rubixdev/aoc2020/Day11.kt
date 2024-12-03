package de.rubixdev.aoc2020

fun runDay11() {
    println("--- DAY 11 ---")

    val input = object {}.javaClass.getResource("/input11.txt")!!.readText().split('\n')

    part1(input)
    println()
    part2(input)
    println()
}

private fun part1(input: List<String>) {
    var curState = input
    var prevState = listOf<String>()
    while (curState != prevState) {
        prevState = curState
        curState = mutableListOf()
        for ((lineIndex, line) in prevState.withIndex()) {
            curState.add("")
            for ((seatIndex, seat) in line.withIndex()) {
                val adjacentSeats = mutableListOf<Char>()
                for (y in (lineIndex - 1)..(lineIndex + 1)) {
                    for (x in (seatIndex - 1)..(seatIndex + 1)) {
                        if (!(y == lineIndex && x == seatIndex) && y in 0..prevState.lastIndex && x in 0..prevState[y].lastIndex) {
                            adjacentSeats.add(prevState[y][x])
                        }
                    }
                }

                if (seat == '#' && adjacentSeats.count { it == '#' } >= 4) {
                    curState[lineIndex] += "L"
                } else if (seat == 'L' && adjacentSeats.all { it != '#' }) {
                    curState[lineIndex] += "#"
                } else {
                    curState[lineIndex] += seat.toString()
                }
            }
        }
    }
    println(prevState.reduce { acc, s -> acc + s }.count { it == '#' })
}

private fun part2(input: List<String>) {
    var curState = input
    var prevState = listOf<String>()
    while (curState != prevState) {
        prevState = curState
        curState = mutableListOf()

        for ((lineIndex, line) in prevState.withIndex()) {
            curState.add("")
            for ((seatIndex, seat) in line.withIndex()) {
                val influencingSeats = mutableListOf<Char>()
                for (yDir in -1..1) {
                    for (xDir in -1..1) {
                        if (xDir == 0 && yDir == 0) continue

                        var pos = lineIndex + yDir to seatIndex + xDir
                        while (pos.first in 0..prevState.lastIndex && pos.second in 0..prevState[pos.first].lastIndex) {
                            val curSeat = prevState[pos.first][pos.second]
                            if (curSeat in listOf('#', 'L')) {
                                influencingSeats.add(curSeat)
                                break
                            }

                            pos = pos.copy(pos.first + yDir, pos.second + xDir)
                        }
                    }
                }

                if (seat == '#' && influencingSeats.count { it == '#' } >= 5) {
                    curState[lineIndex] += "L"
                } else if (seat == 'L' && influencingSeats.all { it != '#' }) {
                    curState[lineIndex] += "#"
                } else {
                    curState[lineIndex] += seat.toString()
                }
            }
        }
    }
    println(prevState.reduce { acc, s -> acc + s }.count { it == '#' })
}
