package com.example.mancalaapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mancalaapplication.databinding.MancalaFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MancalaMultiplayerFragment : Fragment(R.layout.mancala_fragment) {

    private val viewModel: MancalaMultiplayerViewModel by viewModels()

    private lateinit var binding: MancalaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = MancalaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updateDisplay() {
        binding.tvPlayersTurn.text = if (viewModel.player1Turn) "Player 1's turn"
            else "Player 2's turn"
        binding.tvPocket0.text = getString(R.string.stones, viewModel.boardState[0])
        binding.tvPocket1.text = getString(R.string.stones, viewModel.boardState[1])
        binding.tvPocket2.text = getString(R.string.stones, viewModel.boardState[2])
        binding.tvPocket3.text = getString(R.string.stones, viewModel.boardState[3])
        binding.tvPocket4.text = getString(R.string.stones, viewModel.boardState[4])
        binding.tvPocket5.text = getString(R.string.stones, viewModel.boardState[5])
        binding.tvPocket6.text = getString(R.string.stones, viewModel.boardState[6])
        binding.tvPocket7.text = getString(R.string.stones, viewModel.boardState[7])
        binding.tvPocket8.text = getString(R.string.stones, viewModel.boardState[8])
        binding.tvPocket9.text = getString(R.string.stones, viewModel.boardState[9])
        binding.tvPocket10.text = getString(R.string.stones, viewModel.boardState[10])
        binding.tvPocket11.text = getString(R.string.stones, viewModel.boardState[11])
        binding.tvPocket12.text = getString(R.string.stones, viewModel.boardState[12])
        binding.tvPocket13.text = getString(R.string.stones, viewModel.boardState[13])
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Update UI to reflect initial number of stones in each pocket.
        updateDisplay()
        // set up click listener on each button
        binding.btnPocket0.setOnClickListener { onButtonClick(0) }
        binding.btnPocket1.setOnClickListener { onButtonClick(1) }
        binding.btnPocket2.setOnClickListener { onButtonClick(2) }
        binding.btnPocket3.setOnClickListener { onButtonClick(3) }
        binding.btnPocket4.setOnClickListener { onButtonClick(4) }
        binding.btnPocket5.setOnClickListener { onButtonClick(5) }
        binding.btnPocket7.setOnClickListener { onButtonClick(7) }
        binding.btnPocket8.setOnClickListener { onButtonClick(8) }
        binding.btnPocket9.setOnClickListener { onButtonClick(9) }
        binding.btnPocket10.setOnClickListener { onButtonClick(10) }
        binding.btnPocket11.setOnClickListener { onButtonClick(11) }
        binding.btnPocket12.setOnClickListener { onButtonClick(12) }
    }

    private fun wrongSideSnackbar() {
        Log.d("Multiplayer", "wrong side snackbar")
        activity?.let { it ->
            Snackbar.make(
                it.findViewById(R.id.mainActivityCoordinatorLayout),
                R.string.pocket_wrong_side_snackbar,
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.dismiss) {}
        }?.show()
        return
    }

    private fun emptyPocketSnackbar() {
        activity?.let { it ->
            Snackbar.make(
                it.findViewById(R.id.mainActivityCoordinatorLayout),
                R.string.pocket_empty_snackbar,
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.dismiss) {}
        }?.show()
        return
    }

    private fun moveAgainSnackbar() {
        Log.d("Multiplayer", "move again snackbar")
        activity?.let { it ->
            Snackbar.make(
                it.findViewById(R.id.mainActivityCoordinatorLayout),
                R.string.move_again_snackbar,
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.dismiss) {}
        }?.show()
        return
    }

    private fun onButtonClick(pocket: Int) {
        val beforePlayer1Turn = viewModel.player1Turn
        if (viewModel.pocketWrongSide(pocket)) {
            wrongSideSnackbar()
            return
        } else if (viewModel.pocketEmpty(pocket)) {
            emptyPocketSnackbar()
            return
        } else {
            viewModel.moveStones(pocket)
            updateDisplay()
            if (viewModel.gameOver) showGameOverDialog()
        }
        if (beforePlayer1Turn == viewModel.player1Turn) moveAgainSnackbar()
    }

    private fun showGameOverDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog).apply {
            if (viewModel.checkTie()) {
                setTitle(R.string.tie)
                setMessage(R.string.tie_score)
            } else {
                setTitle(getString(R.string.congratulations))
                setMessage(
                    getString(
                        R.string.winner,
                        if (viewModel.checkPlayer1Winner()) "Player 1" else "Player 2",
                        viewModel.boardState[6], viewModel.boardState[13]
                    )
                )
            }
            setCancelable(false)
            setPositiveButton(getString(R.string.play_again)) { _, _ ->
                run {
                    viewModel.restartGame()
                    updateDisplay()
                }
            }
            setNegativeButton(R.string.exit) { _, _ ->
                startActivity(Intent(activity, DashboardActivity::class.java))
            }
            show()
        }
    }
}