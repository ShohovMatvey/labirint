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
                + "Ypoint text,"
                + "Xpoint_start text,"
                + "Ypoint_start text,"
                + "Xpoint_old text,"
                + "Ypoint_old text,"
                + "Xkey text,"
                + "Ykey text,"
                + "key_yes_was,"
                + "key_yes,"
                + "Left_time text" + ");"))

        Log.d(LOG_TAG, "--- onCreate database ---")
        db.execSQL(("create table IF NOT EXISTS mytable2 ("
                + "id integer primary key autoincrement,"
                + "Kol_games_easy text,"
                + "Kol_wins_easy text,"
                + "Sred_time_easy text,"
                + "Min_time_easy text,"
                + "Kol_games_normal text,"
                + "Kol_wins_normal text,"
                + "Sred_time_normal text,"
                + "Min_time_normal text,"
                + "Kol_games_dif text,"
                + "Kol_wins_dif text,"
                + "Sred_time_dif text,"
                + "Min_time_dif text,"
                + "Kol_games_hard text,"
                + "Kol_wins_hard text,"
                + "Sred_time_hard text,"
                + "Min_time_hard text" + ");"))

        Log.d(LOG_TAG, "--- onCreate database ---")
        db.execSQL(("create table IF NOT EXISTS mytable3 ("
                + "id integer primary key autoincrement,"
                + "Mas_klet_str text,"
                + "Mas_sten_str text,"
                + "win_sten text" + ");"))
    }



    fun add(state : GameState) {
        val cv = ContentValues()
        val db = this.writableDatabase
        Log.d(LOG_TAG, "--- Insert in mytable: ---")

        cv.put("Xpoint", state.Xpoint)
        cv.put("Ypoint", state.Ypoint)
        cv.put("Xpoint_start", state.Xpoint_start)
        cv.put("Ypoint_start", state.Ypoint_start)
        cv.put("Xpoint_old", state.Xpoint_old)
        cv.put("Ypoint_old", state.Ypoint_old)
        cv.put("Xkey", state.Xkey)
        cv.put("Ykey", state.Ykey)
        cv.put("key_yes_was", state.key_yes_was)
        cv.put("key_yes", state.key_yes)
        cv.put("Left_time", state.Left_time)

        val rowID = db.insert("mytable", null, cv)
        Log.d(LOG_TAG, "row inserted, ID = " + rowID)
        this.close()
    }

    fun add_stat(state : GameStatisticActivity) {
        val cv = ContentValues()
        val db = this.writableDatabase
        Log.d(LOG_TAG, "--- Insert in mytable2: ---")

        cv.put("Kol_games_easy", state.Kol_games_easy)
        cv.put("Kol_wins_easy", state.Kol_wins_easy)
        cv.put("Sred_time_easy", state.Sred_time_easy)
        cv.put("Min_time_easy", state.Min_time_easy)
        cv.put("Kol_games_normal", state.Kol_games_normal)
        cv.put("Kol_wins_normal", state.Kol_wins_normal)
        cv.put("Sred_time_normal", state.Sred_time_normal)
        cv.put("Min_time_normal", state.Min_time_normal)
        cv.put("Kol_games_dif", state.Kol_games_dif)
        cv.put("Kol_wins_dif", state.Kol_wins_dif)
        cv.put("Sred_time_dif", state.Sred_time_dif)
        cv.put("Min_time_dif", state.Min_time_dif)
        cv.put("Kol_games_hard", state.Kol_games_hard)
        cv.put("Kol_wins_hard", state.Kol_wins_hard)
        cv.put("Sred_time_hard", state.Sred_time_hard)
        cv.put("Min_time_hard", state.Min_time_hard)

        val rowID2 = db.insert("mytable2", null, cv)
        Log.d(LOG_TAG, "row inserted, ID = " + rowID2)
        this.close()
    }

    fun add_mas(state : GameMassivActivity) {
        val cv = ContentValues()
        val db = this.writableDatabase
        Log.d(LOG_TAG, "--- Insert in mytable3: ---")

        cv.put("Mas_klet_str", state.Mas_klet_str)
        cv.put("Mas_sten_str", state.Mas_sten_str)
        cv.put("win_sten", state.win_sten)

        val rowID3 = db.insert("mytable3", null, cv)
        Log.d(LOG_TAG, "row inserted, ID = " + rowID3)
        this.close()
    }



    fun read() : GameState? {
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Rows in mytable: ---")
        val c = db.query("mytable", arrayOf("id",
                "Xpoint",
                "Ypoint",
                "Xpoint_start",
                "Ypoint_start",
                "Xpoint_old",
                "Ypoint_old",
                "Xkey",
                "Ykey",
                "key_yes_was",
                "key_yes",
                "Left_time"), null, null, null, null, null)  //"id like ?", arrayOf("3")

        val state : GameState?

        if (c.moveToNext()) {
            state = GameState(c.getString(c.getColumnIndex("Xpoint")),
                    c.getString(c.getColumnIndex("Ypoint")),
                    c.getString(c.getColumnIndex("Xpoint_start")),
                    c.getString(c.getColumnIndex("Ypoint_start")),
                    c.getString(c.getColumnIndex("Xpoint_old")),
                    c.getString(c.getColumnIndex("Ypoint_old")),
                    c.getString(c.getColumnIndex("Xkey")),
                    c.getString(c.getColumnIndex("Ykey")),
                    c.getString(c.getColumnIndex("key_yes_was")),
                    c.getString(c.getColumnIndex("key_yes")),
                    c.getString(c.getColumnIndex("Left_time")))
        }else {
            state = null
        }

        c.close()
        db.close()

        return state
    }

    fun read_stat() : GameStatisticActivity? {
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Rows in mytable2: ---")
        val c = db.query("mytable2", arrayOf("id",
                "Kol_games_easy",
                "Kol_wins_easy",
                "Sred_time_easy",
                "Min_time_easy",
                "Kol_games_normal",
                "Kol_wins_normal",
                "Sred_time_normal",
                "Min_time_normal",
                "Kol_games_dif",
                "Kol_wins_dif",
                "Sred_time_dif",
                "Min_time_dif",
                "Kol_games_hard",
                "Kol_wins_hard",
                "Sred_time_hard",
                "Min_time_hard"),null, null, null, null, null)

        val state2 : GameStatisticActivity?

        if (c.moveToNext()) {
            state2 = GameStatisticActivity(c.getString(c.getColumnIndex("Kol_games_easy")),
                    c.getString(c.getColumnIndex("Kol_wins_easy")),
                    c.getString(c.getColumnIndex("Sred_time_easy")),
                    c.getString(c.getColumnIndex("Min_time_easy")),
                    c.getString(c.getColumnIndex("Kol_games_normal")),
                    c.getString(c.getColumnIndex("Kol_wins_normal")),
                    c.getString(c.getColumnIndex("Sred_time_normal")),
                    c.getString(c.getColumnIndex("Min_time_normal")),
                    c.getString(c.getColumnIndex("Kol_games_dif")),
                    c.getString(c.getColumnIndex("Kol_wins_dif")),
                    c.getString(c.getColumnIndex("Sred_time_dif")),
                    c.getString(c.getColumnIndex("Min_time_dif")),
                    c.getString(c.getColumnIndex("Kol_games_hard")),
                    c.getString(c.getColumnIndex("Kol_wins_hard")),
                    c.getString(c.getColumnIndex("Sred_time_hard")),
                    c.getString(c.getColumnIndex("Min_time_hard")))
        }else {
            state2 = null
        }

        c.close()
        db.close()

        return state2
    }

    fun read_mas() : GameMassivActivity? {
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Rows in mytable3: ---")
        val c = db.query("mytable3", arrayOf("id",
                "Mas_klet_str",
                "Mas_sten_str",
                "win_sten"), null, null, null, null, null)  //"id like ?", arrayOf("3")

        val state3 : GameMassivActivity?

        if (c.moveToNext()) {
            state3 = GameMassivActivity(c.getString(c.getColumnIndex("Mas_klet_str")),
                    c.getString(c.getColumnIndex("Mas_sten_str")),
                    c.getString(c.getColumnIndex("win_sten")))
        }else {
            state3 = null
        }

        c.close()
        db.close()

        return state3
    }




    fun clear(){
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Clear mytable: ---")
        val clearCount = db.delete("mytable", null, null)
        Log.d(LOG_TAG, "deleted rows count = " + clearCount)

        db.close()
    }

    fun clear_stat() {
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Clear mytable: ---")
        val clearCount = db.delete("mytable2", null, null)
        Log.d(LOG_TAG, "deleted rows count = " + clearCount)

        db.close()
    }

    fun clear_mas() {
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Clear mytable: ---")
        val clearCount = db.delete("mytable3", null, null)
        Log.d(LOG_TAG, "deleted rows count = " + clearCount)

        db.close()
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}