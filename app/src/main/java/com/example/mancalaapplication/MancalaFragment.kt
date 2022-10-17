package com.example.mancalaapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mancalaapplication.databinding.MancalaFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MancalaFragment : Fragment(R.layout.mancala_fragment) {

    private var mancalaModel = MancalaModel()

    private lateinit var binding: MancalaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = MancalaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Update UI to reflect initial number of stones in each pocket.
        updateDisplay()
        // set up click listener on each button
        binding.btnPocket0.setOnClickListener { moveStones(0) }
        binding.btnPocket1.setOnClickListener { moveStones(1) }
        binding.btnPocket2.setOnClickListener { moveStones(2) }
        binding.btnPocket3.setOnClickListener { moveStones(3) }
        binding.btnPocket4.setOnClickListener { moveStones(4) }
        binding.btnPocket5.setOnClickListener { moveStones(5) }
        binding.btnPocket7.setOnClickListener { moveStones(7) }
        binding.btnPocket8.setOnClickListener { moveStones(8) }
        binding.btnPocket9.setOnClickListener { moveStones(9) }
        binding.btnPocket10.setOnClickListener { moveStones(10) }
        binding.btnPocket11.setOnClickListener { moveStones(11) }
        binding.btnPocket12.setOnClickListener { moveStones(12) }
    }

    private fun updateDisplay() {
        binding.tvPlayersTurn.text = if (mancalaModel.player1Turn) "Player 1's turn"
            else "Player 2's turn"
        binding.tvPocket0.text = getString(R.string.stones, mancalaModel.pocketStones[0])
        binding.tvPocket1.text = getString(R.string.stones, mancalaModel.pocketStones[1])
        binding.tvPocket2.text = getString(R.string.stones, mancalaModel.pocketStones[2])
        binding.tvPocket3.text = getString(R.string.stones, mancalaModel.pocketStones[3])
        binding.tvPocket4.text = getString(R.string.stones, mancalaModel.pocketStones[4])
        binding.tvPocket5.text = getString(R.string.stones, mancalaModel.pocketStones[5])
        binding.tvPocket6.text = getString(R.string.stones, mancalaModel.pocketStones[6])
        binding.tvPocket7.text = getString(R.string.stones, mancalaModel.pocketStones[7])
        binding.tvPocket8.text = getString(R.string.stones, mancalaModel.pocketStones[8])
        binding.tvPocket9.text = getString(R.string.stones, mancalaModel.pocketStones[9])
        binding.tvPocket10.text = getString(R.string.stones, mancalaModel.pocketStones[10])
        binding.tvPocket11.text = getString(R.string.stones, mancalaModel.pocketStones[11])
        binding.tvPocket12.text = getString(R.string.stones, mancalaModel.pocketStones[12])
        binding.tvPocket13.text = getString(R.string.stones, mancalaModel.pocketStones[13])
    }

    private fun checkBottomEmpty(): Boolean {
        for (i in 0..5) {
            if (mancalaModel.pocketStones[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkTopEmpty(): Boolean {
        for (i in 7..12) {
            if (mancalaModel.pocketStones[i] != 0) {
                return false
            }
        }
        return true
    }

    private fun checkPlayer1Winner(): Boolean {
        return mancalaModel.pocketStones[6] > mancalaModel.pocketStones[13]
    }

    private fun restartGame() {
        mancalaModel = MancalaModel()
        updateDisplay()
    }

    private fun showWinnerDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.winner, if (checkPlayer1Winner()) "Player 1" else "Player2"))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .show()
    }

    private fun moveStones(pocket: Int) {
        val playersStore = if (mancalaModel.player1Turn) 6 else 13
        val otherPlayersStore = if (mancalaModel.player1Turn) 13 else 6
        val playersPockets = if (mancalaModel.player1Turn) {
            mutableListOf(0, 1, 2, 3, 4, 5)
        } else {
            mutableListOf(7, 8, 9, 10, 11, 12)
        }
        val otherplayersPockets = if (mancalaModel.player1Turn) {
            mutableListOf(7, 8, 9, 10, 11, 12)
        } else {
            mutableListOf(0, 1, 2, 3, 4, 5)
        }
        // if selected pocket is on the wrong side of the board, prompt user ot pick another pocket
        if (pocket in otherplayersPockets) {
            activity?.let { it ->
                Snackbar.make(
                    it.findViewById(R.id.coordinatorLayout),
                    R.string.wrong_side_snackbar,
                    Snackbar.LENGTH_SHORT
                ).setAction(R.string.dismiss) {}
            }?.show()
            return
        }
        // if selected pocket is empty, prompt user to pick another pocket
        if (mancalaModel.pocketStones[pocket] == 0) {
            activity?.let { it ->
                Snackbar.make(
                    it.findViewById(R.id.coordinatorLayout),
                    R.string.pocket_empty_snackbar,
                    Snackbar.LENGTH_SHORT
                ).setAction(R.string.dismiss) {}
            }?.show()
            return
        }
        var numStonesToMove: Int = mancalaModel.pocketStones[pocket]
        mancalaModel.pocketStones[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            if (currentPocket == otherPlayersStore) {
                currentPocket = (currentPocket + 1) % 14
            }
            mancalaModel.pocketStones[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && mancalaModel.pocketStones[lastPocket] == 1
            && lastPocketOpposite != null) {
            mancalaModel.pocketStones[playersStore] += mancalaModel.pocketStones[lastPocketOpposite]
            mancalaModel.pocketStones[lastPocketOpposite] = 0
        }
        // check if last stone ends in player's store. If so player gets another turn
        mancalaModel.player1Turn = if (lastPocket != playersStore) {
            !mancalaModel.player1Turn
        } else mancalaModel.player1Turn
        updateDisplay()
        if (checkTopEmpty() || checkBottomEmpty()) {
            for (i in 0..5) {
                mancalaModel.pocketStones[6] += mancalaModel.pocketStones[i]
                mancalaModel.pocketStones[i] = 0
            }
            for (i in 7..12) {
                mancalaModel.pocketStones[13] += mancalaModel.pocketStones[i]
                mancalaModel.pocketStones[i] = 0
            }
            mancalaModel.gameOver = true
            updateDisplay()
        }
        if (mancalaModel.gameOver) {
            showWinnerDialog()
        }
    }

    private fun oppositePocket(pocket: Int): Int? {
        return if (pocket == 6 || pocket == 13) {
            null
        } else {
            12 - pocket
        }
    }
}