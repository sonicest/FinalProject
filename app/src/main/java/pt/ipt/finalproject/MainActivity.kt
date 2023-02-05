package pt.ipt.finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pt.ipt.finalproject.databinding.ActivityMainBinding
import pt.ipt.finalproject.utilities.Constant

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
        var appContext: Context? = null
            private set
    }

    private lateinit var binding: ActivityMainBinding


    //Main page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val uid = intent.getStringExtra("USER")
        val email = UserAuthentication.userInfo
        Log.d("ID", email)
        setListener()
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        val editor: SharedPreferences.Editor = Constant.sharedPreferences.edit()
        editor.putBoolean(Constant.IS_LOGGED_KEY, false)
        editor.apply()
        Intent(applicationContext, UserAuthentication::class.java).also {
            startActivity(it)
        }
        finish()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }

    fun basicAlert(name: String, desc: String, boolean: Boolean, button: String) {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(name)
            setMessage(desc)
            setNegativeButton(button, negativeButtonClick)
            if (boolean) {
                setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener(function = positiveButtonClick)
                )
            }
            show()
        }
    }

    private fun setListener() {
        binding.buttonCamera.setOnClickListener {
            Intent(applicationContext, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.acc.setOnClickListener {
            basicAlert(
                "Log out",
                "Are you sure you want to log out from the account?",
                true, "No"
            )
        }
        binding.info.setOnClickListener {
            basicAlert(
                "Information",
                "Instituto Politecnico de Tomar\nEngenharia Informatica\nDesenvolvimento de Aplicacoes Movies\nAlunos: 25153, 21075",
                false,
                "OK"
            )
        }
    }
}