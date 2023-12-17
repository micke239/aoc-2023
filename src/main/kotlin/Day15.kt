import java.io.File
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day15.txt").readLines()
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
    val hello = lines.first().split(",")
    val sum = hello.sumOf(::hashIt)
    println(sum)
}

private fun part2(lines: List<String>) {
    val hello = lines.first().split(",")
    val sum = hello.fold((0..256).map { mutableListOf<Pair<String, Int>>() }.toList()) { acc, it ->

        val (str, operation, extra) = if (it.contains("=")) {
            val split = it.split("=")
            Triple(split[0], '=', split[1].toInt())
        }
        else {
            val split = it.split("-")
            Triple(split[0], '-', null)
        }

        val hash = hashIt(str)
        val idx = acc[hash].indexOfFirst{x -> x.first == str}

        if (operation == '=') {
            if (idx == -1) {
                acc[hash].add(str to extra!!)
            } else {
                acc[hash][idx] = str to extra!!
            }
        }
        else if (idx != -1){
            acc[hash].removeAt(idx)
        }

        acc
    }.mapIndexed {i, list ->
        list.mapIndexed { j, (_, l) ->
            (i + 1) * (j + 1) * l
        }.sum()
    }.sum()

    println(sum)
}

private fun hashIt(str: String): Int {
    return str.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
}
