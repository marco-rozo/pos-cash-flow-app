package com.marcodev.cashflow.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.marcodev.cashflow.entity.Flow

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "database"
        private val DATABASE_VERSION = 1
    }

//Flow(var _id: Int, var value: Double, var type: String, var detail: String, var dtaLcto: String )
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${Flow.TABLE_NAME} ( ${Flow.KEY_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${Flow.KEY_VALUE} REAL," +
                    " ${Flow.KEY_TYPE} TEXT, ${Flow.KEY_DETAIL} TEXT, ${Flow.KEY_DTA_LCTO} TEXT )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE ${Flow.TABLE_NAME}")
        onCreate(db)
    }

}