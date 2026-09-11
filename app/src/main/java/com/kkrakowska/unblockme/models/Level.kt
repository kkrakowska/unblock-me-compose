package com.kkrakowska.unblockme.models

data class Level(
    val levelId: Int,
    val gridSize: Int = 6,
    val blocks: List<Block>,
    val exitY: Int
)