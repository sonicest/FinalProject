package pt.ipt.finalproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.ipt.finalproject.databinding.ActivityChosenPhotoBinding
import pt.ipt.finalproject.utilities.displayToast
import pt.ipt.finalproject.utilities.show
import pt.ipt.finalproject.viewmodels.EditImageViewModel

class ChosenPhoto : AppCompatActivity() {
    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityChosenPhotoBinding
    private val viewModel: EditImageViewModel by viewModel()

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "chosenImageUri"
    }

    //Бітмап фотографій
    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    //Гололвна функція  відтворення відредагованого фото
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChosenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
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
                    viewModel.loadImageFilters(this)
                }
                //binding.imagePreview.setImageBitmap(bitmap)
            } ?: kotlin.run {
                dataState.error?.let { error ->
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
    private fun setListeners(){
        binding.fabShare.setOnClickListener{
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
        binding.save.setOnClickListener{
           // filteredBitmap.value?.let{bitmap->
            originalBitmap?.let{bitmap->
                viewModel.saveFilteredImage(bitmap)
            }
        }

    }
}