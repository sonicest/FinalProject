package pt.ipt.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import pt.ipt.finalproject.utilities.Constant.Companion.IS_LOGGED_KEY
import pt.ipt.finalproject.utilities.Constant.Companion.sharedPreferences

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLogged = sharedPreferences.getBoolean(IS_LOGGED_KEY, false)
            if (isLogged)
                Intent(applicationContext, MainActivity::class.java).also {
                    startActivity(it)

                }
            else
                Intent(applicationContext, UserAuthentication::class.java).also {
                    startActivity(it)
                }
        }, 3000)
    }
}