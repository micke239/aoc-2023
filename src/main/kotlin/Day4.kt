import java.io.File
import kotlin.math.pow

class Day4 {
    fun run() {
        val lines = File("src/main/resources/day4.txt").readLines()
        part1(lines)
        part2(lines)
    }

    private fun part1(lines: List<String>) {
        val cards = parseRounds(lines)
        println(cards.sumOf { 2.0.pow(it.winCount-1).toInt() })
    }

    private fun part2(lines: List<String>) {
        val rounds = parseRounds(lines)

        val sum = rounds.fold(mutableMapOf<Int,Int>()) { acc, round ->
            acc[round.index] = (acc[round.index]?:0) + 1

            (1..round.winCount).forEach{ idx ->
                acc[round.index + idx] = (acc[round.index + idx]?:0) + (acc[round.index]?:0)
            }

            acc
        }.values.sum()

        println(sum)
    }

    private fun parseRounds(lines: List<String>): List<Round> {
        return lines.mapIndexed { index, line  ->
            val cards = line.split(":")[1]
            val split = cards.split('|')
            val winning = split[0].trim().split(" ").filter{it.isNotBlank()}.map { it.toInt() }.toSet()
            val mine = split[1].trim().split(" ").filter{it.isNotBlank()}.map { it.toInt() }.toList()

            Round(index + 1, winning, mine, mine.count{number -> number in winning})
        }
    }
}

data class Round(val index: Int, val winning: Set<Int>, val mine: List<Int>, val winCount: Int)