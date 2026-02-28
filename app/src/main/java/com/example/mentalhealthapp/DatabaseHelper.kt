package com.example.mentalhealthapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import java.security.MessageDigest

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MentalHealthApp.db"
        private const val DATABASE_VERSION = 2

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

    // =============================
    // PASSWORD HASHING (Security Upgrade)
    // =============================

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // =============================
    // ADD USER
    // =============================

    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)

        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, hashedPassword)
        }

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    // =============================
    // CHECK USER LOGIN
    // =============================

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val hashedPassword = hashPassword(password)

        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_USERNAME=? AND $COL_PASSWORD=?",
            arrayOf(username, hashedPassword),
            null, null, null
        )

        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    // =============================
    // UPDATE PASSWORD
    // =============================

    fun updatePassword(username: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(newPassword)

        val values = ContentValues().apply {
            put(COL_PASSWORD, hashedPassword)
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

    // =============================
    // DELETE USER
    // =============================

    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_USERS,
            "$COL_USERNAME=?",
            arrayOf(username)
        )
        db.close()
        return result > 0
    }

    // =============================
    // SEARCH USERS (Fixes Your Error)
    // =============================

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