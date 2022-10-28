package com.example.mancalaapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MancalaMultiplayerViewModel : ViewModel() {
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
        return _boardState.value[6] > _boardState.value[13]
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
}