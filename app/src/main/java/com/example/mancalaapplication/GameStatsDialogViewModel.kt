package com.example.mancalaapplication

import androidx.lifecycle.*


class GameStatsDialogViewModel(repository: GameOutcomeRepository) : ViewModel() {

    var allGameOutcomes: LiveData<List<GameOutcome>> = repository.allGameOutcomes.asLiveData()
    var singlePlayerGameOutcomes: LiveData<List<GameOutcome>> =
        repository.singlePlayerGameOutcomes.asLiveData()

    var easyGamesTotal: LiveData<Int> = repository.easyGamesTotal.asLiveData()
    var easyGamesWon: LiveData<Int> = repository.easyGamesWon.asLiveData()
    var easyGames: MediatorLiveData<Pair<Int?, Int?>> =
        easyGamesTotal.combineLiveData(easyGamesWon) { a, b -> Pair(a, b)}

    var intermediateGamesTotal: LiveData<Int> = repository.intermediateGamesTotal.asLiveData()
    var intermediateGamesWon: LiveData<Int> = repository.intermediateGamesWon.asLiveData()
    var intermediateGames: MediatorLiveData<Pair<Int?, Int?>> =
        intermediateGamesTotal.combineLiveData(intermediateGamesWon) { a, b -> Pair(a, b)}

    var hardGamesTotal: LiveData<Int> = repository.hardGamesTotal.asLiveData()
    var hardGamesWon: LiveData<Int> = repository.hardGamesWon.asLiveData()
    var hardGames: MediatorLiveData<Pair<Int?, Int?>> =
        hardGamesTotal.combineLiveData(hardGamesWon) { a, b -> Pair(a, b)}

    var multiplayerGameOutcomes: LiveData<List<GameOutcome>> =
        repository.multiplayerGameOutcomes.asLiveData()

    fun <T, A, B> LiveData<A>.combineLiveData(
        other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {

        var source1emitted = false
        var source2emitted = false

        val result = MediatorLiveData<T>()

        val mergeF = {
            val source1Value = this.value
            val source2Value = other.value

            if (source1emitted && source2emitted) {
                result.value = onChange.invoke(source1Value!!, source2Value!!)
            }
        }

        result.addSource(this) { source1emitted = true; mergeF.invoke() }
        result.addSource(other) { source2emitted = true; mergeF.invoke() }

        return result
    }
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