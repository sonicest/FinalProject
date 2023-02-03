package pt.ipt.finalproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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

class ChosenPhoto : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityChosenPhotoBinding
    private val viewModel: EditImageViewModel by viewModel()

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "chosenImageUri"
        //const val DEFAULT_CURRENCY_TYPE = "Choose an emotion"
    }

    //Бітмап фотографій
    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private lateinit var textEmotions: String
    private val filteredBitmap = MutableLiveData<Bitmap>()

    private lateinit var mId: String
    private var isUpdate = false
    private val pickImage = 100

    //Гололвна функція  відтворення відредагованого фото
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChosenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
        setupSpinner()
///


        val id = intent.getStringExtra("id")
        if (id != null) {
            fileUri = Uri.parse(intent.getStringExtra("imgUri"))
            image_preview.setImageURI(fileUri)
            inputEmotions.setText(intent.getStringExtra("description"))
            mId = id
            isUpdate = true

            supportActionBar?.title = "Update Feelings"
            //    btn_saveMoments.text = getString(R.string.update_now)
        }
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, pickImage)

    }

    private fun collectMoments() {
        val description = inputEmotions.text.toString().trim()
        if (isUriEmpty(fileUri))
            showToast("Please select img")
//        else if (description.isEmpty())
//            showToast("Please try to distinguish your feelings")
        else {
            val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
//            if (isUpdate) {
//                Constant.helper.deleteMoment(mId)
//                Constant.helper.saveMoments(fileUri.toString(), description, date)
//                showToast("Moments successfully updated")
//                //startActivity<MomentsActivity>(finish = true)
//            } else {
            Constant.helper.saveMoments(fileUri.toString(), description, date)
            showToast("Moment successfully saved")
            // startActivity<MainActivity>(finish = true)
            //    }
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

    private fun setupObservers() {

        //Перегляд оригінального фото
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                //Firstly filtered image=original image
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImage(this)
                }
                //binding.imagePreview.setImageBitmap(bitmap)
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        //Збереження відредагованого фото
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


    //Поширення фото
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
            // filteredBitmap.value?.let{bitmap->
            if (inputEmotions.isEmpty()) {
                showToast("Please try to distinguish your feelings")
            } else
                collectMoments()
            originalBitmap?.let { bitmap ->
                viewModel.saveEditImage(bitmap, textEmotions)
            }

        }
    }

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        //check if the EditText have values or not
        return msg.trim().isEmpty()
    }

    private fun showToast(s: String) {
        Toast.makeText(
            applicationContext,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isUriEmpty(uri: Uri?): Boolean {
        return uri == null || uri == Uri.EMPTY
    }
}


