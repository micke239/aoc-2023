import java.io.File
import java.util.*
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day17.txt").readLines()
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
    val map = lines.flatMapIndexed {y,l ->
        l.mapIndexed { x,c ->
            IntPoint(x,y) to c.digitToInt()
        }
    }.toMap()

    val path = findBestPath(map, null) { thing, currentDir ->
        if (currentDir.second == 3) {
            thing.dir.first != currentDir.first
        }
        else true
    }

    println(path!!.weight)
}

private fun part2(lines: List<String>) {
    val map = lines.flatMapIndexed {y,l ->
        l.mapIndexed { x,c ->
            IntPoint(x,y) to c.digitToInt()
        }
    }.toMap()

    val path = findBestPath(map, 4) { thing, currentDir ->
            if (currentDir.second < 4) thing.dir.first == currentDir.first
            else if (currentDir.second == 10) thing.dir.first != currentDir.first
            else true
        }

    println(path!!.weight)
}

private fun findBestPath(
    map: Map<IntPoint, Int>,
    minSteps: Int?,
    dirFilter: (Thing, Pair<Char,Int>) -> Boolean
) : WeightedPath?
{
    val yRange = (0..(map.maxOf { it.key.y }))
    val xRange = (0..(map.maxOf { it.key.x }))

    var cheapestEnd: WeightedPath? = null
    val cheapest = mutableMapOf<Pair<IntPoint,Pair<Char,Int>>, Int>()
    val queue = PriorityQueue (
        compareByDescending<Thing> { ((xRange.last - it.point.x) + (yRange.last - it.point.y)) / 2 }
            .thenBy { map[it.point] }
    )
    queue.add(Thing(emptySet(), 0, IntPoint(1,0), 'e' to 1))
    queue.add(Thing(emptySet(), 0, IntPoint(0,1), 's' to 1))
    while(queue.any())
    {

        val (prevPath, prevWeight, newNode, dir) = queue.poll()
        val weight = prevWeight + map[newNode]!!

        val cheapestNode = cheapest[newNode to dir]
        if (cheapestNode != null && cheapestNode <= weight) {
            continue
        }
        cheapest[newNode to dir] = weight

        if (cheapestEnd != null && weight >= cheapestEnd.weight)
        {
            continue
        }

        val newPath = prevPath.toMutableSet()
        newPath.add(newNode)

        val wp = WeightedPath(newPath, weight)
        if (newNode.x == xRange.last && newNode.y == yRange.last)
        {
            if (minSteps == null || dir.second >= minSteps) {
                cheapestEnd = wp
            }
            continue
        }

        getNeighbours(newNode)
            .filter {it.x in xRange && it.y in yRange}
            .filter {!prevPath.contains(it)}
            .map{
                val d = if (it.y > newNode.y) 's'
                    else if (it.y < newNode.y) 'n'
                    else if (it.x < newNode.x) 'w'
                    else 'e'
                val dCount = if (d == dir.first) dir.second + 1 else 1
                Thing(newPath,weight,it, d to dCount)
            }
            .filter { dirFilter(it, dir)}
            .forEach{queue.add(it)}
    }

    return cheapestEnd
}

private fun getNeighbours(point: IntPoint): List<IntPoint> {
    val neighbours = mutableListOf<IntPoint>()
    neighbours.add(IntPoint(point.x, point.y - 1)) // N
    neighbours.add(IntPoint(point.x, point.y + 1)) // S
    neighbours.add(IntPoint(point.x - 1, point.y)) // W
    neighbours.add(IntPoint(point.x + 1, point.y)) // E
    return neighbours
}

data class Thing(val path: Set<IntPoint>, val weight: Int, val point: IntPoint, val dir: Pair<Char,Int> )
data class IntPoint(val x: Int, val y: Int)
data class WeightedPath(val path: Set<IntPoint>, val weight: Int)