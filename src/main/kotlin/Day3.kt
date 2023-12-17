import aocutil.Point
import aocutil.getNeighbours
import java.io.File

fun main() {
    val lines = File("src/main/resources/day3.txt").readLines()
    part1(lines)
    part2(lines)
}

private fun part1(lines: List<String>) {
    val engine = lines.flatMapIndexed{ i,s ->
        s.mapIndexed { j,c -> Point(j.toLong(),i.toLong(),0) to c }
    }.toMap()

    val poop = mutableSetOf<EnginePart>()
    for (y in 0L..<lines.count()) {
        var strDigit = ""
        for (x in 0L..<lines[y.toInt()].length) {
            val point = Point(x,y,0)
            if (engine[point]?.isDigit() == true) {
                strDigit += engine[Point(x,y,0)]
            }
            else if (strDigit.isNotEmpty()) {
                poop.add(EnginePart(strDigit, Point(x-strDigit.length,y,0),Point(x-1,y,0)))
                strDigit = ""
            }
        }

        if (strDigit.isNotEmpty()) {
            val x = lines[y.toInt()].length.toLong()
            poop.add(EnginePart(strDigit, Point(x-strDigit.length,y,0),Point(x-1,y,0)))
        }
    }

    val poop2 = poop.filter { part ->
        var n = false

        for(x in part.start.x..part.end.x) {
            val b = getNeighbours(Point(x,part.start.y,0),true).any{
                engine[it]?.isDigit() == false && engine[it] != '.'
            }

            if (b) {
                n = true
                break
            }
        }

        n
    }.sumOf { it.str.toInt() }

    println(poop2)
}

private fun part2(lines: List<String>) {
    val engine = lines.flatMapIndexed{ i,s ->
        s.mapIndexed { j,c -> Point(j.toLong(),i.toLong(),0) to c }
    }.toMap()

    val poop = mutableSetOf<EnginePart>()
    for (y in 0L..<lines.count()) {
        var strDigit = ""
        for (x in 0L..<lines[y.toInt()].length) {
            val point = Point(x,y,0)
            if (engine[point]?.isDigit() == true) {
                strDigit += engine[Point(x,y,0)]
            }
            else if (strDigit.isNotEmpty()) {
                poop.add(EnginePart(strDigit, Point(x-strDigit.length,y,0),Point(x-1,y,0)))
                strDigit = ""
            }
        }

        if (strDigit.isNotEmpty()) {
            val x = lines[y.toInt()].length.toLong()
            poop.add(EnginePart(strDigit, Point(x-strDigit.length,y,0),Point(x-1,y,0)))
        }
    }

    val poop2 = poop.asSequence().map { part ->
        var n: Pair<Point, EnginePart>? = null

        for(x in part.start.x..part.end.x) {
            val b = getNeighbours(Point(x, part.start.y, 0),true).firstOrNull{
                engine[it] == '*'
            }

            if (b != null) {
                n = b to part
                break
            }
        }
        n
    }
        .filterNotNull()
        .groupBy {it.first}
        .filter { it.value.count() > 1 }
        .map { it.value.fold(1L) { acc, p -> acc * p.second.str.toLong() } }
        .sum()

    println(poop2)
}

data class EnginePart(val str: String, val start: Point, val end: Point)