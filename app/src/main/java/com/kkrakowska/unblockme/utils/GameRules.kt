package com.kkrakowska.unblockme.utils

import com.kkrakowska.unblockme.models.Block
import com.kkrakowska.unblockme.models.Orientation

object GameRules {
    private val optimalMoves = mapOf(
        1 to 4, 2 to 4, 3 to 5, 4 to 6, 5 to 7,
        6 to 8, 7 to 9, 8 to 9, 9 to 9, 10 to 11,
        11 to 11, 12 to 11, 13 to 12, 14 to 12, 15 to 13
    )

    fun calculateStars(levelNumber: Int, movesCount: Int): Int {
        val optimal = optimalMoves[levelNumber] ?: 8
        return when {
            movesCount <= optimal + 1 -> 3
            movesCount <= optimal * 2 -> 2
            else -> 1
        }
    }

    fun calculateBounds(current: Block, all: List<Block>, gridSize: Int): Pair<Int, Int> {
        var min = 0
        var max = gridSize - current.length
        all.filter { it.id != current.id }.forEach { other ->
            val occ = other.getOccupiedCells()
            if (current.orientation == Orientation.HORIZONTAL) {
                if (occ.any { it.second == current.y }) {
                    val oMax = occ.maxOf { it.first }
                    val oMin = occ.minOf { it.first }
                    if (oMax < current.x) min = maxOf(min, oMax + 1)
                    else if (oMin > current.x) max = minOf(max, oMin - current.length)
                }
            } else {
                if (occ.any { it.first == current.x }) {
                    val oMax = occ.maxOf { it.second }
                    val oMin = occ.minOf { it.second }
                    if (oMax < current.y) min = maxOf(min, oMax + 1)
                    else if (oMin > current.y) max = minOf(max, oMin - current.length)
                }
            }
        }
        return Pair(min, max)
    }
}