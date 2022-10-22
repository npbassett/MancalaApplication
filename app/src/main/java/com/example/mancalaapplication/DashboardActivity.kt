package com.example.mancalaapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()

        val btnRules = findViewById<ImageButton>(R.id.btnRules)
        btnRules.setOnClickListener {
            showRules()
        }

        val btnBeginMultiplayer = findViewById<Button>(R.id.btnBeginMultiplayer)
        btnBeginMultiplayer.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game mode", "multiplayer")
            startActivity(intent)
        }

        val btnSinglePlayerEasy = findViewById<Button>(R.id.btnSinglePlayerEasy)
        btnSinglePlayerEasy.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game mode", "single player")
            intent.putExtra("AI difficulty", "easy")
            startActivity(intent)
        }

        val btnSinglePlayerIntermediate = findViewById<Button>(R.id.btnSinglePlayerIntermediate)
        btnSinglePlayerIntermediate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game mode", "single player")
            intent.putExtra("AI difficulty", "intermediate")
            startActivity(intent)
        }

        val btnSinglePlayerHard = findViewById<Button>(R.id.btnSinglePlayerHard)
        btnSinglePlayerHard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game mode", "single player")
            intent.putExtra("AI difficulty", "hard")
            startActivity(intent)
        }
    }

    private fun showRules() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle("Mancala Rules")
            .setMessage("Set up:\n" +
                    "The board is made up of two rows of six pockets along with two stores, one" +
                    " for each player on their right-hand side of the board. To begin the game," +
                    " four stones are placed in each of the 12 pockets.\n\n" +
                    "Game play:\n" +
                    "1. Player one picks up all the stones from one of the pockets on their" +
                    " side.\n 2. Player places one stone in each pocket in a counter-clockwise" +
                    " direction. If the player reaches their own store, they place a stone in it." +
                    " If they reach their opponent's store, it is skipped.\n3. If the player's" +
                    " last stone is placed in their own store, the player gets another turn.\n" +
                    "4. If the player's last stone is placed in an empty pocket on their own" +
                    " side, any stones in the opposite pocket are captured and placed in the" +
                    " player's store.\n\nEnding the Game:\n"+
                    "When all of the pockets on one side of the board are empty, the game is" +
                    " over. If a player still has stones on their side of the board when the" +
                    " game ends, all of the remaining stones are moved to that player's store." +
                    " The player with the most stones in their store wins!")
            .setNegativeButton(R.string.lets_play, null)
            .show()
    }
}