package pt.ipt.finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
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
        Toast.makeText(
            applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT
        ).show()
    }

    fun basicAlert() {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Log out")
            setMessage("Are you sure you want to log out from the account?")
            setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener(function = positiveButtonClick)
            )
            setNegativeButton("No", negativeButtonClick)
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
            basicAlert()
        }
    }
}