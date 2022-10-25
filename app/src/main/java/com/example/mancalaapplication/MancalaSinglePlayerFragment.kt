package com.example.mancalaapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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
        binding.tvPlayersTurn.text = if (viewModel.player1Turn) "Your turn"
        else "MancalaBot's turn"
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
            viewModel.applyMove(pocket)
            updateDisplay()
            if (viewModel.gameOver) showGameOverDialog()
            // wait for 2 seconds and disable buttons before executing AI move.
            if (!viewModel.player1Turn) {
                disableButtons()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        while (!viewModel.player1Turn && !viewModel.gameOver) {
                            viewModel.aiMoveStones()
                            updateDisplay()
                            if (viewModel.gameOver) showGameOverDialog()
                        }
                        enableButtons()
                    }, 1000
                )
            } else {
                moveAgainSnackbar()
            }
        }
    }

    private fun showGameOverDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog).apply {
            if (viewModel.checkTie()) {
                setTitle(R.string.tie)
                setMessage(R.string.tie_score)
            } else if (viewModel.checkPlayer1Winner()) {
                setTitle(getString(R.string.congratulations))
                setMessage(getString(R.string.you_won, viewModel.boardState[6],
                    viewModel.boardState[13]))
            } else {
                setTitle(R.string.better_luck)
                setMessage(getString(R.string.mancalabot_won, viewModel.boardState[6],
                    viewModel.boardState[13]))
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