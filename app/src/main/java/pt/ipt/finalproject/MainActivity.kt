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

    //Відкриття головного меню
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setListener()
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        val editor: SharedPreferences.Editor =  Constant.sharedPreferences.edit()
        editor.putBoolean(Constant.IS_LOGGED_KEY, false)
        editor.apply()
        finish()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
    }

    fun basicAlert(){

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Log out")
            setMessage("Are you sure you want to log out from the account?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("No", negativeButtonClick)
            show()
        }


    }

    //Функція, що виконує запит користувача при нажатті на кнопки
    private fun setListener() {
        binding.buttonCamera.setOnClickListener {
//            Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            ).also { pickerIntent ->
//                pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
//            }
            Intent(applicationContext, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.buttonMap.setOnClickListener {
            Intent(applicationContext, MapTracking::class.java).also {
                startActivity(it)
            }
        }

        binding.acc.setOnClickListener{
//            Intent(applicationContext, MapInfoWindow::class.java).also {
//                startActivity(it)
//            }
            basicAlert()
        }
    }

    //Доступ до галереї
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode== RESULT_OK){
//            data?.data?.let { imageUri ->
//                Intent(applicationContext, EditImageActivity::class.java).also{ editImageIntent ->
//                    editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
//                    startActivity(editImageIntent)
//                }
//            }
//        }
//    }
}