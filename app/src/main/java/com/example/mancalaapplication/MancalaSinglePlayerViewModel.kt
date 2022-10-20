package com.example.mancalaapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MancalaSinglePlayerViewModel : ViewModel() {
    var aiDifficulty = ""
        set(value) {
            (if (value in listOf("easy", "intermediate", "hard")) value
                else throw java.lang.IllegalArgumentException(
                "Difficulty must be either easy, intermediate, or hard.")).also { field = it }
        }

    private var _boardState = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
    private var _player1Turn = true
    private var _gameOver = false
    private val _player1Pockets = listOf(0, 1, 2, 3, 4, 5)
    private val _player2Pockets = listOf(7, 8, 9, 10, 11, 12)

    val boardState: List<Int>
        get() = _boardState.toList()
    val player1Turn: Boolean
        get() = _player1Turn
    val gameOver: Boolean
        get() = _gameOver

    fun restartGame() {
        _boardState = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
        _player1Turn = true
        _gameOver = false
    }

    fun checkPlayer1Winner(): Boolean {
        return _boardState[6] > _boardState[13]
    }

    fun pocketWrongSide(pocket: Int): Boolean {
        return (player1Turn && pocket in _player2Pockets) ||
                (!player1Turn && pocket in _player1Pockets)
    }

    fun pocketEmpty(pocket: Int): Boolean {
        return _boardState[pocket] == 0
    }

    fun moveStones(pocket: Int) {
        val playersStore = if (_player1Turn) 6 else 13
        val otherPlayersStore = if (_player1Turn) 13 else 6
        val playersPockets = if (_player1Turn) {
            listOf(0, 1, 2, 3, 4, 5)
        } else {
            listOf(7, 8, 9, 10, 11, 12)
        }
        var numStonesToMove: Int = _boardState[pocket]
        _boardState[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            if (currentPocket == otherPlayersStore) {
                currentPocket = (currentPocket + 1) % 14
            }
            _boardState[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && _boardState[lastPocket] == 1
            && lastPocketOpposite != null) {
            _boardState[playersStore] += _boardState[lastPocketOpposite]
            _boardState[lastPocketOpposite] = 0
        }
        // check if last stone ends in player's store. If so player gets another turn
        _player1Turn = if (lastPocket != playersStore) {
            !_player1Turn
        } else _player1Turn
        if (checkTopEmpty() || checkBottomEmpty()) {
            for (i in 0..5) {
                _boardState[6] += _boardState[i]
                _boardState[i] = 0
            }
            for (i in 7..12) {
                _boardState[13] += _boardState[i]
                _boardState[i] = 0
            }
            _gameOver = true
        }
    }

    private fun checkBottomEmpty(): Boolean {
        for (i in 0..5) {
            if (_boardState[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkTopEmpty(): Boolean {
        for (i in 7..12) {
            if (_boardState[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun oppositePocket(pocket: Int): Int? {
        return if (pocket == 6 || pocket == 13) {
            null
        } else {
            12 - pocket
        }
    }

    fun aiMoveStones() {
        if (!_player1Turn) {
            if (aiDifficulty == "easy") aiMoveStonesEasy()
            else if (aiDifficulty == "intermediate") aiMoveStonesIntermediate()
        }
    }

    private fun aiMoveStonesEasy() {
        val randomGenerator = Random(System.currentTimeMillis())
        val randomPocket = randomGenerator.nextInt(7, 12)
        if (_boardState[randomPocket] != 0) moveStones(randomPocket) else aiMoveStonesEasy()
    }

    private fun aiMoveStonesIntermediate() {
        val minimaxMove = minimaxMove()
        moveStones(minimaxMove)
    }

    private fun evaluateMove(pocket: Int, tempBoardState: MutableList<Int>,
                             tempPlayer1Turn: Boolean): Triple<Int, MutableList<Int>, Boolean> {
        Log.d("Intermediate", "BoardState = $tempBoardState, Player1Turn = $tempPlayer1Turn, evaluating move $pocket")
        // returns difference in stones in player 1's (human) store after the move, the board
        // state after the move, and whether the current player gets another turn.
        val playersStore = if (tempPlayer1Turn) 6 else 13
        val otherPlayersStore = if (tempPlayer1Turn) 13 else 6
        val playersPockets = if (tempPlayer1Turn) {
            listOf(0, 1, 2, 3, 4, 5)
        } else {
            listOf(7, 8, 9, 10, 11, 12)
        }
        val stonesInPlayer1StoreBefore = tempBoardState[6]
        var numStonesToMove: Int = tempBoardState[pocket]
        tempBoardState[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            if (currentPocket == otherPlayersStore) {
                currentPocket = (currentPocket + 1) % 14
            }
            tempBoardState[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && tempBoardState[lastPocket] == 1
            && lastPocketOpposite != null) {
            tempBoardState[playersStore] += tempBoardState[lastPocketOpposite]
            tempBoardState[lastPocketOpposite] = 0
        }
        // check if last stone ends in player's store. If so player gets another turn
        if (checkTopEmpty() || checkBottomEmpty()) {
            for (i in 0..5) {
                tempBoardState[6] += tempBoardState[i]
                tempBoardState[i] = 0
            }
            for (i in 7..12) {
                tempBoardState[13] += tempBoardState[i]
                tempBoardState[i] = 0
            }
        }
        val stonesInPlayer1StoreDifference = tempBoardState[6] - stonesInPlayer1StoreBefore
        return if (lastPocket != playersStore) {
            Triple(stonesInPlayer1StoreDifference, tempBoardState,false)
        } else {
            Triple(stonesInPlayer1StoreDifference, tempBoardState,true)
        }
    }

    private fun minimaxMove(): Int {
        // TODO: account for ability to move again if last stone ends in player's store.
        // create list of possible moves by player 2
        val player2PossibleMoves = mutableListOf<Int>()
        for (i in 7..12) {
            if (_boardState[i] != 0) player2PossibleMoves.add(i)
        }
        // if only one possible move, return that move without any more calculation
        if (player2PossibleMoves.size == 1) return player2PossibleMoves[0]
        val player1MaximumScores = mutableListOf<Int>()
        for (player2Move in player2PossibleMoves) {
            val boardStateBeforePlayer2Move = _boardState.toMutableList()
            val (scoreAfterPlayer2Move, boardStateAfterPlayer2Move, player1TurnAfterPlayer2Move) =
                evaluateMove(player2Move, boardStateBeforePlayer2Move, false)
            // create list of possible moves by player 1
            val player1PossibleMoves = mutableListOf<Int>()
            for (j in 0..5) {
                if (boardStateAfterPlayer2Move[j] != 0) player1PossibleMoves.add(j)
            }
            val player1Scores = mutableListOf<Int>()
            for (player1Move in player1PossibleMoves) {
                val tempBoardStateAfterPlayer2Move = boardStateAfterPlayer2Move.toMutableList()
                val (scoreAfterPlayer1Move, boardStateAfterPlayer1Move, player1TurnAfterPlayer1Move) =
                    evaluateMove(player1Move, tempBoardStateAfterPlayer2Move, true)
                player1Scores.add(scoreAfterPlayer2Move + scoreAfterPlayer1Move)
            }
            player1MaximumScores.add(player1Scores.max())
        }
        // return move that minimizes the maximum possible score by player 1 (human)
        Log.d("Intermediate", "chose move ${player2PossibleMoves[player1MaximumScores.indexOf(player1MaximumScores.min())]}")
        return player2PossibleMoves[player1MaximumScores.indexOf(player1MaximumScores.min())]
    }

}