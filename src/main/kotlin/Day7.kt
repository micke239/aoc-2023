import java.io.File
import kotlin.math.pow
import kotlin.time.measureTime

class Day7 {
    fun run() {
        val lines = File("src/main/resources/day7.txt").readLines()
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
        val cardRank = mapOf(
            'A' to 13, 'K' to 12, 'Q' to 11, 'J' to 10, 'T' to 9, '9' to 8, '8' to 7, '7' to 6, '6' to 5, '5' to 4, '4' to 3, '3' to 2, '2' to 1
        )

        val cardBids = lines.map {
            val (cards, bid) = it.split(" ")
            CardBid(cards.toList(), bid.toLong())
        }

        val rank = cardBids.map {
            val handRank = handRank(it.cards, cardRank)
            Triple(rankCards(it.cards, null), handRank, it)
        }.sortedWith(compareBy<Triple<Int, Int, CardBid>> { it.first }.thenBy { it.second })
            .mapIndexed { i, it -> it.third.bid * (i+1) }
            .sum()

        println(rank)
    }


    private fun part2(lines: List<String>) {
        val cardRank = mapOf(
            'A' to 13, 'K' to 12, 'Q' to 11, 'T' to 10, '9' to 9, '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2, 'J' to 1,
        )

        val cardBids = lines.map {
            val (cards, bid) = it.split(" ")
            CardBid(cards.toList(), bid.toLong())
        }

        val rank = cardBids.map {
            val handRank = handRank(it.cards, cardRank)
            Triple(rankCards(it.cards, 'J'), handRank, it)
        }.sortedWith(compareBy<Triple<Int, Int, CardBid>> { it.first }.thenBy { it.second })
            .mapIndexed { i, it -> it.third.bid * (i+1) }
            .sum()

        println(rank)
    }

    private fun rankCards(cards: List<Char>, joker: Char?) : Int {
        if (cards.all { it == cards[0] }) {
            return 6// five of a kind
        }

        val firstJoker = cards.indexOf(joker)
        if (firstJoker != -1) {
            return cards.filter {it != joker}.maxOf {
                val nc = cards.toMutableList()
                nc[firstJoker] = it
                rankCards(nc, joker)
            }
        }

        if (cards.any {card -> cards.count { card2 -> card == card2 } == 4}) {
            return 5//four of a kind
        }

        val three = cards.firstOrNull {card -> cards.count { card2 -> card == card2 } == 3}
        val pair = cards.firstOrNull {card -> cards.count { card2 -> card == card2 } == 2}
        if (three != null && pair != null) {
            return 4//full house
        }

        if (three != null) {
            return 3//three of a kind
        }

        if (pair != null && cards.filter{it != pair}.any {card -> cards.count { card2 -> card == card2 } == 2}) {
            return 2//two pair
        }

        if (pair != null) {
            //one pair
            return 1
        }

        return 0
    }

    private fun handRank(cards: List<Char>, cardRankings: Map<Char, Int>) : Int {
        val rankingCount = cardRankings.count().toDouble()
        return (0..<cards.count()).sumOf { cardRankings[cards[it]]!! * rankingCount.pow(4 - it).toInt() }
    }
}

data class CardBid(val cards: List<Char>, val bid: Long)