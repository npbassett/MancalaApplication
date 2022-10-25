package com.example.mancalaapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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

    private fun getPocketImage(numStones: Int): Int {
        when (numStones) {
            0 -> return R.drawable.pocket_0_stones
            1 -> return R.drawable.pocket_1_stones
            2 -> return R.drawable.pocket_2_stones
            3 -> return R.drawable.pocket_3_stones
            4 -> return R.drawable.pocket_4_stones
            5 -> return R.drawable.pocket_5_stones
            6 -> return R.drawable.pocket_6_stones
            7 -> return R.drawable.pocket_7_stones
            8 -> return R.drawable.pocket_8_stones
            9 -> return R.drawable.pocket_9_stones
            10 -> return R.drawable.pocket_10_stones
            11 -> return R.drawable.pocket_11_stones
            12 -> return R.drawable.pocket_12_stones
            13 -> return R.drawable.pocket_13_stones
            14 -> return R.drawable.pocket_14_stones
            else -> return R.drawable.pocket_14_stones
        }
    }

    private fun updateDisplay() {
        binding.tvPlayersTurn.text = if (viewModel.player1Turn) "Player 1's turn"
            else "Player 2's turn"
        binding.tvPocket0.text = getString(R.string.stones, viewModel.boardState[0])
        binding.btnPocket0.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[0]), null)
        binding.tvPocket1.text = getString(R.string.stones, viewModel.boardState[1])
        binding.btnPocket1.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[1]), null)
        binding.tvPocket2.text = getString(R.string.stones, viewModel.boardState[2])
        binding.btnPocket2.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[2]), null)
        binding.tvPocket3.text = getString(R.string.stones, viewModel.boardState[3])
        binding.btnPocket3.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[3]), null)
        binding.tvPocket4.text = getString(R.string.stones, viewModel.boardState[4])
        binding.btnPocket4.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[4]), null)
        binding.tvPocket5.text = getString(R.string.stones, viewModel.boardState[5])
        binding.btnPocket5.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[5]), null)
        binding.tvPocket6.text = getString(R.string.stones, viewModel.boardState[6])
        binding.btnPocket6.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[6]), null)
        binding.tvPocket7.text = getString(R.string.stones, viewModel.boardState[7])
        binding.btnPocket7.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[7]), null)
        binding.tvPocket8.text = getString(R.string.stones, viewModel.boardState[8])
        binding.btnPocket8.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[8]), null)
        binding.tvPocket9.text = getString(R.string.stones, viewModel.boardState[9])
        binding.btnPocket9.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[9]), null)
        binding.tvPocket10.text = getString(R.string.stones, viewModel.boardState[10])
        binding.btnPocket10.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[10]), null)
        binding.tvPocket11.text = getString(R.string.stones, viewModel.boardState[11])
        binding.btnPocket11.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[11]), null)
        binding.tvPocket12.text = getString(R.string.stones, viewModel.boardState[12])
        binding.btnPocket12.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[12]), null)
        binding.tvPocket13.text = getString(R.string.stones, viewModel.boardState[13])
        binding.btnPocket13.background = ResourcesCompat.getDrawable(resources,
            getPocketImage(viewModel.boardState[13]), null)
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