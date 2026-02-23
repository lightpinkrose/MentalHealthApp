package com.example.mentalhealthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnCheckIn: Button
    private lateinit var btnHistory: Button
    private lateinit var btnSearch: Button
    private lateinit var btnChangePassword: Button
    private lateinit var btnLogout: Button
    private lateinit var switchReminder: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnCheckIn = findViewById(R.id.btnCheckIn)
        btnHistory = findViewById(R.id.btnHistory)
        btnSearch = findViewById(R.id.btnSearch)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnLogout = findViewById(R.id.btnLogout)
        switchReminder = findViewById(R.id.switchReminder)

        val username = intent.getStringExtra("username")
        tvWelcome.text = "Welcome, $username!"

        btnCheckIn.setOnClickListener {
            startActivity(Intent(this, CheckInActivity::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LaunchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Daily reminder enabled!", Toast.LENGTH_SHORT).show()
                // TODO: Add notification scheduling here
            } else {
                Toast.makeText(this, "Daily reminder disabled.", Toast.LENGTH_SHORT).show()
                // TODO: Cancel scheduled notifications here
            }
        }
    }
}