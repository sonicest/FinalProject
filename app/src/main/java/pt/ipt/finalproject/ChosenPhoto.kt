package pt.ipt.finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_chosen_photo.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.ipt.finalproject.databinding.ActivityChosenPhotoBinding
import pt.ipt.finalproject.utilities.Constant
import pt.ipt.finalproject.utilities.displayToast
import pt.ipt.finalproject.utilities.show
import pt.ipt.finalproject.viewmodels.EditImageViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChosenPhoto : AppCompatActivity(), LocationListener {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private val LOCATION_PERMISSION_CODE = 2
    private lateinit var locationManager: LocationManager

    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityChosenPhotoBinding
    private lateinit var positionll: Pair<Double, Double>
    private val viewModel: EditImageViewModel by viewModel()

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "chosenImageUri"
    }

    //Бітмап фотографій
    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private lateinit var textEmotions: String


    private lateinit var mId: String
    private var isUpdate = false
    private val pickImage = 100

    //Гололвна функція  відтворення відредагованого фото
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChosenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        positionll = Pair(0.0, 0.0)

        getLocation()
        setListeners()
        setupObservers()
        prepareImagePreview()
        setupSpinner()

        val id = intent.getStringExtra("id")
        if (id != null) {
            fileUri = Uri.parse(intent.getStringExtra("imgUri"))
            image_preview.setImageURI(fileUri)
            inputEmotions.setText(intent.getStringExtra("description"))
            mId = id
            isUpdate = true
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // if ((ContextCompat.checkSelfPermission(
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0.1f, this)
    }

    override fun onLocationChanged(location: Location) {
        positionll = Pair(location.latitude, location.longitude)
        Log.d("pos", positionll.toString())
    }

    private fun collectMoments() {
        val description = inputEmotions.text.toString().trim()
        if (isUriEmpty(fileUri))
            displayToast("Please select img")
        else if (inputEmotions.isEmpty()) {
            displayToast("Please try to distinguish your feelings")
        } else {
            Log.d("Pos", positionll.toString())
//            val id = UserAuthentication.userInfo
//            Log.d("ID", id)
            val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            Constant.helper.saveMoments(
                fileUri.toString(),
                description,
                date,
                positionll.toString(),
                //id
            )
            displayToast("Moment successfully saved")
            originalBitmap?.let { bitmap ->
                viewModel.saveEditImage(bitmap)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            if (data != null) {
                fileUri = data.data!!
            }
            image_preview.setImageURI(fileUri)
        }
    }

    //spinner for choosing the emotions
    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.emotions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            emSpinner.adapter = adapter
        }

        emSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(pos).toString()
                if (inputEmotions.isEmpty())
                    inputEmotions.append(selectedItem)
                else inputEmotions.append(", $selectedItem")
                textEmotions = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    //part of these code was taken from the https://www.youtube.com/watch?v=dtlZENmOzp4&list=PLam6bY5NszYOGk7-8S9F3K4YpjLt2nKv8
    private fun setupObservers() {
        //showing the photo for the preview
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                originalBitmap = bitmap
                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImage(this)
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        //Saving
        viewModel.saveEditedImageUiState.observe(this) {
            val saveEditedImageDataState = it ?: return@observe
            if (saveEditedImageDataState.isLoading) {
                binding.save.visibility = View.GONE
                binding.previewProgressBar.visibility = View.VISIBLE
            } else {
                binding.previewProgressBar.visibility = View.GONE
                binding.save.visibility = View.VISIBLE
            }
            saveEditedImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    MainActivity::class.java
                ).also { editedImageIntent ->
                    editedImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(editedImageIntent)
                }
            } ?: kotlin.run {
                saveEditedImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(CameraActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
            fileUri = imageUri
            binding.imagePreview.setImageURI(imageUri)
        }
    }

    private fun setListeners() {
        binding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        binding.save.setOnClickListener {
            getLocation()
            Handler(Looper.getMainLooper()).postDelayed({
                //   locationManager.removeUpdates(this)
                collectMoments()
            }, 1000) // waits one second to center map
        }
    }

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        return msg.trim().isEmpty()
    }

    private fun isUriEmpty(uri: Uri?): Boolean {
        return uri == null || uri == Uri.EMPTY
    }

}