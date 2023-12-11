import aocutil.Point
import java.io.File
import kotlin.math.abs
import kotlin.time.measureTime

class Day11 {
    fun run() {
        val lines = File("src/main/resources/day11.txt").readLines()
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
        var galaxies = lines.flatMapIndexed{y,line ->
            line.mapIndexed{x,c ->
                if (c == '#')
                    Point(x.toLong(), y.toLong(), 0)
                else
                    null
            }.filterNotNull()
        }

        galaxies = expandGalaxies(galaxies, 1)

        val sum = (0..<galaxies.count()).flatMap { i ->
            val galaxy1 = galaxies[i]
            ((i+1)..<galaxies.count()).map { j ->
                val galaxy2 = galaxies[j]
                abs(galaxy2.x - galaxy1.x) + abs(galaxy2.y - galaxy1.y)
            }
        }.sum()
        println(sum)
    }

    private fun part2(lines: List<String>) {
        var galaxies = lines.flatMapIndexed{y,line ->
            line.mapIndexed{x,c ->
                if (c == '#')
                    Point(x.toLong(), y.toLong(), 0)
                else
                    null
            }.filterNotNull()
        }

        galaxies = expandGalaxies(galaxies, 999999)

        val sum = (0..<galaxies.count()).flatMap { i ->
            val galaxy1 = galaxies[i]
            ((i+1)..<galaxies.count()).map { j ->
                val galaxy2 = galaxies[j]
                abs(galaxy2.x - galaxy1.x) + abs(galaxy2.y - galaxy1.y)
            }
        }.sum()
        println(sum)
    }

    private fun expandGalaxies(galaxies: List<Point>, i: Int): List<Point> {
        val expandedGalaxies = mutableMapOf<Point,Point>()

        for (x in (0..(galaxies.maxOf { it.x }))) {
            if (!galaxies.any {it.x == x}) {
                galaxies.filter{g -> g.x > x}.forEach {g ->
                    if (expandedGalaxies.containsKey(g)) {
                        expandedGalaxies[g] = Point(expandedGalaxies[g]!!.x + i, expandedGalaxies[g]!!.y, 0)
                    }
                    else {
                        expandedGalaxies[g] = Point(g.x + i, g.y, 0)
                    }
                }
            }
        }

        for (y in (0..(galaxies.maxOf { it.y }))) {
            if (!galaxies.any {it.y == y}) {
                galaxies.filter{g -> g.y > y}.forEach {g ->
                    if (expandedGalaxies.containsKey(g)) {
                        expandedGalaxies[g] = Point(expandedGalaxies[g]!!.x, expandedGalaxies[g]!!.y + i, 0)
                    }
                    else {
                        expandedGalaxies[g] = Point(g.x, g.y + i, 0)
                    }
                }
            }
        }

        return galaxies.map { expandedGalaxies[it] ?: it }
    }
}