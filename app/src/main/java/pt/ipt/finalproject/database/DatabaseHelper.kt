package pt.ipt.finalproject.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import pt.ipt.finalproject.models.Emotions
import pt.ipt.finalproject.models.Moment
import pt.ipt.finalproject.utilities.Constant.Companion.helper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //establishing the db columns

    //for the excuting the tables querys (request the information of database)
    override fun onCreate(db: SQLiteDatabase) {
        val qryMoments = "CREATE TABLE $TABLE_MOMENTS ($ID INTEGER PRIMARY KEY, $IMG_URI TEXT , $DESCRIPTION TEXT, $DATE TEXT, $LOCATION TEXT)"
        val qryEmotions = "CREATE TABLE $TABLE_EMOTIONS ($ID INTEGER PRIMARY KEY, $TYPE INTEGER , $NAME TEXT, $DATE TEXT)"
        val qryUsers = "CREATE TABLE $TABLE_USERS ($USER_ID INTEGER PRIMARY KEY, $USER_EMAIL TEXT , $USER_PSW TEXT)"


        db.execSQL(qryMoments)
        db.execSQL(qryUsers)
        db.execSQL(qryEmotions)

        // You can add any initial data insertion or other database setup operations here
    }


    // it checks the talbes if exist or not if exist, they just drop them from db
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMOTIONS")
        onCreate(db)
    }
// insert moments in the datebase

    fun saveEmotions(
        type: Int,
        name: String,
        // description: String,
        date: String,
        //  location: String,
    ): Boolean {
        val values = ContentValues()
        values.put(TYPE, type)
        values.put(NAME, name)
        // values.put(DESCRIPTION, description)
        values.put(DATE, date)
        //values.put(LOCATION, location)
        //     values.put(USERID, id)

        val db = this.writableDatabase
        val res = db.insert(TABLE_EMOTIONS, null, values)
        println(values.getAsString("type"))
        println(values.getAsString("name"))
        println(values.getAsString("date"))
        return res != -1L // verified if the data is insert or not
    }

    @SuppressLint("Range", "Recycle")
    fun getEmotions(): ArrayList<Emotions> {
        val list: ArrayList<Emotions> = ArrayList()
        val selectQuery =
            "SELECT * FROM $TABLE_EMOTIONS"
        val db = this.readableDatabase
        val cursor: Cursor?
        //try catch is for exepction handling, means the may be some exepction comes when the user fetch data from
        //the db so i put in the try catch block
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //if there is some exception it executes the catch block other wiese try block
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0)
                val type = cursor.getString(1)
                val name = cursor.getString(2)
                val date = cursor.getString(3)
                val emotions = Emotions(id, type, name, date)//, userId)
                list.add(emotions)
            } while (cursor.moveToNext())
        }
        return list
    }

    fun saveMoments(
        imgUri: String,
        description: String,
        date: String,
        location: String,
        // id: String
    ): Boolean {
        val values = ContentValues()
        values.put(IMG_URI, imgUri)
        values.put(DESCRIPTION, description)
        values.put(DATE, date)
        values.put(LOCATION, location)
        //     values.put(USERID, id)

        val db = this.writableDatabase
        val res = db.insert(TABLE_MOMENTS, null, values)
        return res != -1L // verified if the data is insert or not (return the value for collectionlingsactivity)
    }

    // delete a moment
    fun deleteMoment(id: String): Int {
        val database = this.writableDatabase
        return database.delete(TABLE_MOMENTS, "$ID=?", arrayOf(id))
    }

    // for register the user in db
    //recive the email string and psw string from the input user
    fun registerUser(email: String, psw: String): Boolean {
        val values = ContentValues()
        values.put(USER_EMAIL, email)
        values.put(USER_PSW, psw)
        val db = this.writableDatabase
        val res = db.insert(TABLE_USERS, null, values)
        return res != -1L
    }

    // for geting all the lsit of moments saved in the database
    @SuppressLint("Range", "Recycle")
    fun getMoments(): ArrayList<Moment> {
        val list: ArrayList<Moment> = ArrayList()
        val selectQuery =
            "SELECT * FROM $TABLE_MOMENTS"//WHERE ${USERID}=UserAuthentication.userInfo "
        val db = this.readableDatabase
        val cursor: Cursor?
        //try catch is for exepction handling, means the may be some exepction comes when the user fetch data from
        //the db so i put in the try catch block
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        //if there is some exception it executes the catch block other wiese try block
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0)
                val imgUri = cursor.getString(1)
                val description = cursor.getString(2)
                val date = cursor.getString(3)
                val location = cursor.getString(4)
                // val userId = cursor.getString(5)
                val moment = Moment(id, imgUri, description, date, location)//, userId)
                list.add(moment)
            } while (cursor.moveToNext())
        }
        return list
    }
//data base variebles

    companion object {
        private const val DATABASE_NAME = "peenox_db"
        private const val DATABASE_VERSION = 2
        const val TABLE_MOMENTS = "tbl_moments"
        const val TABLE_USERS = "tbl_users"
        const val TABLE_EMOTIONS = "tbl_emotions"

        const val ID = "id"
        const val IMG_URI = "imgUri"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val LOCATION = "location"
        const val USERID = "userId"

        const val USER_ID = "id"
        const val USER_EMAIL = "email"
        const val USER_PSW = "psw"

        const val TYPE = "type"
        const val NAME = "name"

    }


}