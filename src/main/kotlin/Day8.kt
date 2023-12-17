import java.io.File
import kotlin.time.measureTime

fun main() {
    val lines = File("src/main/resources/day8.txt").readLines()
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
    val instructions = lines.first().map { if (it == 'L') 0 else 1 }.toList()
    val nodes = lines.drop(2).map {
        val split = it.split(" = ")
        val nodeName = split[0].trim()
        val node1 = split[1].replace("(", "").split(",")[0].trim()
        val node2 = split[1].replace(")", "").split(",")[1].trim()

        nodeName to (listOf( node1, node2))
    }.toMap()

    var i = 0
    var node = "AAA"
    while(node != "ZZZ") {
        val inst = i++ % (instructions.count())
        node = nodes[node]!![instructions[inst]]
    }

    println(i)
}

private fun part2(lines: List<String>) {
    val instructions = lines.first().map { if (it == 'L') 0 else 1 }.toList()
    val nodes = lines.drop(2).map {
        val split = it.split(" = ")
        val nodeName = split[0].trim()
        val node1 = split[1].replace("(", "").split(",")[0].trim()
        val node2 = split[1].replace(")", "").split(",")[1].trim()

        nodeName to (listOf( node1, node2))
    }.toMap()

    val found = mutableMapOf<String,Int>()
    var i = 0
    val startNodes = nodes.keys.filter { it.endsWith('A') };
    var currNodes = startNodes.map { n -> n to n }.toSet()
    while(currNodes.any()) {
        val inst = i++ % instructions.count()

        currNodes = currNodes.map { it.first to nodes[it.second]!![instructions[inst]] }.toSet()

        currNodes.filter { it.second.endsWith('Z') }.forEach {
            found[it.first] = i
        }

        currNodes = currNodes.filter { !it.second.endsWith('Z') }.toSet()
    }

    println(found)

    var i2 = 1L
    val max = found.values.max().toLong()
    val values = found.values.filter { it.toLong() != max }.map { it.toLong() }
    while (values.any { ((max * i2) % it * i2 ) != 0L }) { //all divisible
        i2++;
    }

    println(i2 * max)
}
