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

    @Query("SELECT * FROM game_outcome_table WHERE game_type = 'single player'")
    fun getSinglePlayerGameOutcomes(): Flow<List<GameOutcome>>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'easy'")
    fun getEasyGamesTotal(): Flow<Int>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'easy' AND player_1_winner = 'true'")
    fun getEasyGamesWon(): Flow<Int>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'intermediate'")
    fun getIntermediateGamesTotal(): Flow<Int>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'intermediate' AND player_1_winner = 'true'")
    fun getIntermediateGamesWon(): Flow<Int>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'hard'")
    fun getHardGamesTotal(): Flow<Int>

    @Query("SELECT count(*) FROM game_outcome_table WHERE game_type = 'single player' AND difficulty = 'hard' AND player_1_winner = 'true'")
    fun getHardGamesWon(): Flow<Int>

    @Query("SELECT * FROM game_outcome_table WHERE game_type = 'multiplayer'")
    fun getMultiplayerGameOutcomes(): Flow<List<GameOutcome>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameOutcome: GameOutcome)

    @Query("DELETE FROM game_outcome_table")
    suspend fun deleteAll()
}