package pt.ipt.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.content.FileProvider
import pt.ipt.finalproject.adapters.SavedImagesAdapter
import pt.ipt.finalproject.databinding.ActivitySavedImagesBinding
import pt.ipt.finalproject.listeners.SavedImageListener
import pt.ipt.finalproject.utilities.displayToast
import pt.ipt.finalproject.viewmodels.SavedImagesViewModel
import pt.ipt.finalproject.ChosenPhoto
import java.io.File

class SavedImages : AppCompatActivity() , SavedImageListener {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    //Основна функція збережених фото
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        setListener()
        viewModel.loadSavedImages()
    }

    //Показ збережних фото
    private fun setupObserver(){
        viewModel.savedImagesUiState.observe(this) {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImagesAdapter(savedImages, this).also { adapter->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    //Вихід в головне меню
    private fun  setListener(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
    }

    //Відкриття фото в новій сторінці
    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            ChosenPhoto::class.java
        ).also{ filteredImageIntent->
            filteredImageIntent.putExtra(CameraActivity.KEY_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)
        }
    }
}