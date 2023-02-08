package pt.ipt.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_authentication.*
import pt.ipt.finalproject.database.DatabaseHelper
import pt.ipt.finalproject.models.Moment
import pt.ipt.finalproject.utilities.Constant.Companion.IS_LOGGED_KEY
import pt.ipt.finalproject.utilities.Constant.Companion.helper
import pt.ipt.finalproject.utilities.Constant.Companion.sharedPreferences
import pt.ipt.finalproject.utilities.ProgressDialogUtils
import pt.ipt.finalproject.utilities.displayToast

class UserAuthentication : AppCompatActivity() {
    private var isRegister = false
//    companion object {
//        var userInfo = ""
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_authentication)
        supportActionBar?.hide() //dont show the app bar on the top

        btn_login.setOnClickListener {
            //what the user its inputing
            val email = et_email.text.toString().trim()
            val psw = et_psw.text.toString().trim()

            if (email.isEmpty()) {
                et_email.error = "Enter Email"
                et_email.requestFocus()
                et_email.performClick()
            } else if (psw.isEmpty()) {
                et_psw.error = "Enter Password"
                et_psw.requestFocus()
                et_psw.performClick()
            } else if (psw.length < 6) {
                et_psw.error = "Enter Password at least 6 characters"
                et_psw.requestFocus()
                et_psw.performClick()
            } else {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                ProgressDialogUtils.showProgressDialog(this, "Loading, please wait...", false)
                val res = getUserValid(email, psw)
                if (isRegister) {
                    if (!res) {
                        helper.registerUser(email, psw)
                        displayToast("Successfully register")
                        Intent(applicationContext, MainActivity::class.java).also {
                            // userInfo = getUserId(email, psw)
                            startActivity(it)
                        }
                        editor.putBoolean(IS_LOGGED_KEY, true)
                        editor.apply()
                        ProgressDialogUtils.dismissProgress()
                    }
                } else {
                    if (res) {
                        displayToast("Successfully Logged")
                        Intent(applicationContext, MainActivity::class.java).also {
                            //    userInfo = getUserId(email, psw)
                            startActivity(it)
                        }
                        editor.putBoolean(IS_LOGGED_KEY, true)
                        editor.apply()
                        ProgressDialogUtils.dismissProgress()
                    } else {
                        displayToast("Wrong Email/Password")
                        ProgressDialogUtils.dismissProgress()
                    }
                }
            }
        }

        tv_changeScreenStatus.setOnClickListener {
            if (isRegister) {
                isRegister = false
                btn_login.text = getString(R.string.login)
                tv_statusLabel.text = getString(R.string.do_not_have_an_account)
            } else {
                isRegister = true
                btn_login.text = getString(R.string.reg)
                tv_statusLabel.text = getString(R.string.have_acc)
            }
        }
    }

    @SuppressLint("Range", "Recycle")
    fun getUserValid(email: String, psw: String): Boolean {
        var isUser = false
        val selectQuery =
            "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.USER_EMAIL}=? AND ${DatabaseHelper.USER_PSW}=?"
        val db: SQLiteDatabase = helper.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(email, psw))
        if (cursor.count > 0) {
            isUser = true
        }
        return isUser
    }

//    fun getUserId(email: String, psw: String): String {
//        val list: ArrayList<Moment> = ArrayList()
//        val selectQuery = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.USER_EMAIL}=? AND ${DatabaseHelper.USER_PSW}=?"
//        var id = ""
//        val db: SQLiteDatabase = helper.readableDatabase
//        val cursor: Cursor?
//        cursor = db.rawQuery(selectQuery,  arrayOf(email, psw))
//        db.execSQL(selectQuery)
//        if (cursor.moveToFirst()) {
//            do {
//                id = cursor.getString(0)
//                val imgUri = cursor.getString(1)
//                val description = cursor.getString(2)
//            } while (cursor.moveToNext())
//        }
//        return id
//    }
}