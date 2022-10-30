package com.example.mancalaapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_outcome_table")
class GameOutcome(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "time_completed") val timeCompleted: Int,
    @ColumnInfo(name = "game_type") val gameType: String,
    @ColumnInfo(name = "difficulty") val difficulty: String?,
    @ColumnInfo(name = "player_1_winner") val player1Winner: Boolean
    )