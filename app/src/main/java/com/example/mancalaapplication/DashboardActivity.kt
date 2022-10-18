package com.example.mancalaapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class DashboardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()

        val btnBeginMultiplayer = findViewById<Button>(R.id.btnBeginMultiplayer)
        btnBeginMultiplayer.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game mode", "multiplayer")
            intent.putExtra("AI difficulty", "")
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
            //TODO: implement intermediate difficulty AI
            Snackbar.make(
                findViewById(R.id.dashboardActivityCoordinatorLayout),
                "Intermediate difficulty not yet implemented.",
                Snackbar.LENGTH_SHORT
                ).setAction(R.string.dismiss) {}
            .show()
        }

        val btnSinglePlayerHard = findViewById<Button>(R.id.btnSinglePlayerHard)
        btnSinglePlayerHard.setOnClickListener {
            //TODO: implement hard difficulty AI
            Snackbar.make(
                findViewById(R.id.dashboardActivityCoordinatorLayout),
                "Hard difficulty not yet implemented.",
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.dismiss) {}
                .show()
        }
    }
}