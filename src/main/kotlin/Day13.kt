import aocutil.Point
import java.io.File
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day13.txt").readLines()
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
    val maps = parse(lines)

    var above = 0L
    var left = 0L
    for(map in maps) {
        val maxY = map.maxOf { it.key.y }
        val maxX = map.maxOf { it.key.x }

        val mirror = findMirror(map, maxX, maxY, null)

        above += mirror!!.first
        left += mirror.second
    }
    println(left + (100 * above))
}

private fun part2(lines: List<String>) {
    val maps = parse(lines);

    var above = 0L
    var left = 0L
    mapIterator@for(map in maps) {
        val maxY = map.maxOf { it.key.y }
        val maxX = map.maxOf { it.key.x }

        val orig = findMirror(map, maxX, maxY, null)
        for (x in (0..maxX)) {
            for (y in (0..maxY)) {
                map[Point(x,y,0)] = when(map[Point(x,y,0)]) {
                    '#' -> '.'
                    else -> '#'
                }

                val newMirror = findMirror(map, maxX, maxY, orig)

                map[Point(x,y,0)] = when(map[Point(x,y,0)]) {
                    '#' -> '.'
                    else -> '#'
                }

                if (newMirror != null) {
                    above += newMirror.first
                    left += newMirror.second
                    continue@mapIterator;
                }

            }
        }
    }
    println(left + (100 * above))
}

private fun parse(lines: List<String>) : List<MutableMap<Point, Char>> {
    return lines.fold(0 to mutableListOf<MutableMap<Point,Char>>(mutableMapOf())){ (y,acc), line ->
        if (line.isBlank()) {
            acc.add(mutableMapOf())
            0 to acc
        } else {
            line.forEachIndexed { x, c ->
                acc.last()[Point(x.toLong(),y.toLong(),0)] = c
            }
            y+1 to acc
        }
    }.second
}

private fun findMirror(map: Map<Point, Char>, maxX: Long, maxY: Long, orig: Pair<Long,Long>?): Pair<Long,Long>? {
    for (y in (0..<maxY)) {
        val diff = min(y, maxY - 1 - y)
        //println(diff)
        val mirror1 = (0..diff).flatMap {y1 ->
            (0..maxX).map { x1 ->
                Point(x1,y1,0) to map[Point(x1,y+y1+1,0)]!!
            }
        }.toMap()
        val mirror2 = (0..diff).flatMap {y1 ->
            (0..maxX).map { x1 ->
                Point(x1,y1,0) to map[Point(x1,y-y1,0)]!!
            }
        }.toMap()

        if (mirror1 == mirror2) {
            val v = (y + 1) to 0L
            if (v != orig) {
                return v;
            }
        }
    }

    for (x in (0..<maxX)) {
        val diff = min(x, maxX - 1 - x)

        val mirror1 = (0..diff).flatMap {x1 ->
            (0..maxY).map { y1 ->
                Point(x1,y1,0) to map[Point(x+x1+1,y1,0)]!!
            }
        }.toMap()
        val mirror2 = (0..diff).flatMap {x1 ->
            (0..maxY).map { y1 ->
                Point(x1,y1,0) to map[Point(x-x1,y1,0)]!!
            }
        }.toMap()

        if (mirror1 == mirror2) {
            val v = 0L to (x + 1)
            if (v != orig) {
                return v;
            }
        }
    }

    return null
}
