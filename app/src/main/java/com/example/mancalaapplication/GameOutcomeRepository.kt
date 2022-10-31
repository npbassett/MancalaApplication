package com.example.mancalaapplication

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GameOutcomeRepository(private val gameOutcomeDao: GameOutcomeDao) {

    val allGameOutcomes: Flow<List<GameOutcome>> = gameOutcomeDao.getAllGameOutcomes()
    val singlePlayerGameOutcomes: Flow<List<GameOutcome>> =
        gameOutcomeDao.getSinglePlayerGameOutcomes()
    val easyGamesTotal: Flow<Int> = gameOutcomeDao.getEasyGamesTotal()
    val easyGamesWon: Flow<Int> = gameOutcomeDao.getEasyGamesWon()
    val intermediateGamesTotal: Flow<Int> = gameOutcomeDao.getIntermediateGamesTotal()
    val intermediateGamesWon: Flow<Int> = gameOutcomeDao.getIntermediateGamesWon()
    val hardGamesTotal: Flow<Int> = gameOutcomeDao.getHardGamesTotal()
    val hardGamesWon: Flow<Int> = gameOutcomeDao.getHardGamesWon()
    val multiplayerGameOutcomes: Flow<List<GameOutcome>> =
        gameOutcomeDao.getMultiplayerGameOutcomes()

    @WorkerThread
    suspend fun insert(gameOutcome: GameOutcome) {
        gameOutcomeDao.insert(gameOutcome)
    }

    @WorkerThread
    suspend fun deleteAll() {
        gameOutcomeDao.deleteAll()
    }
}