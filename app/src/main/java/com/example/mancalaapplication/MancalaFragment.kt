package com.example.mancalaapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mancalaapplication.databinding.MancalaFragmentBinding

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

    private fun oppositePocket(pocket: Int): Int? {
        return if (pocket != 6 && pocket != 13) {
            12 - pocket
        } else {
            null
        }
    }

    private fun moveStones(pocket: Int) {
        val playersStore = if (mancalaModel.player1Turn) 6 else 13
        val playersPockets = if (mancalaModel.player1Turn) {
            mutableListOf(0, 1, 2, 3, 4, 5)
        } else {
            mutableListOf(7, 8, 9, 10, 11, 12)
        }
        var numStonesToMove: Int = mancalaModel.pocketStones[pocket]
        mancalaModel.pocketStones[pocket] = 0
        var currentPocket = pocket + 1
        // distribute stones into pockets
        while (numStonesToMove > 0) {
            mancalaModel.pocketStones[currentPocket]++
            currentPocket = (currentPocket + 1) % 14
            numStonesToMove--
        }
        Log.d("MancalaFragment", "$currentPocket")
        val lastPocket = if (currentPocket == 0) 13 else currentPocket - 1
        Log.d("MancalaFragment", "$lastPocket")
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
    }
}