package com.example.mentalhealthapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        db = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etOldPassword = findViewById<EditText>(R.id.etOldPassword)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val btnChange = findViewById<Button>(R.id.btnChangePassword)

        btnChange.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val oldPass = etOldPassword.text.toString().trim()
            val newPass = etNewPassword.text.toString().trim()

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
}