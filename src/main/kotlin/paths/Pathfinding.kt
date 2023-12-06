package paths

import java.util.*

fun findPath(
    from: Point,
    isEnd: (Point) -> Boolean,
    getWeight: (Point) -> Long,
    isBlocked: (Point, Point) -> Boolean,
    multiEnd: Boolean,
    allowDiagonal: Boolean = false,
    is3d: Boolean = false
    ) : WeightedPath?
{
    var cheapestEnd: WeightedPath? = null
    val cheapest = mutableMapOf<Point, WeightedPath>()
    val queue = LinkedList<Triple<Set<Point>, Long, Point>>()
    queue.addLast(Triple(emptySet(), -getWeight(from), from))
    while(queue.any())
    {
        val (prevPath, prevWeight, newNode) = queue.pop()
        val weight = prevWeight + getWeight(newNode)

        if ((cheapest[newNode]?.weight ?: Long.MAX_VALUE) < weight)
        {
            continue
        }

        if (multiEnd && cheapestEnd != null && weight >= cheapestEnd.weight)
        {
            continue
        }

        val newPath = prevPath.toHashSet()
        newPath.add(newNode)
        val wp = WeightedPath(newPath, weight)

        cheapest[newNode] = wp

        if (isEnd(newNode))
        {
            if (multiEnd) {
                cheapestEnd = wp
                continue
            }
            return wp
        }

        getNeighbours(newNode, allowDiagonal, is3d)
            .filter {!isBlocked(newNode, it)}
            .filter {!prevPath.contains(it)}
            .map{Triple(newPath,weight,it)}
            .forEach{queue.addLast(it)}
    }

    return cheapestEnd
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
