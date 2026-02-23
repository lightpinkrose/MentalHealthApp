package com.example.mentalhealthapp

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CheckInActivity : AppCompatActivity() {

    private lateinit var rgMood: RadioGroup
    private lateinit var seekStress: SeekBar
    private lateinit var etNotes: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        // Bind views
        rgMood = findViewById(R.id.rgMood)
        seekStress = findViewById(R.id.seekStress)
        etNotes = findViewById(R.id.etNotes)
        btnSubmit = findViewById(R.id.btnSubmitCheckIn)

        // Optional: load previous saved data
        loadCheckIn()

        // Handle submit button
        btnSubmit.setOnClickListener {
            saveCheckIn()
        }
    }

    private fun saveCheckIn() {
        val selectedMoodId = rgMood.checkedRadioButtonId
        if (selectedMoodId == -1) {
            Toast.makeText(this, "Please select your mood", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedMood = findViewById<RadioButton>(selectedMoodId).text.toString()
        val stressLevel = seekStress.progress
        val notes = etNotes.text.toString()

        // Save to SharedPreferences
        val sharedPref = getSharedPreferences("DailyCheckIn", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("mood", selectedMood)
            putInt("stress", stressLevel)
            putString("notes", notes)
            apply()
        }

        // Give feedback
        Toast.makeText(this, "Check-in saved! ✅", Toast.LENGTH_SHORT).show()

        // Optional: clear inputs after saving
        rgMood.clearCheck()
        seekStress.progress = 0
        etNotes.text.clear()
    }

    private fun loadCheckIn() {
        val sharedPref = getSharedPreferences("DailyCheckIn", Context.MODE_PRIVATE)
        val savedMood = sharedPref.getString("mood", "")
        val savedStress = sharedPref.getInt("stress", 0)
        val savedNotes = sharedPref.getString("notes", "")

        if (!savedMood.isNullOrEmpty()) {
            for (i in 0 until rgMood.childCount) {
                val rb = rgMood.getChildAt(i) as RadioButton
                if (rb.text == savedMood) {
                    rb.isChecked = true
                    break
                }
            }
        }

        seekStress.progress = savedStress
        etNotes.setText(savedNotes)
    }
}