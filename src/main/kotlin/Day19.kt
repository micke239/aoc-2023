import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

private val Xmas = listOf("x","m","a","s");


fun main() {
    val lines = File("src/main/resources/day19.txt").readLines()
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
    val functions = lines.takeWhile {
        it != ""
    }.associate {
        val s = it.split("{")
        val name = s[0]
        val cond = s[1].replace("}", "")

        val condition = parseIfFunction(cond)

        name to condition
    }

    val sum = lines.takeLastWhile { it != "" }
        .map {
            it.replace("{","").replace("}","")
                .split(",")
                .associate {
                    val x = it.split("=")
                    x[0] to x[1].toLong()
                }
        }
        .filter { isAccepted(it, functions, functions["in"]!!) }
        .sumOf { it["x"]!! + it["m"]!! + it["a"]!! + it["s"]!! }

    println(sum)
}

private fun part2(lines: List<String>) {
    val functions = lines.takeWhile {
        it != ""
    }.associate {
        val s = it.split("{")
        val name = s[0]
        val cond = s[1].replace("}", "")

        val condition = parseIfFunction(cond)

        name to condition
    }

    val ranges = countAcceptedCombinations(functions, functions["in"]!!, mapOf())

    println(ranges)
}


private fun countAcceptedCombinations(functions: Map<String,IfFunction>, ifFunction: IfFunction, dataRanges: Map<String, LongRange>): Long {
    val l = ifFunction.condition.left.toLongOrNull()
    val r = ifFunction.condition.right.toLongOrNull()

    val (variable, trueRange, falseRange) =
        when(ifFunction.condition.operator) {
            '<' -> when(l) {
                null -> Triple(ifFunction.condition.left, 1..<r!!, r..4000 )
                else -> Triple(ifFunction.condition.right, l+1..4000, 1..l )
            }
            else -> when(l) {
                null -> Triple(ifFunction.condition.left, (r!!+1)..4000, 1..r )
                else -> Triple(ifFunction.condition.right, 1..<l, l..4000 )
            }
        }

    val trueCount = countAcceptedCombinations(ifFunction.ifTrue, functions, addRange(dataRanges, variable, trueRange))
    val falseCount = countAcceptedCombinations(ifFunction.ifFalse, functions, addRange(dataRanges, variable, falseRange))

    return trueCount + falseCount
}

fun countAcceptedCombinations(instruction: Instruction, functions: Map<String,IfFunction>, dataRanges: Map<String,LongRange>) : Long {
    return if (dataRanges.any { it.value.last < it.value.first })
            0L
        else if (instruction.accepted) {
            Xmas.fold(1L) { acc, it ->
                val v = dataRanges[it]
                if (v != null) {
                    acc * (v.last - v.first + 1)
                } else {
                    acc * 4000
                }
            }
        }
        else if (instruction.rejected) {
            0L
        }
        else if (instruction.funCall != null) {
            countAcceptedCombinations(functions, functions[instruction.funCall]!!, dataRanges)
        }
        else {
            countAcceptedCombinations(functions, instruction.condition!!, dataRanges)
        }
}

fun addRange(dataRanges: Map<String, LongRange>, variable: String, range: LongRange): Map<String,LongRange> {
    val newRanges = dataRanges.toMutableMap()

    val currentRange = dataRanges[variable]
    if (currentRange == null) {
        newRanges[variable] = range
    }
    else {
        newRanges[variable] = max(currentRange.first,range.first)..min(currentRange.last,range.last)
    }

    return newRanges
}

private fun isAccepted(data: Map<String,Long>, functions: Map<String,IfFunction>, ifFunction: IfFunction): Boolean {
    val instruction =
        if (matchesCondition(ifFunction.condition, data)) {
            ifFunction.ifTrue
        } else {
            ifFunction.ifFalse
        }

    return if (instruction.accepted) {
        true
    }
    else if (instruction.rejected) {
        false
    }
    else if (instruction.funCall != null) {
        isAccepted(data, functions, functions[instruction.funCall]!!)
    }
    else {
        isAccepted(data, functions, instruction.condition!!)
    }
}

fun matchesCondition(condition: Condition, data: Map<String,Long>): Boolean {
    val left = condition.left.toLongOrNull() ?: data[condition.left]!!
    val right = condition.right.toLongOrNull() ?: data[condition.right]!!

    return when (condition.operator) {
        '<' -> left < right
        else -> left > right
    }
}

private fun parseIfFunction(inst: String) : IfFunction {
    var i = inst.indexOf(":")
    val cond = inst.substring(0..<i)
    val condition = parseCondition(cond)

    val f =  inst.substring(i+1)
    i = f.indexOf(",")
    val trueInst = parseInstruction(f.substring(0..<i))
    val falseInst = parseInstruction(f.substring(i+1))

    return IfFunction(condition, trueInst, falseInst)
}

private fun parseCondition(cond: String): Condition {
    return if (cond.contains("<")) {
        val s = cond.split("<")
        Condition('<',s[0],s[1])
    } else {
        val s = cond.split(">")
        Condition('>',s[0],s[1])
    }
}

private fun parseInstruction(inst: String): Instruction {
    return if (inst == "A")
            Instruction(true,false,null,null)
        else if (inst == "R")
            Instruction(false, true, null,null)
        else if (inst.contains(":"))
            Instruction(false,false,null,parseIfFunction(inst))
        else
            Instruction(false,false, inst,null)
}

data class Instruction(val accepted: Boolean, val rejected: Boolean, val funCall: String?, val condition: IfFunction?)
data class IfFunction(val condition: Condition, val ifTrue: Instruction, val ifFalse: Instruction)
data class Condition(val operator: Char, val left: String, val right: String);