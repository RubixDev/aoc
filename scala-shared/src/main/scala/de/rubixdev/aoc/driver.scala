package de.rubixdev.aoc

import io.github.cdimascio.dotenv.Dotenv

import java.io.File
import java.net.URI
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import scala.annotation.tailrec
import scala.collection.immutable.VectorMap
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.chaining.*

type Days = Map[String => Unit, Int]

private def measureTime[R](block: => R): (R, FiniteDuration) = {
  val start = System.nanoTime()
  val result = block
  val end = System.nanoTime()
  (result, Duration(end - start, TimeUnit.NANOSECONDS))
}

def runDays(days: Days, year: Int): Unit = {
  val total = measureTime {
    for ((day, n) <- days) {
      val total = measureTime {
        val (input, timeInput) = measureTime { getInput(year, n) }
        println(s"--- Day $n ---")
        day(input)
        println(s"\u001b[90mReading Input ${timeInput.pretty}\u001b[0m")
      }
      println(s"\u001b[90mTotal         ${total._2.pretty}\u001b[0m\n")
    }
  }
  println(s"\u001b[90mTotal Execution Time: ${total._2.pretty}\u001b[0m")
}

def getInput(year: Int, day: Int): String = {
  val file = File(s"inputs/$year/day$day.txt")
  Files.createDirectories(file.toPath.getParent)
  if (file.exists) {
    return Files.readString(file.toPath)
  }

  val sessionToken = Dotenv.load.get("AOC_SESSION")
  val response = HttpClient.newHttpClient.send(
    HttpRequest
      .newBuilder(URI.create(s"https://adventofcode.com/$year/day/$day/input"))
      .header("Cookie", s"session=$sessionToken")
      .build(),
    BodyHandlers.ofString(),
  )
  response.body.tap(Files.writeString(file.toPath, _))
}

private val timeUnitLabels = VectorMap(
  TimeUnit.DAYS -> "d",
  TimeUnit.HOURS -> "h",
  TimeUnit.MINUTES -> "m",
  TimeUnit.SECONDS -> "s",
  TimeUnit.MILLISECONDS -> "ms",
  TimeUnit.MICROSECONDS -> "µs",
  TimeUnit.NANOSECONDS -> "ns",
)

extension (duration: FiniteDuration) {
  def toShortString: String = duration.length.toString + {
    timeUnitLabels(duration.unit)
  }

  def pretty: String = {
    @tailrec
    def formatDurationRec(
        acc: List[FiniteDuration],
        remUnits: List[TimeUnit],
        rem: FiniteDuration,
    ): String =
      remUnits match {
        case head :: tail =>
          if (rem > Duration(1, head)) {
            val x = Duration(rem.toUnit(head).toLong, head)
            formatDurationRec(x :: acc, tail, rem - x)
          } else {
            formatDurationRec(acc, tail, rem)
          }
        case Nil => acc.reverse.map(_.toShortString).mkString(" ")
      }

    formatDurationRec(Nil, timeUnitLabels.keys.toList, duration)
  }
}
