package com.example.headsupprep

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context): SQLiteOpenHelper(context, "celebrities", null, 1) {
    var sqlDb: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        if(db != null){
            db.execSQL("create table celebrities (_id integer primary key autoincrement, Name text, Taboo1 text, Taboo2 text, Taboo3 text)")
        }
    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun addCelebrity(name: String, taboo1: String, taboo2: String, taboo3: String): Long{
//        sqlDb = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", name)
        contentValues.put("Taboo1", taboo1)
        contentValues.put("Taboo2", taboo2)
        contentValues.put("Taboo3", taboo3)
        return sqlDb.insert("celebrities", null, contentValues)
    }

    @SuppressLint("Range")
    fun getCelebrities(): ArrayList<Celebrity>{
        val celebrities: ArrayList<Celebrity> = ArrayList()
        val tableName = "celebrities"
        val selectQuery = "SELECT * FROM $tableName"

        var cursor: Cursor? = null

        try{
            cursor = sqlDb.rawQuery(selectQuery, null)
        }catch(e: SQLiteException){
            sqlDb.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var taboo1: String
        var taboo2: String
        var taboo3: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("_id"))
                name = cursor.getString(cursor.getColumnIndex("Name"))
                taboo1 = cursor.getString(cursor.getColumnIndex("Taboo1"))
                taboo2 = cursor.getString(cursor.getColumnIndex("Taboo2"))
                taboo3 = cursor.getString(cursor.getColumnIndex("Taboo3"))

                val celebrity = Celebrity(id, name, taboo1, taboo2, taboo3)
                celebrities.add(celebrity)
            }while(cursor.moveToNext())
        }

        return celebrities
    }

    fun updateCelebrity(celebrity: Celebrity): Int {
        val contentValues = ContentValues()
        contentValues.put("Name", celebrity.name)
        contentValues.put("Taboo1", celebrity.taboo1)
        contentValues.put("Taboo2", celebrity.taboo2)
        contentValues.put("Taboo3", celebrity.taboo3)

        val success = sqlDb.update("celebrities", contentValues, "_id = ${celebrity.id}", null)

        sqlDb.close()
        return success
    }

    fun deleteCelebrity(celebrity: Celebrity): Int{
        val contentValues = ContentValues()
        contentValues.put("_id", celebrity.id)
        val success = sqlDb.delete("celebrities", "_id = ${celebrity.id}", null)
        sqlDb.close()
        return success
//        success > 0 means it worked
    }
}

