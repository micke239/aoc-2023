import aocutil.Point
import java.io.File
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day18.txt").readLines()
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
    val instructions = lines.map {
        val s = it.split(" ")
        s[0] to s[1].toInt()
    }

    val area = handleInstructions(instructions)

    println(area)
}

private fun part2(lines: List<String>) {
    val instructions = lines.map {
        val s = it.split("(")
        val hex = s[1].replace("#","").replace(")","")
        val dir = when(hex.last()) {
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> throw NotImplementedError()
        }
        dir to hex.substring(0..4).toInt(16)
    }

    val area = handleInstructions(instructions)

    println(area)
}

private fun handleInstructions(instructions: List<Pair<String,Int>>): Long {
    val edges = instructions.fold(mutableMapOf<String,MutableList<Pair<Long,LongRange>>>(
        "R" to mutableListOf(),
        "L" to mutableListOf(),
        "U" to mutableListOf(),
        "D" to mutableListOf()
    ) to Point(0L,0L,0L)) { (acc, currPoint), (dir, length) ->
        //val range = 0..length // one to much
        //val start = acc.last()

        val range = when (dir) {
            "R" -> {
                val range = currPoint.x..(currPoint.x + length)
                val point = Point(range.last, currPoint.y, 0L)
                Triple(currPoint.y, range, point)
            }
            "L" -> {
                val range = (currPoint.x - length)..currPoint.x
                val point = Point(range.first, currPoint.y, 0L)
                Triple(currPoint.y, range, point)
            }
            "U" -> {
                val range = (currPoint.y - length)..currPoint.y
                val point = Point(currPoint.x, range.first, 0L)
                Triple(currPoint.x, range, point)
            }
            else -> {
                val range = currPoint.y..(currPoint.y + length)
                val point = Point(currPoint.x, range.last, 0L)
                Triple(currPoint.x, range, point)
            }
        }

        acc[dir]!!.add(range.first to range.second)

        acc to range.third
    }.first

    return countArea(edges)
}

fun countArea(edges: Map<String,List<Pair<Long,LongRange>>>): Long {
    val upSorted = edges["U"]!!.sortedByDescending { it.first }
    var sum = 0L
    for((x,yRange) in edges["D"]!!) {
        val startFromRight = edges["R"]!!.any {it.first == yRange.first && x in it.second}
        val endsFromRight = edges["R"]!!.any {it.first == yRange.last && x in it.second}
        for(y in yRange) {
            if (y == yRange.first && startFromRight) {
                continue
            }
            if (y == yRange.last && !endsFromRight) {
                continue
            }
            val closestLeftUp = upSorted.first { it.first < x && y in it.second }
            sum += x - closestLeftUp.first - 1
        }
    }

    val edgeCount = edges.entries.sumOf { edgeEntry ->
        edgeEntry.value.sumOf {
            it.second.last - it.second.first
        }
    }

    return sum + edgeCount
}
