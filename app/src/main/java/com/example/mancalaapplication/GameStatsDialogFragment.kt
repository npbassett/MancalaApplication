package com.example.mancalaapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        observeEasyGames()
        observeIntermediateGames()
        observeHardGames()
        observeMultiplayerGameOutcomes()

        val btnExit: Button = view.findViewById(R.id.btnExit)
        btnExit.setOnClickListener { dialog?.dismiss() }

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

    private fun observeEasyGames() =
        viewModel.easyGames.observe(this) { (easyGamesTotal, easyGamesWon) ->
            easyGamesWonView.text =
                if (easyGamesTotal != null ) {
                    if (easyGamesWon != null) {
                        val percentage = if (easyGamesTotal == 0) {
                            0.0
                        } else {
                            (easyGamesWon.toDouble() / easyGamesTotal) * 100
                        }
                        getString(R.string.easy_games_won, easyGamesWon, easyGamesTotal, percentage)
                    } else {
                        getString(R.string.easy_games_won, 0, easyGamesTotal, 0.0)
                    }
                } else {
                    getString(R.string.easy_games_won, 0, 0, 0.0)
                }
        }

    private fun observeIntermediateGames() =
        viewModel.intermediateGames.observe(this) {
                (intermediateGamesTotal, intermediateGamesWon) ->
            intermediateGamesWonView.text =
                if (intermediateGamesTotal != null) {
                    if (intermediateGamesWon != null) {
                        val percentage = if (intermediateGamesTotal == 0) {
                            0.0
                        } else {
                            (intermediateGamesWon.toDouble() / intermediateGamesTotal) * 100
                        }
                        getString(
                            R.string.intermediate_games_won,
                            intermediateGamesWon, intermediateGamesTotal, percentage
                        )
                    } else {
                        getString(R.string.intermediate_games_won,
                            0, intermediateGamesTotal, 0.0)
                    }
                } else {
                    getString(R.string.intermediate_games_won, 0, 0, 0.0)
                }
        }

    private fun observeHardGames() =
        viewModel.hardGames.observe(this) { (hardGamesTotal, hardGamesWon) ->
            hardGamesWonView.text =
                if (hardGamesTotal != null ) {
                    if (hardGamesWon != null) {
                        val percentage = if (hardGamesTotal == 0) {
                            0.0
                        } else {
                            (hardGamesWon.toDouble() / hardGamesTotal) * 100
                        }
                        getString(R.string.hard_games_won, hardGamesWon, hardGamesTotal, percentage)
                    } else {
                        getString(R.string.hard_games_won, 0, hardGamesTotal, 0.0)
                    }
                } else {
                    getString(R.string.hard_games_won, 0, 0, 0.0)
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