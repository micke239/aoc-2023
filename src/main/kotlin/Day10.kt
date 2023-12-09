import java.io.File
import java.math.BigInteger
import kotlin.time.measureTime

class Day10 {
    fun run() {
        val lines = File("src/main/resources/day10.txt").readLines()
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