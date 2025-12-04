package de.rubixdev.aoc

import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.io.path.createParentDirectories
import kotlin.time.measureTime

typealias Day = Sequence<Any>
typealias Days = Map<(String) -> Day, Int>

fun runDays(days: Days, year: Int) {
    val total =
        measureTime {
            for ((day, n) in days.entries) {
                println("--- Day $n ---")
                val total =
                    measureTime {
                        val input: String
                        val timeInput = measureTime { input = getInput(year, n) }
                        val iter = day(input).iterator()
                        val timeParse = measureTime { iter.next() }
                        val timePart1 = measureTime { println("Part 1: ${iter.next()}") }
                        val timePart2 = measureTime { println("Part 2: ${iter.next()}") }
                        println("\u001b[90mReading Input $timeInput\u001b[0m")
                        println("\u001b[90mParsing       $timeParse\u001b[0m")
                        println("\u001b[90mPart 1        $timePart1\u001b[0m")
                        println("\u001b[90mPart 2        $timePart2\u001b[0m")
                    }
                println("\u001b[90mTotal         $total\u001b[0m\n")
            }
        }
    println("\u001b[90mTotal Execution Time: $total\u001b[0m")
}

fun getInput(year: Int, day: Int, filename: String = "day"): String {
    val file = File("inputs/$year/$filename$day.txt")
    file.toPath().createParentDirectories()
    if (file.exists()) {
        return file.readText().trim()
    }

    val sessionToken = dotenv()["AOC_SESSION"]!!
    val response = HttpClient.newHttpClient().send(
        HttpRequest.newBuilder(URI.create("https://adventofcode.com/$year/day/$day/input"))
            .header("Cookie", "session=$sessionToken")
            .build(),
        BodyHandlers.ofString(),
    )
    return response.body().trim().also { file.writeText(it) }
}
