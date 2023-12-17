import java.io.File
import java.math.BigInteger
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day9.txt").readLines()
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
    val rows = lines.map { it.split(" ").map { n -> n.toLong() }}

    val sum = rows.map {
        val history = mutableListOf(it)
        while(history.last().any { n -> n != 0L }) {
            val last = history.last()
            val nl = (1..<last.count()).map { i -> last[i] - last[i-1] }
            history.add(nl)
        }
        history.toList()
    }.sumOf {
        it.sumOf { l -> l.last() }
    }

    println(sum)
}

private fun part2(lines: List<String>) {
    val rows = lines.map { it.split(" ").map { n -> n.toLong() }}

    val sum = rows.map {
        val history = mutableListOf(it)
        while(history.last().any { n -> n != 0L }) {
            val last = history.last()
            val nl = (1..<last.count()).map { i -> last[i] - last[i-1] }
            history.add(nl)
        }
        history.toList()
    }.sumOf {
        val nm = it.reversed().map { i -> i.toMutableList() }.toMutableList()
        nm[0].addFirst(0)
        nm.forEachIndexed { idx, l ->
            if (idx == (nm.count() -1)) return@forEachIndexed

            nm[idx+1].addFirst(nm[idx+1][0] - l[0])
        }
        nm.last().first()
    }

    println(sum)
}