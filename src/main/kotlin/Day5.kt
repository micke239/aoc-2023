import java.io.File
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

class Day5 {
    fun run() {
        val lines = File("src/main/resources/day5.txt").readLines()
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
        val seedToSoil = mutableListOf<Mapping>()
        val soilToFertilizer = mutableListOf<Mapping>()
        val fertilizerToWater = mutableListOf<Mapping>()
        val waterToLight = mutableListOf<Mapping>()
        val lightToTemperature = mutableListOf<Mapping>()
        val temperatureToHumidity = mutableListOf<Mapping>()
        val humidityToLocation = mutableListOf<Mapping>()

        val seeds = mutableListOf<Long>()
        var current = seedToSoil;
        lines.forEach {
            if (it.isBlank()) {
                return@forEach;
            }
            if (it.startsWith("seeds:")) {
                val s = it.split(" ");
                s.drop(1).forEach {y -> seeds.add(y.toLong()) }
                return@forEach
            }
            if (!it[0].isDigit()) {
                current = when (it) {
                    "seed-to-soil map:" -> seedToSoil
                    "soil-to-fertilizer map:" -> soilToFertilizer
                    "fertilizer-to-water map:" -> fertilizerToWater
                    "water-to-light map:" -> waterToLight
                    "light-to-temperature map:" -> lightToTemperature
                    "temperature-to-humidity map:" -> temperatureToHumidity
                    "humidity-to-location map:" -> humidityToLocation
                    else -> throw UnsupportedOperationException()
                }
                return@forEach
            }

            val split = it.split(" ");
            val toStart = split[0].toLong()
            val fromStart = split[1].toLong()
            val range = split[2].toLong()

            current.add(Mapping(fromStart, fromStart + range, toStart - fromStart))
        }

        val location = seeds.minOf{seed ->
            val soil = mapIt(seedToSoil, seed);
            val fertilizer = mapIt(soilToFertilizer, soil);
            val water = mapIt(fertilizerToWater, fertilizer);
            val light = mapIt(waterToLight, water);
            val temperature = mapIt(lightToTemperature, light);
            val humidity = mapIt(temperatureToHumidity, temperature);
            val location = mapIt(humidityToLocation, humidity);
            location
        }

        println(location);
    }

    private fun part2(lines: List<String>) {
        val seedToSoil = mutableListOf<Mapping>()
        val soilToFertilizer = mutableListOf<Mapping>()
        val fertilizerToWater = mutableListOf<Mapping>()
        val waterToLight = mutableListOf<Mapping>()
        val lightToTemperature = mutableListOf<Mapping>()
        val temperatureToHumidity = mutableListOf<Mapping>()
        val humidityToLocation = mutableListOf<Mapping>()

        val seeds = mutableSetOf<SeedRange>()
        var current = seedToSoil;
        lines.forEach {
            if (it.isBlank()) {
                return@forEach;
            }
            if (it.startsWith("seeds:")) {
                val s = it.split(" ");
                s.drop(1).chunked(2).forEach {y -> seeds.add(SeedRange(y[0].toLong(), y[0].toLong()+y[1].toLong())) }
                return@forEach
            }
            if (!it[0].isDigit()) {
                current = when (it) {
                    "seed-to-soil map:" -> seedToSoil
                    "soil-to-fertilizer map:" -> soilToFertilizer
                    "fertilizer-to-water map:" -> fertilizerToWater
                    "water-to-light map:" -> waterToLight
                    "light-to-temperature map:" -> lightToTemperature
                    "temperature-to-humidity map:" -> temperatureToHumidity
                    "humidity-to-location map:" -> humidityToLocation
                    else -> throw UnsupportedOperationException()
                }
                return@forEach
            }

            val split = it.split(" ");
            val toStart = split[0].toLong()
            val fromStart = split[1].toLong()
            val range = split[2].toLong()

            current.add(Mapping(fromStart, fromStart + range, toStart - fromStart))
        }

        val soil = mapIt(seedToSoil, seeds)
        val fertilizer = mapIt(soilToFertilizer, soil)
        val water = mapIt(fertilizerToWater, fertilizer)
        val light = mapIt(waterToLight, water)
        val temperature = mapIt(lightToTemperature, light)
        val humidity = mapIt(temperatureToHumidity, temperature)
        val location = mapIt(humidityToLocation, humidity)

        println(location.minOf { it.from })
    }

    private fun mapIt(mappings: List<Mapping>, from: Long): Long {
        val mapping = mappings.firstOrNull { from >= it.from && from < it.to }
        return if (mapping != null) from + (mapping.diff) else from
    }

    private fun mapIt(mappings: List<Mapping>, ranges: Set<SeedRange>): Set<SeedRange> {
        val newRanges = mutableSetOf<SeedRange>()

        val rangeQueue = LinkedList<SeedRange>()
        rangeQueue.addAll(ranges)

        while(rangeQueue.any()) {
            val range = rangeQueue.pop()
            val mapping = mappings.firstOrNull { mapping ->
                (range.from <= mapping.from && range.to > mapping.from) || (range.from < mapping.to && range.to > mapping.from)
            }

            if (mapping == null) {
                newRanges.add(range)
                continue
            }

            if (range.from < mapping.from) {
                rangeQueue.add(SeedRange(range.from, mapping.from))
            }
            if (range.to >= mapping.from) {
                newRanges.add(SeedRange(max(range.from,mapping.from) + mapping.diff, min(range.to, mapping.to) + mapping.diff))
            }
            if (range.to > mapping.to) {
                rangeQueue.add(SeedRange(mapping.to, range.to))
            }
        }

        return newRanges
    }
}

data class Mapping(val from: Long, val to: Long, val diff: Long)
data class SeedRange(val from: Long, val to: Long)
