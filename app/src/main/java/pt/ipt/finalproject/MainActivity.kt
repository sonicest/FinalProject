package pt.ipt.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import pt.ipt.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding: ActivityMainBinding

    //Відкриття головного меню
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setListener()
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