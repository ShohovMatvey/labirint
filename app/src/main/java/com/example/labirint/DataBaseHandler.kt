package com.example.labirint

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context)
    : SQLiteOpenHelper(context, "myDB", null, 1) {

    internal val LOG_TAG = "myLogs"

    override fun onCreate(db: SQLiteDatabase) {

        Log.d(LOG_TAG, "--- onCreate database ---")
        db.execSQL(("create table IF NOT EXISTS mytable ("
                + "id integer primary key autoincrement,"
                + "Xpoint text,"
                + "Ypoint text," +
                "Xpoint_start text," +
                "Ypoint_start text," +
                "Xpoint_old text," +
                "Ypoint_old text," +
                "Left_time text" + ");"))
    }


    fun add(state : GameState) {
        clear()
        val cv = ContentValues()
        val db = this.writableDatabase
        Log.d(LOG_TAG, "--- Insert in mytable: ---")

        cv.put("Xpoint", state.Xpoint)
        cv.put("Ypoint", state.Ypoint)
        cv.put("Xpoint_start", state.Xpoint_start)
        cv.put("Ypoint_start", state.Ypoint_start)
        cv.put("Xpoint_old", state.Xpoint_old)
        cv.put("Ypoint_old", state.Ypoint_old)
        cv.put("Left_time", state.Left_time)

        val rowID = db.insert("mytable", null, cv)
        Log.d(LOG_TAG, "row inserted, ID = " + rowID)
        this.close()
    }


    fun read(){
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Rows in mytable: ---")
        val c = db.query("mytable", arrayOf("id",
                "Xpoint",
                "Ypoint",
                "Xpoint_start",
                "Ypoint_start",
                "Xpoint_old",
                "Ypoint_old",
                "Left_time"), null, null, null, null, null)  //"id like ?", arrayOf("3")

        if (c.moveToFirst()) {

            val idIndex = c.getColumnIndex("id")
            val XpointIndex = c.getColumnIndex("Xpoint")
            val YpointIndex = c.getColumnIndex("Ypoint")
            val Xpoint_startIndex = c.getColumnIndex("Xpoint_start")
            val Ypoint_startIndex = c.getColumnIndex("Ypoint_start")
            val Xpoint_oldIndex = c.getColumnIndex("Xpoint_old")
            val Ypoint_oldIndex = c.getColumnIndex("Ypoint_old")
            val Left_timeIndex = c.getColumnIndex("Left_time")
            do {

                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idIndex) +
                                ", Xpoint = " + c.getString(XpointIndex) +
                                ", Ypoint = " + c.getString(YpointIndex) +
                                ", Xpoint_start = " + c.getString(Xpoint_startIndex) +
                                ", Ypoint_start = " + c.getString(Ypoint_startIndex) +
                                ", Xpoint_old = " + c.getString(Xpoint_oldIndex) +
                                ", Ypoint_old = " + c.getString(Ypoint_oldIndex) +
                                ", Left_time = " + c.getString(Left_timeIndex))
            } while (c.moveToNext())
        }
        else
            Log.d(LOG_TAG, "0 rows")

        c.close()
        db.close()
    }

    fun clear(){
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Clear mytable: ---")
        val clearCount = db.delete("mytable", null, null)
        Log.d(LOG_TAG, "deleted rows count = " + clearCount)

        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}