package com.example.mancalaapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.Random

class MancalaSinglePlayerViewModel : ViewModel() {
    var aiDifficulty = ""
        set(value) {
            (if (value in listOf("easy", "intermediate", "hard")) value
                else throw java.lang.IllegalArgumentException(
                "Difficulty must be either easy, intermediate, or hard.")).also { field = it }
        }

    private val _boardState = MutableStateFlow(
        mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0))
    val boardState: StateFlow<List<Int>> = _boardState

    private val _player1Turn = MutableStateFlow(true)
    val player1Turn: StateFlow<Boolean> = _player1Turn

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver

    private val _player1Pockets = listOf(0, 1, 2, 3, 4, 5)
    private val _player2Pockets = listOf(7, 8, 9, 10, 11, 12)

    fun restartGame() {
        _boardState.value = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
        _player1Turn.value = true
        _gameOver.value = false
    }

    fun checkPlayer1Winner(): Boolean {
        return boardState.value[6] > boardState.value[13]
    }

    fun checkTie(): Boolean {
        return boardState.value[6] == boardState.value[13]
    }

    fun pocketWrongSide(pocket: Int): Boolean {
        return (player1Turn.value && pocket in _player2Pockets) ||
                (!player1Turn.value && pocket in _player1Pockets)
    }

    fun pocketEmpty(pocket: Int): Boolean {
        return _boardState.value[pocket] == 0
    }

    fun applyMove(pocket: Int) {
        val boardStateBeforeMove = boardState.value.toMutableList()
        _player1Turn.value = if (checkMoveAgain(pocket, boardStateBeforeMove)) {
            player1Turn.value
        } else !player1Turn.value
        _boardState.value = moveStones(pocket, boardStateBeforeMove)
        _gameOver.value = checkGameOver(boardState.value)
        if (gameOver.value) {
            for (i in 0..5) {
                _boardState.value[6] += _boardState.value[i]
                _boardState.value[i] = 0
            }
            for (i in 7..12) {
                _boardState.value[13] += _boardState.value[i]
                _boardState.value[i] = 0
            }
        }
    }

    private fun moveStones(pocket: Int, currentBoardState: MutableList<Int>): MutableList<Int> {
        val currentPlayer1Turn = pocket in listOf(0, 1, 2, 3, 4, 5)
        val playersStore = if (currentPlayer1Turn) 6 else 13
        val otherPlayersStore = if (currentPlayer1Turn) 13 else 6
        val playersPockets = if (currentPlayer1Turn) {
            listOf(0, 1, 2, 3, 4, 5)
        } else {
            listOf(7, 8, 9, 10, 11, 12)
        }
        var numStonesToMove: Int = currentBoardState[pocket]
        currentBoardState[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            if (currentPocket == otherPlayersStore) {
                currentPocket = (currentPocket + 1) % 14
            }
            currentBoardState[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && currentBoardState[lastPocket] == 1
            && lastPocketOpposite != null) {
            currentBoardState[playersStore] += currentBoardState[lastPocketOpposite]
            currentBoardState[lastPocketOpposite] = 0
        }
        return currentBoardState
    }

    private fun checkMoveAgain(pocket: Int, currentBoardState: MutableList<Int>): Boolean {
        val currentPlayer1Turn = pocket in listOf(0, 1, 2, 3, 4, 5)
        val lastPocket = (pocket + currentBoardState[pocket]) % 14
        return (currentPlayer1Turn && lastPocket == 6) || (!currentPlayer1Turn && lastPocket == 13)
    }

    private fun checkBottomEmpty(currentBoardState: List<Int>): Boolean {
        for (i in 0..5) {
            if (currentBoardState[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkTopEmpty(currentBoardState: List<Int>): Boolean {
        for (i in 7..12) {
            if (currentBoardState[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkGameOver(currentBoardState: List<Int>): Boolean {
        return checkBottomEmpty(currentBoardState) || checkTopEmpty(currentBoardState)
    }

    private fun oppositePocket(pocket: Int): Int? {
        return if (pocket == 6 || pocket == 13) {
            null
        } else {
            12 - pocket
        }
    }

    fun aiMoveStones() {
        if (!_player1Turn.value) {
            when (aiDifficulty) {
                "easy" -> applyMove(greedyMove())
                "intermediate" -> applyMove(boundedMinimaxMoveAlphaBeta(3))
                "hard" -> applyMove(boundedMinimaxMoveAlphaBeta(8))
            }
        }
    }

    private fun randomMove(): Int {
        val possibleMoves = mutableListOf<Int>()
        for (i in 7..12) {
            if (boardState.value[i] != 0) possibleMoves.add(i)
        }
        val randomGenerator = Random(System.currentTimeMillis())
        val randomIndex = randomGenerator.nextInt(0, possibleMoves.size)
        return possibleMoves[randomIndex]
    }

    private fun evaluateBoard(currentBoardState: MutableList<Int>): Int {
        return currentBoardState[13] - currentBoardState[6]
    }

    private fun greedyMove(): Int {
        // always choose the move that adds the most stones to the player's store
        val possibleMoves = mutableListOf<Int>()
        for (i in 7..12) {
            if (boardState.value[i] != 0) possibleMoves.add(i)
        }
        if (possibleMoves.size == 1) return possibleMoves[0]
        val moveScores = mutableListOf<Int>()
        for (move in possibleMoves) {
            val boardStateAfterMove = moveStones(move, boardState.value.toMutableList())
            val moveScore = evaluateBoard(boardStateAfterMove)
            moveScores.add(moveScore)
        }
        return possibleMoves[moveScores.indexOf(moveScores.max())]
    }

    private fun boundedMinimax(currentBoardState: MutableList<Int>, currentPlayer1Turn: Boolean,
                               currentDepth: Int, maxDepth: Int): Int {
        if (currentDepth == maxDepth) {
            return evaluateBoard(currentBoardState)
        } else if (checkGameOver(currentBoardState)) {
            val boardAfterGameOver = currentBoardState.toMutableList()
            for (i in 0..5) {
                boardAfterGameOver[6] += boardAfterGameOver[i]
                boardAfterGameOver[i] = 0
            }
            for (i in 7..12) {
                boardAfterGameOver[13] += boardAfterGameOver[i]
                boardAfterGameOver[i] = 0
            }
            return evaluateBoard(boardAfterGameOver)
        }
        val playersPockets = if (currentPlayer1Turn) {
            listOf(0, 1, 2, 3, 4, 5)
        } else {
            listOf(7, 8, 9, 10, 11, 12)
        }
        // since there are 48 total stones, the score must be between -48 and 48
        var bestScore = if (currentPlayer1Turn) 49 else -49
        val possibleMoves = mutableListOf<Int>()
        for (pocket in playersPockets) {
            if (currentBoardState[pocket] != 0) possibleMoves.add(pocket)
        }
        for (move in possibleMoves) {
            val boardStateAfterMove = moveStones(move, currentBoardState.toMutableList())
            val player1TurnAfterMove = if (checkMoveAgain(move, currentBoardState.toMutableList())) {
                currentPlayer1Turn
            } else {
                !currentPlayer1Turn
            }
            val score = boundedMinimax(boardStateAfterMove, player1TurnAfterMove,
                currentDepth+1, maxDepth)
            if (currentPlayer1Turn) {
                if (score < bestScore) bestScore = score
            } else {
                if (score > bestScore) bestScore = score
            }
        }
        return bestScore
    }

    private fun boundedMinimaxMove(maxDepth: Int): Int {
        val possibleMoves = mutableListOf<Int>()
        for (i in 7..12) {
            if (boardState.value[i] != 0) possibleMoves.add(i)
        }
        val minimaxScores = mutableListOf<Int>()
        for (move in possibleMoves) {
            val boardStateAfterMove = moveStones(move, boardState.value.toMutableList())
            val player1TurnAfterMove = !checkMoveAgain(move, boardState.value.toMutableList())
            minimaxScores.add(boundedMinimax(boardStateAfterMove,
                player1TurnAfterMove, 1, maxDepth))
        }
        return possibleMoves[minimaxScores.indexOf(minimaxScores.max())]
    }

    private fun boundedMinimaxAlphaBeta(currentBoardState: MutableList<Int>,
                                        currentPlayer1Turn: Boolean, currentDepth: Int,
                                        maxDepth: Int, inputAlpha: Int, inputBeta: Int): Int {
        var alpha = inputAlpha
        var beta = inputBeta
        if (currentDepth == maxDepth) {
            return evaluateBoard(currentBoardState)
        } else if (checkGameOver(currentBoardState)) {
            val boardAfterGameOver = currentBoardState.toMutableList()
            for (i in 0..5) {
                boardAfterGameOver[6] += boardAfterGameOver[i]
                boardAfterGameOver[i] = 0
            }
            for (i in 7..12) {
                boardAfterGameOver[13] += boardAfterGameOver[i]
                boardAfterGameOver[i] = 0
            }
            return evaluateBoard(boardAfterGameOver)
        }
        val playersPockets = if (currentPlayer1Turn) {
            listOf(0, 1, 2, 3, 4, 5)
        } else {
            listOf(7, 8, 9, 10, 11, 12)
        }
        // since there are 48 total stones, the score must be between -48 and 48
        var bestScore = if (currentPlayer1Turn) 49 else -49
        val possibleMoves = mutableListOf<Int>()
        for (pocket in playersPockets) {
            if (currentBoardState[pocket] != 0) possibleMoves.add(pocket)
        }
        for (move in possibleMoves) {
            val boardStateAfterMove = moveStones(move, currentBoardState.toMutableList())
            val player1TurnAfterMove = if (checkMoveAgain(move, currentBoardState.toMutableList())) {
                currentPlayer1Turn
            } else {
                !currentPlayer1Turn
            }
            val score = boundedMinimaxAlphaBeta(boardStateAfterMove, player1TurnAfterMove,
                currentDepth+1, maxDepth, alpha, beta)
            if (currentPlayer1Turn) {
                if (score < bestScore) bestScore = score
                if (bestScore <= alpha)  break
                beta = min(beta, bestScore)

            } else {
                if (score > bestScore) bestScore = score
                if (bestScore >= beta) break
                alpha = max(alpha, bestScore)
            }
        }
        return bestScore
    }

    private fun boundedMinimaxMoveAlphaBeta(maxDepth: Int): Int {
        val possibleMoves = mutableListOf<Int>()
        for (i in 7..12) {
            if (boardState.value[i] != 0) possibleMoves.add(i)
        }
        val minimaxScores = mutableListOf<Int>()
        for (move in possibleMoves) {
            val boardStateAfterMove = moveStones(move, boardState.value.toMutableList())
            val player1TurnAfterMove = !checkMoveAgain(move, boardState.value.toMutableList())
            minimaxScores.add(boundedMinimaxAlphaBeta(boardStateAfterMove,
                player1TurnAfterMove, 1, maxDepth, -49, 49))
        }
        return possibleMoves[minimaxScores.indexOf(minimaxScores.max())]
    }

}