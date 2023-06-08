package pt.ipt.finalproject

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pt.ipt.finalproject.databinding.ActivityMainBinding
import pt.ipt.finalproject.utilities.Constant
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
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

        requestPermissionIfNessesary(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
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
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
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
        binding.observe.setOnClickListener {
            Intent(applicationContext, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.explore.setOnClickListener {
            Intent(applicationContext, MapActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.experience.setOnClickListener {
            Intent(applicationContext, TaskActivity::class.java).also {
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
                "Hi there",
                false,
                "OK"
            )
            Intent(applicationContext, AnalysisActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    fun requestPermissionIfNessesary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}