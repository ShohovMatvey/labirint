package com.example.labirint

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context)// конструктор суперкласса
    : SQLiteOpenHelper(context, "myDB", null, 1) {

    internal val LOG_TAG = "myLogs"


    override fun onCreate(db: SQLiteDatabase) {

        Log.d(LOG_TAG, "--- onCreate database ---")
        // создаем таблицу с полями
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
        val cv = ContentValues()
        // получаем данные из полей ввода
        // подключаемся к БД
        val db = this.writableDatabase
        Log.d(LOG_TAG, "--- Insert in mytable: ---")
        // подготовим данные для вставки в виде пар: наименование столбца - значение

        cv.put("Xpoint", state.Xpoint)
        cv.put("Ypoint", state.Ypoint)
        cv.put("Xpoint_start", state.Xpoint_start)
        cv.put("Ypoint_start", state.Ypoint_start)
        cv.put("Xpoint_old", state.Xpoint_old)
        cv.put("Ypoint_old", state.Ypoint_old)
        cv.put("Left_time", state.Left_time)
        // вставляем запись и получаем ее ID
        val rowID = db.insert("mytable", null, cv)
        Log.d(LOG_TAG, "row inserted, ID = " + rowID)

        this.close()
    }




    fun read(){
        // подключаемся к БД
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Rows in mytable: ---")
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        val c = db.query("mytable", arrayOf("id",
                "Xpoint",
                "Ypoint",
                "Xpoint_start",
                "Ypoint_start",
                "Xpoint_old",
                "Ypoint_old",
                "Left_time"), "id like ?", arrayOf("3"), null, null, null)

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            val idColIndex = c.getColumnIndex("id")
            val nameColIndex = c.getColumnIndex("Xpoint")
            val emailColIndex = c.getColumnIndex("Ypoint")

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", Xpoint = " + c.getString(nameColIndex) +
                                ", Ypoint = " + c.getString(emailColIndex))
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext())
        } else
            Log.d(LOG_TAG, "0 rows")
        c.close()

        db.close()
    }

    fun clear(){
        // подключаемся к БД
        val db = this.writableDatabase

        Log.d(LOG_TAG, "--- Clear mytable: ---")
        // удаляем все записи
        val clearCount = db.delete("mytable", null, null)
        Log.d(LOG_TAG, "deleted rows count = " + clearCount)

        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}