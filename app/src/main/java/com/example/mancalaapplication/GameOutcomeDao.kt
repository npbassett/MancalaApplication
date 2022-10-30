package com.example.mancalaapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameOutcomeDao {

    @Query("SELECT * FROM game_outcome_table")
    fun getAllGameOutcomes(): Flow<List<GameOutcome>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameOutcome: GameOutcome)

    @Query("DELETE FROM game_outcome_table")
    suspend fun deleteAll()
}