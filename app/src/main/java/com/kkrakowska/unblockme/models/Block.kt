package com.kkrakowska.unblockme.models

enum class Orientation { HORIZONTAL, VERTICAL }

data class Block(
    val id: String,
    val x: Int,
    val y: Int,
    val length: Int,
    val orientation: Orientation,
    val isTarget: Boolean = false
) {
    fun getOccupiedCells(): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until length) {
            if (orientation == Orientation.HORIZONTAL) cells.add(Pair(x + i, y))
            else cells.add(Pair(x, y + i))
        }
        return cells
    }
}