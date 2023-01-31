package pt.ipt.finalproject.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.finalproject.utilities.Coroutines
//import pt.ipt.photoredactor.data.ImageFilter
import pt.ipt.finalproject.repositories.EditImageRepository


class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {

    //region:: Підготовка перегляду фото
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUiState(bitmap = bitmap)
                } else {
                    emitImagePreviewUiState(error = "Unable to preview image.")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )

    //endregion

//    region:: Завантажуємо фільтри
//    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
//    val imageFiltersUiState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState
//
//    fun loadImageFilters(originalImage: Bitmap) {
//        Coroutines.io {
//            kotlin.runCatching {
//                emitImageFiltersUiState(isLoading = true)
//                editImageRepository.getImageFilters(getPreviewImage(originalImage))
//            }.onSuccess { imageFilters ->
//                emitImageFiltersUiState(imageFilters = imageFilters)
//            }.onFailure {
//                emitImageFiltersUiState(error = it.message.toString())
//            }
//        }
//    }

     fun loadImage(originalImage: Bitmap): Bitmap? {
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    /////////////
//    private fun emitImageFiltersUiState(
//        isLoading: Boolean = false,
//        imageFilters: List<ImageFilter>? = null,
//        error: String? = null
//    ) {
//        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
//        imageFiltersDataState.postValue(dataState)
//    }
//
//    data class ImageFiltersDataState(
//        val isLoading: Boolean,
//        val imageFilters: List<ImageFilter>?,
//        val error: String?
//    )
    //endregion

    //region:: Зберігаємо відредаговані фото
    private val saveFilteredImageDataState= MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveEditImage(editedBitmap: Bitmap){
        Coroutines.io{
            runCatching{
                emitSaveEditImageUiState(isLoading = true)
                editImageRepository.saveEditImage(editedBitmap)
            }.onSuccess { savedImageUri ->
                emitSaveEditImageUiState(uri = savedImageUri)
            }.onFailure {
                emitSaveEditImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveEditImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion


}