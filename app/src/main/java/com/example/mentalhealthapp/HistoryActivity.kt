package com.example.mentalhealthapp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // ✅ Back arrow in top bar
        supportActionBar?.apply {
            title = "History"
            setDisplayHomeAsUpEnabled(true)
        }

        val tvHistory = findViewById<TextView>(R.id.tvHistory)

        val prefs = getSharedPreferences("DailyCheckInHistory", Context.MODE_PRIVATE)
        val date = prefs.getString("date", null)
        val mood = prefs.getString("mood", null)
        val stress = prefs.getInt("stress", -1)
        val notes = prefs.getString("notes", "")

        tvHistory.text =
            if (date == null || mood == null || stress == -1) {
                "No check-ins submitted yet."
            } else {
                "Last Check-In:\n\n" +
                        "Date: $date\n" +
                        "Mood: $mood\n" +
                        "Stress: $stress/10\n\n" +
                        "Notes:\n${if (notes.isNullOrBlank()) "None" else notes}"
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}