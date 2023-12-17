import aocutil.Point
import aocutil.findAnyPath
import java.io.File
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day14.txt").readLines()
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
    val map = lines.flatMapIndexed { y,l ->
        l.mapIndexed{x,c->
            Point(x.toLong(), y.toLong(), 0) to c
        }
    }.toMap().toMutableMap()

    val maxX = map.maxOf { it.key.x }
    val maxY = map.maxOf { it.key.y }

    tilt(map, Point(0,-1,0), maxX, maxY)

    val sum = map.entries.filter { it.value == 'O' }.sumOf {
        maxY - it.key.y + 1
    }

    println(sum)
}

private fun part2(lines: List<String>) {
    var map = lines.flatMapIndexed { y,l ->
        l.mapIndexed{x,c->
            Point(x.toLong(), y.toLong(), 0) to c
        }
    }.toMap().toMutableMap()

    val maxX = map.maxOf { it.key.x }
    val maxY = map.maxOf { it.key.y }
    val rounds = mutableSetOf<Map<Point,Char>>()
    val roundsToIterate = 1000000000
    for (i in (0..<roundsToIterate)) {
        tilt(map, Point(0,-1,0), maxX, maxY)
        tilt(map, Point(-1,0,0), maxX, maxY)
        tilt(map, Point(0,1,0), maxX, maxY)
        tilt(map, Point(1,0,0), maxX, maxY)

        val earlier = rounds.indexOf(map);
        if (rounds.indexOf(map) != -1) {
            val diff = i - earlier
            var j = i
            while(j < roundsToIterate) {j+=diff}
            val remainder = j - roundsToIterate + 1
            map = rounds.elementAt(i - remainder).toMutableMap()
            break
        }
        rounds.add(map.toMap())
    }

    val sum = map.entries.filter { it.value == 'O' }.sumOf {
        maxY - it.key.y + 1
    }

    println(sum)
}

private fun tilt(map: MutableMap<Point,Char>, by: Point, maxX: Long, maxY: Long) {
    do {
        var hasMoved = false
        for(y in (0..maxY)) {
            for(x in (0..maxX)) {
                val p1 = Point(x,y,0)
                val p2 = Point(x+by.x, y+by.y,0);
                if (p2.y < 0 || p2.y > maxY || p2.x < 0 || p2.x > maxX) {
                    continue
                }

                if (map[p1] == 'O' && map[p2] == '.') {
                    map[p1] = '.'
                    map[p2] = 'O'
                    hasMoved = true
                }
            }
        }
    } while (hasMoved)
}
