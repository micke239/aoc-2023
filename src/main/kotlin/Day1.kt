import java.io.File

private val digits = hashMapOf(
    "1" to 1,
    "one" to 1,
    "2" to 2,
    "two" to 2,
    "3" to 3,
    "three" to 3,
    "4" to 4,
    "four" to 4,
    "5" to 5,
    "five" to 5,
    "6" to 6,
    "six" to 6,
    "7" to 7,
    "seven" to 7,
    "8" to 8,
    "eight" to 8,
    "9" to 9,
    "nine" to 9
)

fun main() {
    val lines = File("src/main/resources/day1.txt").readLines()
    part1(lines)
    part2(lines)
}

private fun part1(lines: List<String>) {
    val sum = lines
        .map {
            it.filter(Char::isDigit)
        }.sumOf { "${it[0]}${it[it.lastIndex]}".toInt() }

    println(sum)
}

private fun part2(lines: List<String>) {
    val sum = lines
        .map { line ->
            digits.map {
                Digit(it.value, line.indexOf(it.key), line.lastIndexOf(it.key) )
            }.filter { it.lastPosition != -1 && it.firstPosition != -1 }
        }.sumOf {
            val first = it.minBy { it2 -> it2.firstPosition }.value
            val last = it.maxBy { it2 -> it2.lastPosition }.value
            ("$first$last").toInt()
        }

    println(sum)
}

data class Digit(val value: Int, val firstPosition: Int, val lastPosition: Int)