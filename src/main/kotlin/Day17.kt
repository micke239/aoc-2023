import aocutil.Point
import java.io.File
import java.lang.Integer.max
import java.util.*
import kotlin.time.measureTime

class Day17 {
    fun run() {
        val lines = File("src/main/resources/day16.txt").readLines()
        var timeTaken = measureTime {
            part1(lines)
        }
        println(timeTaken)

        timeTaken = measureTime {
            part2(lines)
        }
        println(timeTaken)
    }

    private fun part1(lines: List<String>) {

    }

    private fun part2(lines: List<String>) {

    }

}