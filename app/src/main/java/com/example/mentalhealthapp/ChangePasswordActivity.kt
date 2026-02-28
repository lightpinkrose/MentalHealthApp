package com.example.mentalhealthapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var etUsername: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnChange: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Enable back arrow in top bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Change Password"

        db = DatabaseHelper(this)

        etUsername = findViewById(R.id.etUsername)
        etOldPassword = findViewById(R.id.etOldPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        btnChange = findViewById(R.id.btnChangePassword)

        btnChange.setOnClickListener {
            handlePasswordChange()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun handlePasswordChange() {
        val username = etUsername.text.toString().trim()
        val oldPass = etOldPassword.text.toString().trim()
        val newPass = etNewPassword.text.toString().trim()

        // Basic validation
        if (username.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Extra security requirement
        val passwordPattern = Regex("^(?=.*[0-9]).{6,}$")
        if (!newPass.matches(passwordPattern)) {
            Toast.makeText(
                this,
                "New password must be at least 6 characters and contain a number",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (db.checkUser(username, oldPass)) {
            val success = db.updatePassword(username, newPass)

            if (success) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Incorrect username or old password", Toast.LENGTH_SHORT).show()
        }
    }
}