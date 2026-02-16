package com.example.mentalhealthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseHelper(this)

        val usernameField = findViewById<EditText>(R.id.etUsername)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val registerLink = findViewById<TextView>(R.id.tvRegisterLink)

        loginBtn.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (db.checkUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                // Redirect to main screen
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}