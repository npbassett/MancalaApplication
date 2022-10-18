package com.example.mancalaapplication

import androidx.lifecycle.ViewModel

class MancalaMultiplayerViewModel : ViewModel() {
    private var _pocketStones = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
    private var _player1Turn = true
    private var _gameOver = false
    private val _player1Pockets = listOf(0, 1, 2, 3, 4, 5)
    private val _player2Pockets = listOf(7, 8, 9, 10, 11, 12)

    val pocketStones: List<Int>
        get() = _pocketStones.toList()
    val player1Turn: Boolean
        get() = _player1Turn
    val gameOver: Boolean
        get() = _gameOver

    fun restartGame() {
        _pocketStones = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
        _player1Turn = true
        _gameOver = false
    }

    fun checkPlayer1Winner(): Boolean {
        return _pocketStones[6] > _pocketStones[13]
    }

    fun pocketWrongSide(pocket: Int): Boolean {
        return (player1Turn && pocket in _player2Pockets) ||
            (!player1Turn && pocket in _player1Pockets)
    }

    fun pocketEmpty(pocket: Int): Boolean {
        return _pocketStones[pocket] == 0
    }

    fun moveStones(pocket: Int) {
        val playersStore = if (_player1Turn) 6 else 13
        val otherPlayersStore = if (_player1Turn) 13 else 6
        val playersPockets = if (_player1Turn) {
            mutableListOf(0, 1, 2, 3, 4, 5)
        } else {
            mutableListOf(7, 8, 9, 10, 11, 12)
        }
        var numStonesToMove: Int = _pocketStones[pocket]
        _pocketStones[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            if (currentPocket == otherPlayersStore) {
                currentPocket = (currentPocket + 1) % 14
            }
            _pocketStones[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && _pocketStones[lastPocket] == 1
            && lastPocketOpposite != null) {
            _pocketStones[playersStore] += _pocketStones[lastPocketOpposite]
            _pocketStones[lastPocketOpposite] = 0
        }
        // check if last stone ends in player's store. If so player gets another turn
        _player1Turn = if (lastPocket != playersStore) {
            !_player1Turn
        } else _player1Turn
        if (checkTopEmpty() || checkBottomEmpty()) {
            for (i in 0..5) {
                _pocketStones[6] += _pocketStones[i]
                _pocketStones[i] = 0
            }
            for (i in 7..12) {
                _pocketStones[13] += _pocketStones[i]
                _pocketStones[i] = 0
            }
            _gameOver = true
        }
    }

    private fun checkBottomEmpty(): Boolean {
        for (i in 0..5) {
            if (_pocketStones[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkTopEmpty(): Boolean {
        for (i in 7..12) {
            if (_pocketStones[i] != 0) {
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
}