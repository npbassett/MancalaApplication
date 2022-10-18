package com.example.mancalaapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val gameMode = intent.getStringExtra("game mode").toString()
        val aiDifficulty = intent.getStringExtra("AI difficulty").toString()

        val fragment = if (gameMode == "multiplayer") MancalaMultiplayerFragment()
            else MancalaSinglePlayerFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mancala_fragment_container, fragment)
            commit()
        }
    }
}