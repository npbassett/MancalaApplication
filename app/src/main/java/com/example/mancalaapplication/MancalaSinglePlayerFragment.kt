package com.example.mancalaapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mancalaapplication.databinding.MancalaFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MancalaSinglePlayerFragment : Fragment(R.layout.mancala_fragment) {

    companion object {
        fun newInstance(aiDifficulty: String): MancalaSinglePlayerFragment {
            val fragment = MancalaSinglePlayerFragment()
            val args = Bundle()
            args.putString("AI difficulty", aiDifficulty)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: MancalaSinglePlayerViewModel by viewModels()

    private lateinit var aiDifficulty: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("AI difficulty")?.let {
            aiDifficulty = it
        }
        viewModel.aiDifficulty = aiDifficulty
    }

    private lateinit var binding: MancalaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = MancalaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updateDisplay() {
        binding.tvPlayersTurn.text = if (viewModel.player1Turn) "Your turn"
        else "MancalaBot's turn"
        binding.tvPocket0.text = getString(R.string.stones, viewModel.pocketStones[0])
        binding.tvPocket1.text = getString(R.string.stones, viewModel.pocketStones[1])
        binding.tvPocket2.text = getString(R.string.stones, viewModel.pocketStones[2])
        binding.tvPocket3.text = getString(R.string.stones, viewModel.pocketStones[3])
        binding.tvPocket4.text = getString(R.string.stones, viewModel.pocketStones[4])
        binding.tvPocket5.text = getString(R.string.stones, viewModel.pocketStones[5])
        binding.tvPocket6.text = getString(R.string.stones, viewModel.pocketStones[6])
        binding.tvPocket7.text = getString(R.string.stones, viewModel.pocketStones[7])
        binding.tvPocket8.text = getString(R.string.stones, viewModel.pocketStones[8])
        binding.tvPocket9.text = getString(R.string.stones, viewModel.pocketStones[9])
        binding.tvPocket10.text = getString(R.string.stones, viewModel.pocketStones[10])
        binding.tvPocket11.text = getString(R.string.stones, viewModel.pocketStones[11])
        binding.tvPocket12.text = getString(R.string.stones, viewModel.pocketStones[12])
        binding.tvPocket13.text = getString(R.string.stones, viewModel.pocketStones[13])
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
        if (viewModel.pocketWrongSide(pocket)) {
            wrongSideSnackbar()
        } else if (viewModel.pocketEmpty(pocket)) {
            emptyPocketSnackbar()
        } else {
            viewModel.moveStones(pocket)
            updateDisplay()
            if (viewModel.gameOver) showWinnerDialog()
            // wait for 2 seconds and disable buttons before executing AI move.
            if (!viewModel.player1Turn) {
                disableButtons()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        while (!viewModel.player1Turn && !viewModel.gameOver) {
                            viewModel.aiMoveStones()
                            updateDisplay()
                            if (viewModel.gameOver) showWinnerDialog()
                        }
                        enableButtons()
                    }, 2000
                )
            } else {
                moveAgainSnackbar()
            }
        }
    }

    private fun showWinnerDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            if (viewModel.checkPlayer1Winner()) {
                setTitle(getString(R.string.congratulations))
                setMessage(R.string.you_win)
            } else {
                setTitle(R.string.better_luck)
                setMessage(R.string.mancalabot_won)
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

    private fun disableButtons() {
        binding.btnPocket0.isEnabled = false
        binding.btnPocket1.isEnabled = false
        binding.btnPocket2.isEnabled = false
        binding.btnPocket3.isEnabled = false
        binding.btnPocket4.isEnabled = false
        binding.btnPocket5.isEnabled = false
        binding.btnPocket7.isEnabled = false
        binding.btnPocket8.isEnabled = false
        binding.btnPocket9.isEnabled = false
        binding.btnPocket10.isEnabled = false
        binding.btnPocket11.isEnabled = false
        binding.btnPocket12.isEnabled = false

    }

    private fun enableButtons() {
        binding.btnPocket0.isEnabled = true
        binding.btnPocket1.isEnabled = true
        binding.btnPocket2.isEnabled = true
        binding.btnPocket3.isEnabled = true
        binding.btnPocket4.isEnabled = true
        binding.btnPocket5.isEnabled = true
        binding.btnPocket7.isEnabled = true
        binding.btnPocket8.isEnabled = true
        binding.btnPocket9.isEnabled = true
        binding.btnPocket10.isEnabled = true
        binding.btnPocket11.isEnabled = true
        binding.btnPocket12.isEnabled = true
    }
}