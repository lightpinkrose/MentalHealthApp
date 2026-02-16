package com.example.mentalhealthapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        db = DatabaseHelper(this)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val tvResults = findViewById<TextView>(R.id.tvResults)

        btnSearch.setOnClickListener {
            val keyword = etSearch.text.toString().trim()
            val results = db.searchEntries(keyword)
            tvResults.text = if (results.isEmpty()) "No records found." else results.joinToString("\n\n")
        }
    }
}