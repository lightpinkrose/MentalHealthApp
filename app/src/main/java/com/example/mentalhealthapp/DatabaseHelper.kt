package com.example.mentalhealthapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MentalHealthApp.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COL_ID = "id"
        private const val COL_USERNAME = "username"
        private const val COL_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT UNIQUE,
                $COL_PASSWORD TEXT
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    /** Adds a new user to the database */
    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    /** Checks if a username/password pair exists */
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_USERNAME=? AND $COL_PASSWORD=?",
            arrayOf(username, password),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    /** Updates a user's password */
    fun updatePassword(username: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PASSWORD, newPassword)
        }
        val result = db.update(
            TABLE_USERS,
            values,
            "$COL_USERNAME=?",
            arrayOf(username)
        )
        db.close()
        return result > 0
    }

    /** Optional: search entries by keyword (used in SearchActivity) */
    fun searchEntries(keyword: String): List<String> {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COL_USERNAME),
            "$COL_USERNAME LIKE ?",
            arrayOf("%$keyword%"),
            null, null, null
        )

        val results = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return results
    }
}