package com.example.mancalaapplication

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mancalaapplication.databinding.MancalaFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    private lateinit var aiDifficulty: String
    private lateinit var viewModel: MancalaSinglePlayerViewModel
    private lateinit var binding: MancalaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // gets the AI difficulty passed from DashboardActivity
        arguments?.getString("AI difficulty")?.let {
            aiDifficulty = it
        }

        // instantiates viewModel, passing aiDifficulty and database repository
        val viewModelFactory = MancalaSinglePlayerViewModelFactory(
            aiDifficulty,
            (activity?.application as MancalaApplication).repository
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MancalaSinglePlayerViewModel::class.java]

        binding = MancalaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Selects image for pocket with correct number of stones
     *
     * @param numStones number of stones in the pocket
     * @return ID of image
     */
    private fun getPocketImage(numStones: Int): Int {
        return if (numStones in 0..14) {
            resources.getIdentifier("pocket_${numStones}_stones",
                "drawable", context?.packageName)
        } else {
            resources.getIdentifier("pocket_14_stones",
                "drawable", context?.packageName)
        }
    }

    /**
     * Selects image for store with correct number of stones
     *
     * @param numStones number of stones in the store
     * @return ID of image
     */
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

    /**
     * Makes a snackbar to alert the player that they selected pocket on wrong side of board
     */
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

    /**
     * Makes a snackbar to alert the player that they selected an empty pocket
     */
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

    /**
     * Makes a snackbar to alert the player that they get to move again
     */
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

    /**
     * When player clicks a button on the board, either makes a snackbar if pocket invalid or
     * performs the move if pocket is valid
     */
    private fun onButtonClick(pocket: Int) {
        if (viewModel.pocketWrongSide(pocket)) {
            wrongSideSnackbar()
        } else if (viewModel.pocketEmpty(pocket)) {
            emptyPocketSnackbar()
        } else {
            viewModel.applyMove(pocket)
            // wait for 2 seconds and disable buttons before executing AI move.
            if (!viewModel.player1Turn.value) {
                disableButtons()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        while (!viewModel.player1Turn.value && !viewModel.gameOver.value) {
                            viewModel.aiMoveStones()
                        }
                        enableButtons()
                    }, 1000
                )
            } else {
                moveAgainSnackbar()
            }
        }
    }

    /**
     * Alerts players to the winner and final score when game is over
     */
    private fun showGameOverDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialog).apply {
            if (viewModel.checkTie()) {
                setTitle(R.string.tie)
                setMessage(R.string.tie_score)
            } else if (viewModel.checkPlayer1Winner()) {
                setTitle(getString(R.string.congratulations))
                setMessage(getString(R.string.you_won, viewModel.boardState.value[6],
                    viewModel.boardState.value[13]))
            } else {
                setTitle(R.string.better_luck)
                setMessage(getString(R.string.mancalabot_won, viewModel.boardState.value[6],
                    viewModel.boardState.value[13]))
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

    /**
     * inserts outcome of game into database
     */
    private fun addGameToDatabase() {
        viewModel.insertGameOutcome(GameOutcome(
            0,
            System.currentTimeMillis().toInt(),
            "single player",
            aiDifficulty,
            viewModel.checkPlayer1Winner()))
    }

    /**
     * reduces saturation of image to 80%
     *
     * @param drawable
     * @return drawable with saturation reduced
     */
    private fun desaturateDrawable(drawable: Drawable): Drawable {
        val grayScaleMatrix = ColorMatrix()
        grayScaleMatrix.setSaturation(0.80.toFloat())
        val newDrawable = drawable.mutate()
        newDrawable.colorFilter = ColorMatrixColorFilter(grayScaleMatrix)
        return newDrawable
    }

    /**
     * Disables all buttons (to be used while AI opponent is moving)
     */
    private fun disableButtons() {
        binding.btnPocket0.isEnabled = false
        binding.btnPocket0.background = desaturateDrawable(binding.btnPocket0.background)
        binding.btnPocket1.isEnabled = false
        binding.btnPocket1.background = desaturateDrawable(binding.btnPocket1.background)
        binding.btnPocket2.isEnabled = false
        binding.btnPocket2.background = desaturateDrawable(binding.btnPocket2.background)
        binding.btnPocket3.isEnabled = false
        binding.btnPocket3.background = desaturateDrawable(binding.btnPocket3.background)
        binding.btnPocket4.isEnabled = false
        binding.btnPocket4.background = desaturateDrawable(binding.btnPocket4.background)
        binding.btnPocket5.isEnabled = false
        binding.btnPocket5.background = desaturateDrawable(binding.btnPocket5.background)
        binding.btnPocket6.background = desaturateDrawable(binding.btnPocket6.background)
        binding.btnPocket7.isEnabled = false
        binding.btnPocket7.background = desaturateDrawable(binding.btnPocket7.background)
        binding.btnPocket8.isEnabled = false
        binding.btnPocket8.background = desaturateDrawable(binding.btnPocket8.background)
        binding.btnPocket9.isEnabled = false
        binding.btnPocket9.background = desaturateDrawable(binding.btnPocket9.background)
        binding.btnPocket10.isEnabled = false
        binding.btnPocket10.background = desaturateDrawable(binding.btnPocket10.background)
        binding.btnPocket11.isEnabled = false
        binding.btnPocket11.background = desaturateDrawable(binding.btnPocket11.background)
        binding.btnPocket12.isEnabled = false
        binding.btnPocket12.background = desaturateDrawable(binding.btnPocket12.background)
        binding.btnPocket13.background = desaturateDrawable(binding.btnPocket13.background)
    }

    /**
     * Enables all buttons
     */
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

    /**
     * Uses StateFlows to update UI when values change
     */
    private fun subscribeToObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.player1Turn.collectLatest {
                        binding.tvPlayersTurn.text = if (it) "Your turn" else "MancalaBot's turn"
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
                        if (it) {
                            addGameToDatabase()
                            showGameOverDialog()
                        }
                    }
                }
            }
        }
    }
}