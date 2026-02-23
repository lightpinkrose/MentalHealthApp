package com.example.mentalhealthapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var currentUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        db = DatabaseHelper(this)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val tvResults = findViewById<TextView>(R.id.tvResults)

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        // SEARCH
        btnSearch.setOnClickListener {
            val username = etSearch.text.toString().trim()

            if (db.getUser(username)) {
                currentUser = username
                tvResults.text = "User Found: $username"
            } else {
                currentUser = null
                tvResults.text = "User not found"
            }
        }

        // UPDATE PASSWORD
        btnUpdate.setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()

            if (currentUser != null && newPassword.isNotEmpty()) {
                val success = db.updatePassword(currentUser!!, newPassword)

                if (success) {
                    tvResults.text = "Password updated for $currentUser"
                    etNewPassword.text.clear()
                } else {
                    tvResults.text = "Update failed"
                }
            } else {
                tvResults.text = "Search for a user first"
            }
        }

        // DELETE USER
        btnDelete.setOnClickListener {
            if (currentUser != null) {
                val success = db.deleteUser(currentUser!!)

                if (success) {
                    tvResults.text = "User deleted"
                    currentUser = null
                    etSearch.text.clear()
                } else {
                    tvResults.text = "Delete failed"
                }
            } else {
                tvResults.text = "Search for a user first"
            }
        }
    }
}