import java.io.File
import kotlin.time.measureTime

class Day6 {
    fun run() {
        val lines = File("src/main/resources/day6.txt").readLines()
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
        var input = mapOf(
            7 to 9,
            15 to 40,
            30 to 200
        )

        input = mapOf(
            46 to 214,
            80 to 1177,
            78 to 1402,
            66 to 1024
        )

        val res = input.entries.map {
            val duration = it.key
            val record = it.value

            (1..<duration).map {i ->
                (i * (duration - i))
            }.count { i -> i > record }
        }.fold(1L) { acc, i -> acc * i }

        println(res)
    }

    private fun part2(lines: List<String>) {
        val input = mapOf(
            46807866L to 214117714021024L,
        )

        val res = input.entries.map {
            val duration = it.key
            val record = it.value

            (1..<duration).map {i ->
                (i * (duration - i))
            }.count { i -> i > record }
        }.fold(1L) { acc, i -> acc * i }

        println(res)
    }
}
