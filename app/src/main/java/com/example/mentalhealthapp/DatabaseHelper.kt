package com.example.mentalhealthapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MentalHealth.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // INSERT USER (REGISTER)
    fun insertUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("password", password)

        val result = db.insert("users", null, values)
        return result != -1L
    }

    // CHECK USER (LOGIN)
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // SEARCH USER
    fun getUser(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username = ?",
            arrayOf(username)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // UPDATE PASSWORD
    fun updatePassword(username: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("password", newPassword)

        val result = db.update(
            "users",
            values,
            "username = ?",
            arrayOf(username)
        )

        return result > 0
    }

    // DELETE USER
    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            "users",
            "username = ?",
            arrayOf(username)
        )

        return result > 0
    }
}