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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mancalaapplication.databinding.MancalaFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        return if (numStones in 0..14) {
            resources.getIdentifier("pocket_${numStones}_stones",
                "drawable", context?.packageName)
        } else {
            resources.getIdentifier("pocket_35_stones",
                "drawable", context?.packageName)
        }
    }

    private fun getStoreImage(numStones: Int): Int {
        return if (numStones in 0..35) {
            resources.getIdentifier("store_${numStones}_stones",
                "drawable", context?.packageName)
        } else {
            resources.getIdentifier("store_35_stones",
                "drawable", context?.packageName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservables()
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
        val beforePlayer1Turn = viewModel.player1Turn.value
        if (viewModel.pocketWrongSide(pocket)) {
            wrongSideSnackbar()
        } else if (viewModel.pocketEmpty(pocket)) {
            emptyPocketSnackbar()
        } else {
            viewModel.applyMove(pocket)
            if (beforePlayer1Turn == viewModel.player1Turn.value) moveAgainSnackbar()
        }
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
                        viewModel.boardState.value[6], viewModel.boardState.value[13]
                    )
                )
            }
            setCancelable(false)
            setPositiveButton(getString(R.string.play_again)) { _, _ ->
                run {
                    viewModel.restartGame()
                }
            }
            setNegativeButton(R.string.exit) { _, _ ->
                startActivity(Intent(activity, DashboardActivity::class.java))
            }
            show()
        }
    }

    private fun subscribeToObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.player1Turn.collectLatest {
                        binding.tvPlayersTurn.text = if (it) "Player 1's turn"
                            else "Player 2's turn"
                    }
                }
                launch {
                    viewModel.boardState.collectLatest {
                        binding.tvPocket0.text = getString(R.string.stones, it[0])
                        binding.btnPocket0.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[0]), null)
                        binding.tvPocket1.text = getString(R.string.stones, it[1])
                        binding.btnPocket1.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[1]), null)
                        binding.tvPocket2.text = getString(R.string.stones, it[2])
                        binding.btnPocket2.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[2]), null)
                        binding.tvPocket3.text = getString(R.string.stones, it[3])
                        binding.btnPocket3.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[3]), null)
                        binding.tvPocket4.text = getString(R.string.stones, it[4])
                        binding.btnPocket4.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[4]), null)
                        binding.tvPocket5.text = getString(R.string.stones, it[5])
                        binding.btnPocket5.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[5]), null)
                        binding.tvPocket6.text = getString(R.string.stones, it[6])
                        binding.btnPocket6.background = ResourcesCompat.getDrawable(
                            resources, getStoreImage(it[6]), null)
                        binding.tvPocket7.text = getString(R.string.stones, it[7])
                        binding.btnPocket7.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[7]), null)
                        binding.tvPocket8.text = getString(R.string.stones, it[8])
                        binding.btnPocket8.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[8]), null)
                        binding.tvPocket9.text = getString(R.string.stones, it[9])
                        binding.btnPocket9.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[9]), null)
                        binding.tvPocket10.text = getString(R.string.stones, it[10])
                        binding.btnPocket10.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[10]), null)
                        binding.tvPocket11.text = getString(R.string.stones, it[11])
                        binding.btnPocket11.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[11]), null)
                        binding.tvPocket12.text = getString(R.string.stones, it[12])
                        binding.btnPocket12.background = ResourcesCompat.getDrawable(
                            resources, getPocketImage(it[12]), null)
                        binding.tvPocket13.text = getString(R.string.stones, it[13])
                        binding.btnPocket13.background = ResourcesCompat.getDrawable(
                            resources, getStoreImage(it[13]), null)
                    }
                }
                launch {
                    viewModel.gameOver.collectLatest {
                        if (it) showGameOverDialog()
                    }
                }
            }
        }
    }
}