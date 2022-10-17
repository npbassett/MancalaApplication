package com.example.mancalaapplication

data class MancalaModel(
    var pocketStones: MutableList<Int>,
    var player1Turn: Boolean,
    var gameOver: Boolean
) {
    constructor(): this(
        mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0),
        true,
        false)
}