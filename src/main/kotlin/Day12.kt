import java.io.File
import kotlin.time.measureTime

enum class SpringStatus {
    Unknown, Operational, Damaged
}

fun main() {
    val lines = File("src/main/resources/day12.txt").readLines()
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
    val rows = parse(lines)
    val sum = rows.sumOf{ row ->
        countMatches(row.springs.toMutableList(), 0, row.condition, mutableListOf(0))
    }
    println(sum)
}

private fun part2(lines: List<String>) {
    val sum = parse(lines).map { row ->
        val unfoldedSprings = mutableListOf<SpringStatus>()
        val unfoldedCondition = mutableListOf<Int>()

        for(i in (1..5)) {
            if (i != 1) {
                unfoldedSprings.add(SpringStatus.Unknown)
            }
            unfoldedSprings.addAll(row.springs)
            unfoldedCondition.addAll(row.condition)
        }

        Row(unfoldedSprings, unfoldedCondition)
    }.sumOf{ row ->
        countMatches(row.springs.toMutableList(), 0, row.condition, mutableListOf(0))
    }

    println(sum)
}

private fun countMatches(springs: MutableList<SpringStatus>, idx: Int, condition: List<Int>, currentStatus: MutableList<Int>, cache : MutableMap<String,Long> = mutableMapOf()) : Long/*List<List<SpringStatus>>*/ {
    val cacheKey = "${idx}_${condition.joinToString(",")}_${currentStatus.joinToString(",")}"

    val cached = cache[cacheKey]
    if (cached != null) {
        return cached
    }

    val result =
        if (idx == springs.size) {
            if (currentStatus.last() == 0) {
                currentStatus.removeLast()
            }
            if (condition.size == currentStatus.size && condition.last() == currentStatus.last()) {
                1L
            } else {
                0L
            }
        } else if (!isLastValid(currentStatus, condition)) {
            0L
        } else {
            when(springs[idx]) {
                SpringStatus.Unknown -> {
                    val s2 = springs.toMutableList()
                    val status2 = currentStatus.toMutableList()

                    springs[idx] = SpringStatus.Damaged
                    currentStatus[currentStatus.size-1]++

                    s2[idx] = SpringStatus.Operational
                    if (idx != 0 && s2[idx-1] == SpringStatus.Damaged) {
                        status2.add(0)
                    }

                    countMatches(springs, idx+1, condition, currentStatus, cache) + countMatches(s2, idx+1, condition, status2, cache)
                }
                SpringStatus.Damaged -> {
                    currentStatus[currentStatus.size-1]++
                    countMatches(springs, idx+1, condition, currentStatus, cache)
                }
                else -> {
                    if (idx != 0 && springs[idx-1] == SpringStatus.Damaged) {
                        currentStatus.add(0)
                    }
                    countMatches(springs, idx+1, condition, currentStatus, cache)
                }
            }
        }

    cache[cacheKey] = result

    return result
}

private fun isLastValid(currentStatus: List<Int>, condition: List<Int>): Boolean {
    val isNewStatus = currentStatus.last() == 0
    if (isNewStatus) {
        if (currentStatus.size == 1) {
            return true
        } else if (condition[currentStatus.size-2] != currentStatus[currentStatus.size-2]) {
            return false
        }
    } else if (currentStatus.size > condition.size) {
        return false
    } else if (currentStatus.last() > condition[currentStatus.size-1]) {
        return false
    }

    return true
}

private fun parse(lines: List<String>): List<Row> {
    return lines.map {
        val split = it.split(" ")
        val springs = split[0].map { c -> when(c) {
            '#' -> SpringStatus.Damaged
            '.' -> SpringStatus.Operational
            '?' -> SpringStatus.Unknown
            else -> throw UnsupportedOperationException()
        }}
        val condition = split[1].split(",").map { i -> i.toInt() }
        Row(springs, condition)
    }
}

data class Row(val springs: List<SpringStatus>, val condition: List<Int>)