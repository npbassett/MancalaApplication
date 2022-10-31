package com.example.mancalaapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class GameStatsDialogFragment : DialogFragment() {

    private lateinit var viewModel: GameStatsDialogViewModel
    private lateinit var totalGamesPlayedView: TextView
    private lateinit var singlePlayerGamesPlayedView: TextView
    private lateinit var easyGamesWonView: TextView
    private lateinit var intermediateGamesWonView: TextView
    private lateinit var hardGamesWonView: TextView
    private lateinit var multiplayerGamesPlayedView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = GameStatsDialogViewModelFactory(
            (activity?.application as MancalaApplication).repository
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[GameStatsDialogViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.game_stats, container, false)
        totalGamesPlayedView = view.findViewById(R.id.tvTotalGamesPlayed)
        singlePlayerGamesPlayedView = view.findViewById(R.id.tvSinglePlayerGamesPlayed)
        easyGamesWonView = view.findViewById(R.id.tvEasyGamesWon)
        intermediateGamesWonView = view.findViewById(R.id.tvIntermediateGamesWon)
        hardGamesWonView = view.findViewById(R.id.tvHardGamesWon)
        multiplayerGamesPlayedView = view.findViewById(R.id.tvMultiplayerGamesPlayed)

        observeAllGameOutcomes()
        observeSinglePlayerGameOutcomes()
        observeEasyGamesWon()
        observeIntermediateGamesWon()
        observeHardGamesWon()
        observeMultiplayerGameOutcomes()

        return view
    }

    private fun observeAllGameOutcomes() =
        viewModel.allGameOutcomes.observe(this) { allGameOutcomes ->
            totalGamesPlayedView.text =
                if (allGameOutcomes != null) {
                    getString(R.string.total_games_played, allGameOutcomes.size)
                } else {
                    getString(R.string.total_games_played, 0)
                }
    }

    private fun observeSinglePlayerGameOutcomes() =
        viewModel.singlePlayerGameOutcomes.observe(this) { singlePlayerGameOutcomes ->
            singlePlayerGamesPlayedView.text =
                if (singlePlayerGameOutcomes != null) {
                    getString(R.string.single_player_games_played, singlePlayerGameOutcomes.size)
                } else {
                    getString(R.string.single_player_games_played, 0)
                }
        }

    private fun observeEasyGamesWon() =
        viewModel.easyGamesWon.observe(this) { easyGamesWon ->
            easyGamesWonView.text =
                if (easyGamesWon != null) {
                    getString(R.string.easy_games_won, easyGamesWon)
                } else {
                    getString(R.string.easy_games_won, 0)
                }
        }

    private fun observeIntermediateGamesWon() =
        viewModel.intermediateGamesWon.observe(this) { intermediateGamesWon ->
            intermediateGamesWonView.text =
                if (intermediateGamesWon != null) {
                    getString(R.string.intermediate_games_won, intermediateGamesWon)
                } else {
                    getString(R.string.intermediate_games_won, 0)
                }
        }

    private fun observeHardGamesWon() =
        viewModel.hardGamesWon.observe(this) { hardGamesWon ->
            hardGamesWonView.text =
                if (hardGamesWon != null) {
                    getString(R.string.hard_games_won, hardGamesWon)
                } else {
                    getString(R.string.hard_games_won, 0)
                }
        }

    private fun observeMultiplayerGameOutcomes() =
        viewModel.multiplayerGameOutcomes.observe(this) { multiplayerGameOutcomes ->
            multiplayerGamesPlayedView.text =
                if (multiplayerGameOutcomes != null) {
                    getString(R.string.multiplayer_games_played, multiplayerGameOutcomes.size)
                } else {
                    getString(R.string.multiplayer_games_played, 0)
                }
        }
}