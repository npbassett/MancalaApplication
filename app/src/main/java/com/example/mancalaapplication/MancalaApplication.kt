package com.example.mancalaapplication

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MancalaApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { GameOutcomeRoomDatabase.getDatabase(
        this,
        applicationScope
    ) }
    val repository by lazy { GameOutcomeRepository(database.GameOutcomeDao()) }
}