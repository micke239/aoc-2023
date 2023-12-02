import java.io.File

class Day2{
    fun run() {
        val lines = File("src/main/resources/day2.txt").readLines()
        part1(lines)
        part2(lines)
    }

    private fun part1(lines: List<String>) {
        val maxCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        val sum = lines.map { line ->
            val s1 = line.split(":")
            val idContainer = s1[0]
            val info = s1[1]

            val id = idContainer.split(" ")[1].toInt()
            val rounds = info.split("; ").map {
                it.split(", ").associate { cube ->
                    val pair = cube.trim().split(" ")
                    pair[1] to pair[0].toInt()
                }
            }.toList()

            Game(id, rounds)
        }.filter {
            !it.rounds.any { round -> round.any {cube -> cube.value > maxCubes[cube.key]!! }  }
        }.sumOf { it.id }

        println(sum)
    }

    private fun part2(lines: List<String>) {
        val sum = lines.map { line ->
            val s1 = line.split(":")
            val idContainer = s1[0]
            val info = s1[1]

            val id = idContainer.split(" ")[1].toInt()
            val rounds = info.split("; ").map {
                it.split(", ").associate { cube ->
                    val pair = cube.trim().split(" ")
                    pair[1] to pair[0].toInt()
                }
            }.toList()

            Game(id, rounds)
        }.map {
            it.rounds.fold(mutableMapOf<String, Int>()) { acc, round ->
                round.forEach { cube ->
                    if (cube.value > (acc[cube.key] ?: 0)) {
                        acc[cube.key] = cube.value
                    }
                }
                acc
            }
        }.sumOf {
            it.entries.fold(1L) { acc, e -> acc * e.value }
        }

        println(sum)
    }
}

data class Game(val id: Int, val rounds: List<Map<String, Int>>)