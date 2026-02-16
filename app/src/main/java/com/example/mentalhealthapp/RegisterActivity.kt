package com.example.mentalhealthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Make sure XML is named activity_register.xml

        db = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password complexity: at least 6 characters, must contain a number
            val passwordPattern = Regex("^(?=.*[0-9]).{6,}\$")
            if (!password.matches(passwordPattern)) {
                Toast.makeText(
                    this,
                    "Password must be at least 6 characters and contain a number",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Add user to the database
            val success = db.addUser(username, password)
            if (success) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                // Go to Login screen after registration
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }
}