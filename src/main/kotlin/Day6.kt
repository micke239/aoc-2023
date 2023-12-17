import kotlin.time.measureTime

fun main() {
    var timeTaken = measureTime {
        part1()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2()
    }
    println(timeTaken)
}

private fun part1() {
    val input = mapOf(
        46 to 214,
        80 to 1177,
        78 to 1402,
        66 to 1024
    )

    val res = input.entries.map {
        val duration = it.key
        val record = it.value

        (1..<duration).map { i ->
            (i * (duration - i))
        }.count { i -> i > record }
    }.fold(1L) { acc, i -> acc * i }

    println(res)
}

private fun part2() {
    val input = mapOf(
        46807866L to 214117714021024L,
    )

    val res = input.entries.map {
        val duration = it.key
        val record = it.value

        (1..<duration).map { i ->
            (i * (duration - i))
        }.count { i -> i > record }
    }.fold(1L) { acc, i -> acc * i }

    println(res)
}