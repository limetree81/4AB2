package com.example.a4ab2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        Task.createTable(p0!!)
        Plan.createTable(p0)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "testdb"
        var instance : DBHelper? = null

        fun getInstance(context: Context) : DBHelper{
            return if(instance == null) DBHelper(context.applicationContext) else instance!!
        }
    }
}