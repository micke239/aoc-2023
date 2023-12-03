package paths

fun getNeighbours(point: Point, allowDiagonal: Boolean = false, is3d: Boolean = false): Set<Point>
{
    val neighbours = mutableSetOf<Point>();
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

data class Point(val x: Long, val y: Long, val z: Long);