package com.example.mancalaapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()

        val btnBeginMultiplayer = findViewById<Button>(R.id.btnBeginMultiplayer)
        btnBeginMultiplayer.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}