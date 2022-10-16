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
            currentPocket = (currentPocket + 1) % 13
            numStonesToMove--
        }
        val lastPocket = currentPocket - 1
        // check if last stone ends in empty pocket on player's own side. If so, move all stones
        // in opposite pocket to player's store.
        val lastPocketOpposite = oppositePocket(lastPocket)
        if (lastPocket in playersPockets && mancalaModel.pocketStones[lastPocket] == 1
            && lastPocketOpposite != null) {
            mancalaModel.pocketStones[playersStore] += mancalaModel.pocketStones[lastPocketOpposite]
            mancalaModel.pocketStones[lastPocketOpposite] = 0
        }
        Log.d("MancalaFragment", "$currentPocket")
        Log.d("MancalaFragment", "$playersStore")
        // check if last stone ends in player's store. If so player gets another turn
        mancalaModel.player1Turn = if (lastPocket != playersStore) {
            !mancalaModel.player1Turn
        } else mancalaModel.player1Turn
    }
}