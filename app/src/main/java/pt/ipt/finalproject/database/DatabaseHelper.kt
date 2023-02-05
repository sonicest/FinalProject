package pt.ipt.finalproject.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import pt.ipt.finalproject.models.Moment

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private val qryMoments =
        ("CREATE TABLE $TABLE_MOMENTS ($ID INTEGER PRIMARY KEY, $IMG_URI TEXT , $DESCRIPTION TEXT, $DATE TEXT, $LOCATION TEXT, $USER_ID INTEGER FOREIGN KEY)")
    private val qryUser =
        ("CREATE TABLE $TABLE_USERS ($USER_ID INTEGER PRIMARY KEY, $USER_EMAIL TEXT , $USER_PSW TEXT)")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(qryMoments)
        db.execSQL(qryUser)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun saveMoments(imgUri: String, description: String, date: String, location: String): Boolean {
        val values = ContentValues()
        values.put(IMG_URI, imgUri)
        values.put(DESCRIPTION, description)
        values.put(DATE, date)
        values.put(LOCATION, location)
       // values.put(USER_ID, id)

        val db = this.writableDatabase
        val res = db.insert(TABLE_MOMENTS, null, values)
        return res != -1L
    }

    fun deleteMoment(id: String): Int {
        val database = this.writableDatabase
        return database.delete(TABLE_MOMENTS, "$ID=?", arrayOf(id))
    }

    fun registerUser(email: String, psw: String): Boolean {
        val values = ContentValues()
        values.put(USER_EMAIL, email)
        values.put(USER_PSW, psw)
        val db = this.writableDatabase
        val res = db.insert(TABLE_USERS, null, values)
        return res != -1L
    }

    @SuppressLint("Range", "Recycle")
    fun getMoments(): ArrayList<Moment> {
        val list: ArrayList<Moment> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_MOMENTS"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0)
                val imgUri = cursor.getString(1)
                val description = cursor.getString(2)
                val date = cursor.getString(3)
                val location = cursor.getString(4)
//                val userId = cursor.getString(5)
                val moment = Moment(id, imgUri, description, date, location)//, user_id)
                list.add(moment)
            } while (cursor.moveToNext())
        }
        return list
    }


    companion object {
        private const val DATABASE_NAME = "peenox_db"
        private const val DATABASE_VERSION = 1
        const val TABLE_MOMENTS = "tbl_moments"
        const val TABLE_USERS = "tbl_users"

        const val ID = "id"
        const val IMG_URI = "imgUri"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val LOCATION = "location"

        const val USER_ID = "id"
        const val USER_EMAIL = "email"
        const val USER_PSW = "psw"
    }
}