package aocutil

import kotlin.math.max
import kotlin.math.min

fun overlaps(
    range1: Range,
    range2: Range
    ) : Boolean
{
    return (range1.from <= range2.from && range1.to > range2.from) || (range1.from < range2.to && range1.to > range2.from)
}

fun union(range1: Range, range2: Range): Range? {
    if (overlaps(range1, range2)) {
        return Range(max(range1.from,range2.from), min(range1.to, range2.to))
    }
    return null;
}

data class Range(val from: Long, val to: Long)
