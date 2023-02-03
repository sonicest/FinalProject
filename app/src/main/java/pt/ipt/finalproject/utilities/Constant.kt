package pt.ipt.finalproject.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import pt.ipt.finalproject.MainActivity
import pt.ipt.finalproject.database.DatabaseHelper

class Constant {

    companion object {
        @SuppressLint("StaticFieldLeak")
        val APP_CXT = MainActivity.appContext

        val helper = DatabaseHelper(APP_CXT!!)

        private const val sharedPref = "loginSharedPref"
        val sharedPreferences: SharedPreferences = APP_CXT!!.getSharedPreferences(
            sharedPref,
            Context.MODE_PRIVATE)

        const val IS_LOGGED_KEY = "isLogGED"
    }
}