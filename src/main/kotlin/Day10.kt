import aocutil.Point
import aocutil.findAnyPath
import java.io.File
import kotlin.math.min
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
        var start = Point(0,0,0);
        val map = lines.flatMapIndexed{y, l ->
            l.mapIndexed {x,c ->
                val point = Point(x.toLong(), y.toLong(),0)
                if (c == 'S') start = point;
                when (c) {
                    '|' -> point to listOf(Point(x.toLong(), y.toLong() - 1, 0), Point(x.toLong(), y.toLong() + 1, 0))
                    '-' -> point to listOf(Point(x.toLong() - 1, y.toLong(), 0), Point(x.toLong() + 1, y.toLong(), 0))
                    'L' -> point to listOf(Point(x.toLong(), y.toLong() - 1, 0), Point(x.toLong() + 1, y.toLong(), 0))
                    'J' -> point to listOf(Point(x.toLong(), y.toLong() - 1, 0), Point(x.toLong() - 1, y.toLong(), 0))
                    '7','S' -> point to listOf(Point(x.toLong() - 1, y.toLong(), 0), Point(x.toLong(), y.toLong() + 1, 0))
                    'F'/*,'S'*/ -> point to listOf(Point(x.toLong() + 1, y.toLong(), 0), Point(x.toLong(), y.toLong() + 1, 0))
                    else -> null
                }
                //Point(x.toLong(),y.toLong(),0) to c
            }.filterNotNull()
        }.toMap()

        var prev = start;
        var node = map[start]!![0]
        var i = 1
        while (node != start) {
            if (map[node] == null)
                println("Whaat")
            val nnode = map[node]!!.first { it != prev }
            prev = node;
            node = nnode;
            i++
        }
        println(i.toDouble()/2)
    }

    private fun part2(lines: List<String>) {
        var start = Point(0,0,0);

        val expanded = lines.flatMapIndexed {y, line ->
            val nY = y*2

            line.flatMapIndexed {x, c ->
                val nX = x*2
                listOf(
                    Point(nX.toLong(),nY.toLong(),0) to c,
                    Point(nX.toLong()+1,nY.toLong(),0) to '-',
                    Point(nX.toLong(),nY.toLong()+1,0) to '|',
                    Point(nX.toLong()+1,nY.toLong()+1,0) to 'M')
            }
        }.toMap()

        val map = expanded.map {
            val point = it.key
            val (x,y) = point
            val c = it.value
            if (c == 'S') start = point;
            when (c) {
                '|' -> point to listOf(Point(x, y - 1, 0), Point(x, y + 1, 0))
                '-' -> point to listOf(Point(x - 1, y, 0), Point(x + 1, y, 0))
                'L' -> point to listOf(Point(x, y - 1, 0), Point(x + 1, y, 0))
                'J' -> point to listOf(Point(x, y - 1, 0), Point(x - 1, y, 0))
                '7','S' -> point to listOf(Point(x - 1, y, 0), Point(x, y + 1, 0))
                'F'/*,'S'*/ -> point to listOf(Point(x + 1, y, 0), Point(x, y + 1, 0))
                else -> null
            }
        }.filterNotNull().toMap()

        var prev = start;
        var node = map[start]!![0]
        val loop = mutableSetOf(start)
        while (node != start) {
            loop.add(node)
            val newNode = map[node]!!.first { it != prev }
            prev = node;
            node = newNode;
        }

//        printMapWithLoop(expanded, loop)

        val minY = loop.minOf { it.y }
        val maxY = loop.maxOf { it.y }
        val loopMinX = loop.minOf { it.x }
        val loopMaxX = loop.maxOf { it.x }
        val enclosed = mutableSetOf<Point>()
        for (y in ((minY)+1..<maxY)) {
            if (y % 2 == 1L) continue

            var minX = loop.filter { it.y == y }.minOf { it.x }
            var maxX = loop.filter { it.y == y }.maxOf { it.x }
            for(x in ((minX+1)..<maxX)) {
                if (x % 2 == 1L) continue

                val point = Point(x,y,0);
                if (loop.contains(point)) {
                    continue
                }

                val p = findAnyPath(
                    Point(x,y,0),
                    { it.x < loopMinX || it.x > loopMaxX || it.y < minY || it.y > maxY || enclosed.contains(it)  },
                    { _,p2 -> loop.contains(p2) },
                    compareBy { (min(it.x - loopMinX, loopMaxX - it.x) + min(it.y - minY, maxY - it.y)) / 2 })

                if (p == null || enclosed.contains(p.last())) {
                    enclosed.add(point)
                }
            }
        }

//        printMapWithLoopAndEnclosed(expanded, loop, enclosed)

        println(enclosed.count())
    }

    private fun printMapWithLoopAndEnclosed(expanded: Map<Point, Char>, loop: MutableSet<Point>, enclosed: MutableSet<Point>) {
        val expandedX = expanded.keys.maxOf { it.x }
        val expandedY = expanded.keys.maxOf { it.y }
        (0..expandedY).forEach{y ->
            if (y % 2 == 1L) return@forEach

            (0..expandedX).forEach(fun(x: Long) {
                if (x % 2 == 1L) return

                val point = Point(x,y,0)
                if (loop.contains(point)) {
                    print("\u001b[31m" + expanded[point] + "\u001b[0m")
                } else if (enclosed.contains(point)) {
                    print("\u001b[92m" + expanded[point] + "\u001b[0m")
                } else {
                    print(expanded[point])
                }
            })
            println("")
        }
    }

    private fun printMapWithLoop(expanded: Map<Point, Char>, loop: MutableSet<Point>) {
        val expandedX = expanded.keys.maxOf { it.x }
        val expandedY = expanded.keys.maxOf { it.y }
        (0..expandedY).forEach{y ->
            if (y % 2 == 1L) return@forEach

            (0..expandedX).forEach(fun(x: Long){
                if (y % 2 == 1L) return

                val point = Point(x,y,0)
                if (loop.contains(point)) {
                    print("\u001b[31m" + expanded[point] + "\u001b[0m")
                } else {
                    print(expanded[point])
                }
            })
            println("")
        }
    }

}