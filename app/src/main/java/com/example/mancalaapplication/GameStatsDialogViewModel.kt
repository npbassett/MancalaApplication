package com.example.mancalaapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class GameStatsDialogViewModel(repository: GameOutcomeRepository) : ViewModel() {

    var allGameOutcomes: LiveData<List<GameOutcome>> = repository.allGameOutcomes.asLiveData()
    var singlePlayerGameOutcomes: LiveData<List<GameOutcome>> =
        repository.singlePlayerGameOutcomes.asLiveData()
    var easyGamesTotal: LiveData<Int> = repository.easyGamesTotal.asLiveData()
    var easyGamesWon: LiveData<Int> = repository.easyGamesWon.asLiveData()
    var intermediateGamesTotal: LiveData<Int> = repository.intermediateGamesTotal.asLiveData()
    var intermediateGamesWon: LiveData<Int> = repository.intermediateGamesWon.asLiveData()
    var hardGamesTotal: LiveData<Int> = repository.hardGamesTotal.asLiveData()
    var hardGamesWon: LiveData<Int> = repository.hardGamesWon.asLiveData()
    var multiplayerGameOutcomes: LiveData<List<GameOutcome>> =
        repository.multiplayerGameOutcomes.asLiveData()
}

class GameStatsDialogViewModelFactory(
    private val repository: GameOutcomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameStatsDialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameStatsDialogViewModel(repository) as T
        }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}