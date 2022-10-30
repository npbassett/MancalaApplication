package com.example.mancalaapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [GameOutcome::class], version = 1, exportSchema = false)
abstract class GameOutcomeRoomDatabase : RoomDatabase() {

    abstract fun GameOutcomeDao(): GameOutcomeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: GameOutcomeRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GameOutcomeRoomDatabase {
            // if the INSTANCE is not null, then return it. If it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameOutcomeRoomDatabase::class.java,
                    "game_outcome_database"
                )
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}