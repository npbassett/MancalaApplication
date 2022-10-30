package com.example.mancalaapplication

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GameOutcomeRepository(private val gameOutcomeDao: GameOutcomeDao) {

    val allGameOutcomes: Flow<List<GameOutcome>> = gameOutcomeDao.getAllGameOutcomes()

    @WorkerThread
    suspend fun insert(gameOutcome: GameOutcome) {
        gameOutcomeDao.insert(gameOutcome)
    }

    @WorkerThread
    suspend fun deleteAll() {
        gameOutcomeDao.deleteAll()
    }
}