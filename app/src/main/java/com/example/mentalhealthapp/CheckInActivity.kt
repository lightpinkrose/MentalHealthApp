package com.example.mentalhealthapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CheckInActivity : AppCompatActivity() {

    private lateinit var rgMood: RadioGroup
    private lateinit var seekStress: SeekBar
    private lateinit var etNotes: EditText
    private lateinit var btnSubmit: Button

    private val draftPrefsName = "DailyCheckInDraft"
    private val statusPrefsName = "DailyCheckInStatus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        // ✅ Enable ActionBar back button
        supportActionBar?.apply {
            title = "Daily Check-In"
            setDisplayHomeAsUpEnabled(true)
        }

        rgMood = findViewById(R.id.rgMood)
        seekStress = findViewById(R.id.seekStress)
        etNotes = findViewById(R.id.etNotes)
        btnSubmit = findViewById(R.id.btnSubmitCheckIn)

        if (isSubmittedToday()) {
            clearForm()
            clearDraft()
        } else {
            loadDraftIfExists()
        }

        rgMood.setOnCheckedChangeListener { _, _ -> saveDraft() }

        seekStress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) saveDraft()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etNotes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { saveDraft() }
        })

        btnSubmit.setOnClickListener {
            submitCheckIn()
        }
    }

    // ✅ Handle back arrow click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitCheckIn() {
        val selectedMoodId = rgMood.checkedRadioButtonId
        if (selectedMoodId == -1) {
            Toast.makeText(this, "Please select your mood", Toast.LENGTH_SHORT).show()
            return
        }

        val mood = findViewById<RadioButton>(selectedMoodId).text.toString()
        val stress = seekStress.progress
        val notes = etNotes.text.toString()

        // ✅ Save LAST submitted check-in so History can display it
        val historyPrefs = getSharedPreferences("DailyCheckInHistory", Context.MODE_PRIVATE)
        historyPrefs.edit()
            .putString("date", todayKey())
            .putString("mood", mood)
            .putInt("stress", stress)
            .putString("notes", notes)
            .apply()

        markSubmittedToday()
        clearDraft()
        clearForm()

        Toast.makeText(this, "Check-in submitted! ✅", Toast.LENGTH_SHORT).show()
    }

    private fun saveDraft() {
        if (isSubmittedToday()) return

        val draftPrefs = getSharedPreferences(draftPrefsName, Context.MODE_PRIVATE)

        val selectedMoodId = rgMood.checkedRadioButtonId
        val moodText = if (selectedMoodId != -1) {
            findViewById<RadioButton>(selectedMoodId).text.toString()
        } else ""

        draftPrefs.edit()
            .putString("date", todayKey())
            .putString("mood", moodText)
            .putInt("stress", seekStress.progress)
            .putString("notes", etNotes.text.toString())
            .apply()
    }

    private fun loadDraftIfExists() {
        val draftPrefs = getSharedPreferences(draftPrefsName, Context.MODE_PRIVATE)
        val draftDate = draftPrefs.getString("date", null)

        if (draftDate != todayKey()) {
            clearDraft()
            clearForm()
            return
        }

        val savedMood = draftPrefs.getString("mood", "") ?: ""
        val savedStress = draftPrefs.getInt("stress", 0)
        val savedNotes = draftPrefs.getString("notes", "") ?: ""

        if (savedMood.isNotEmpty()) {
            for (i in 0 until rgMood.childCount) {
                val view = rgMood.getChildAt(i)
                if (view is RadioButton && view.text.toString() == savedMood) {
                    view.isChecked = true
                    break
                }
            }
        }

        seekStress.progress = savedStress
        etNotes.setText(savedNotes)
    }

    private fun clearDraft() {
        getSharedPreferences(draftPrefsName, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }

    private fun clearForm() {
        rgMood.clearCheck()
        seekStress.progress = 0
        etNotes.setText("")
    }

    private fun isSubmittedToday(): Boolean {
        val statusPrefs = getSharedPreferences(statusPrefsName, Context.MODE_PRIVATE)
        return statusPrefs.getString("submitted_date", null) == todayKey()
    }

    private fun markSubmittedToday() {
        val statusPrefs = getSharedPreferences(statusPrefsName, Context.MODE_PRIVATE)
        statusPrefs.edit()
            .putString("submitted_date", todayKey())
            .apply()
    }

    private fun todayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
}