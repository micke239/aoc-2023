package aocutil

import java.util.*
import kotlin.Comparator

fun findBestPath(
    from: Point,
    isEnd: (Point) -> Boolean,
    getWeight: (Point) -> Long,
    isBlocked: (Point, Point) -> Boolean,
    priority: Comparator<Point>,
    multiEnd: Boolean,
    allowDiagonal: Boolean = false,
    is3d: Boolean = false
    ) : WeightedPath?
{
    var cheapestEnd: WeightedPath? = null
    val cheapest = mutableMapOf<Point, WeightedPath>()
    val queue = PriorityQueue<Triple<Set<Point>, Long, Point>> {p1,p2 -> priority.compare(p1.third, p2.third) }
//    val queue = LinkedList<Triple<Set<Point>, Long, Point>>() //{p1,p2 -> priority.compare(p1.third, p2.third) }
    queue.add(Triple(emptySet(), -getWeight(from), from))
    while(queue.any())
    {
        val (prevPath, prevWeight, newNode) = queue.poll()
        val weight = prevWeight + getWeight(newNode)

        if ((cheapest[newNode]?.weight ?: Long.MAX_VALUE) < weight)
        {
            continue
        }

        if (multiEnd && cheapestEnd != null && weight >= cheapestEnd.weight)
        {
            continue
        }

        val newPath = prevPath.toMutableSet()
        newPath.add(newNode)
        val wp = WeightedPath(newPath, weight)

        //println(newPath)
        cheapest[newNode] = wp

        if (isEnd(newNode))
        {
            if (multiEnd) {
                cheapestEnd = wp
                println(cheapestEnd.weight)
                continue
            }
            return wp
        }

        getNeighbours(newNode, allowDiagonal, is3d)
            .filter {!isBlocked(newNode, it)}
            .filter {!prevPath.contains(it)}
            .map{Triple(newPath,weight,it)}
            .forEach{queue.add(it)}
    }

    return cheapestEnd
}

fun findAnyPath(
    from: Point,
    isEnd: (Point) -> Boolean,
    isBlocked: (Point, Point) -> Boolean,
    priority: Comparator<Point>,
    allowDiagonal: Boolean = false,
    is3d: Boolean = false
) : Set<Point>?
{
    val queue = PriorityQueue<Pair<Set<Point>, Point>> {p1,p2 -> priority.compare(p1.second, p2.second) }
    queue.add(Pair(emptySet(), from))
    val visited = mutableSetOf<Point>()
    while(queue.any())
    {
        val (prevPath, newNode) = queue.poll()

        visited.add(newNode)

        val newPath = prevPath.toMutableSet()
        newPath.add(newNode)

        if (isEnd(newNode))
        {
            return newPath
        }

        getNeighbours(newNode, allowDiagonal, is3d)
            .filter { !visited.contains(it) }
            .filter {!isBlocked(newNode, it)}
            .map{Pair(newPath,it)}
            .forEach{queue.add(it)}
    }

    return null
}

fun getNeighbours(point: Point, allowDiagonal: Boolean = false, is3d: Boolean = false): Set<Point>
{
    val neighbours = mutableSetOf<Point>()
    neighbours.add(Point(point.x, point.y - 1, point.z)) // N
    neighbours.add(Point(point.x, point.y + 1, point.z)) // S
    neighbours.add(Point(point.x - 1, point.y, point.z)) // W
    neighbours.add(Point(point.x + 1, point.y, point.z)) // E

    if (allowDiagonal)
    {
        neighbours.add(Point(point.x - 1, point.y - 1, point.z)) // NW
        neighbours.add(Point(point.x - 1, point.y + 1, point.z)) // SW
        neighbours.add(Point(point.x + 1, point.y - 1, point.z)) // NE
        neighbours.add(Point(point.x + 1, point.y + 1, point.z)) // SE
    }

    if (is3d)
    {
        neighbours.add(Point(point.x, point.y, point.z - 1))
        neighbours.add(Point(point.x, point.y, point.z + 1))

        if (allowDiagonal)
        {
            neighbours.add(Point(point.x - 1, point.y - 1, point.z - 1))
            neighbours.add(Point(point.x - 1, point.y + 1, point.z - 1))
            neighbours.add(Point(point.x + 1, point.y - 1, point.z - 1))
            neighbours.add(Point(point.x + 1, point.y + 1, point.z - 1))

            neighbours.add(Point(point.x - 1, point.y - 1, point.z + 1))
            neighbours.add(Point(point.x - 1, point.y + 1, point.z + 1))
            neighbours.add(Point(point.x + 1, point.y - 1, point.z + 1))
            neighbours.add(Point(point.x + 1, point.y + 1, point.z + 1))

            neighbours.add(Point(point.x, point.y - 1, point.z - 1))
            neighbours.add(Point(point.x, point.y + 1, point.z - 1))
            neighbours.add(Point(point.x, point.y - 1, point.z + 1))
            neighbours.add(Point(point.x, point.y + 1, point.z + 1))

            neighbours.add(Point(point.x - 1, point.y, point.z - 1))
            neighbours.add(Point(point.x + 1, point.y, point.z - 1))
            neighbours.add(Point(point.x - 1, point.y, point.z + 1))
            neighbours.add(Point(point.x + 1, point.y, point.z + 1))
        }
    }

    return neighbours
}

data class Point(val x: Long, val y: Long, val z: Long)
data class WeightedPath(val path: Set<Point>, val weight: Long)
