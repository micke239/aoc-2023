import aocutil.Point
import java.io.File
import java.lang.Integer.max
import java.util.*
import kotlin.time.measureTime

class Day16 {
    fun run() {
        val lines = File("src/main/resources/day16.txt").readLines()
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
        val obstacles = lines.flatMapIndexed { y, l ->
            l.mapIndexed { x, c ->
                if (c == '.') {
                    null
                } else {
                    Point(x.toLong(), y.toLong(), 0) to c
                }
            }.filterNotNull()
        }.toMap()

        val maxX = lines[0].length - 1
        val maxY = lines.size - 1

        val energized = beamIt(Point(-1,0,0) to 'E', obstacles, maxX, maxY)

        println(energized.size - 1)
    }

    private fun part2(lines: List<String>) {
        val obstacles = lines.flatMapIndexed { y, l ->
            l.mapIndexed { x, c ->
                if (c == '.') {
                    null
                } else {
                    Point(x.toLong(), y.toLong(), 0) to c
                }
            }.filterNotNull()
        }.toMap()

        val maxX = lines[0].length - 1
        val maxY = lines.size - 1

        val maxFromY = (0..maxY).flatMap {
            listOf(
                beamIt(Point(-1,it.toLong(),0) to 'E', obstacles, maxX, maxY).size,
                beamIt(Point(maxX+1.toLong(),it.toLong(),0) to 'W', obstacles, maxX, maxY).size
            )
        }.max()

        val maxFromX = (0..maxX).flatMap {
            listOf(
                beamIt(Point(it.toLong(),-1,0) to 'S', obstacles, maxX, maxY).size,
                beamIt(Point(it.toLong(),maxY+1.toLong(),0) to 'N', obstacles, maxX, maxY).size
            )
        }.max()

        println(max(maxFromX, maxFromY) - 1)
    }

    private fun beamIt(start: Pair<Point,Char>, obstacles: Map<Point, Char>, maxX: Int, maxY: Int) : Set<Point> {
        val beams = LinkedList<Pair<Point,Char>>()
        val energized = mutableSetOf<Point>()
        val visited = mutableSetOf<Pair<Point,Char>>()
        beams.add(start)
        while(beams.any()) {
            val beam = beams.pop()

            if (visited.contains(beam)) {
                continue;
            }

            visited.add(beam)
            energized.add(beam.first)
            val newBeams = when(beam.second) {
                'E' -> {
                    val nextPoint = Point(beam.first.x + 1, beam.first.y, 0)
                    val obstacle = obstacles[nextPoint]
                    when(obstacle) {
                        '/' -> listOf(nextPoint to 'N')
                        '\\' -> listOf(nextPoint to 'S')
                        '|' -> listOf(nextPoint to 'N', nextPoint to 'S')
                        else -> listOf(nextPoint to 'E')
                    }
                }
                'W' -> {
                    val nextPoint = Point(beam.first.x - 1, beam.first.y, 0)
                    val obstacle = obstacles[nextPoint]
                    when(obstacle) {
                        '/' -> listOf(nextPoint to 'S')
                        '\\' -> listOf(nextPoint to 'N')
                        '|' -> listOf(nextPoint to 'N', nextPoint to 'S')
                        else -> listOf(nextPoint to 'W')
                    }
                }
                'N' -> {
                    val nextPoint = Point(beam.first.x, beam.first.y - 1, 0)
                    val obstacle = obstacles[nextPoint]
                    when(obstacle) {
                        '/' -> listOf(nextPoint to 'E')
                        '\\' -> listOf(nextPoint to 'W')
                        '-' -> listOf(nextPoint to 'W', nextPoint to 'E')
                        else -> listOf(nextPoint to 'N')
                    }
                }
                else -> {
                    val nextPoint = Point(beam.first.x, beam.first.y + 1, 0)
                    val obstacle = obstacles[nextPoint]
                    when(obstacle) {
                        '/' -> listOf(nextPoint to 'W')
                        '\\' -> listOf(nextPoint to 'E')
                        '-' -> listOf(nextPoint to 'W', nextPoint to 'E')
                        else -> listOf(nextPoint to 'S')
                    }
                }
            }

            beams.addAll(newBeams.filter { it.first.x in 0..maxX && it.first.y in 0..maxY })
        }

        return energized
    }
}