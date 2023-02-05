package pt.ipt.finalproject.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.finalproject.utilities.Coroutines
import pt.ipt.finalproject.repositories.EditImageRepository


class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {

    //region:: Preview
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

     fun loadImage(originalImage: Bitmap): Bitmap? {
        return runCatching {
            val previewWidth = originalImage.width
            val previewHeight = originalImage.height //* previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    //region:: Saving
    private val saveEditedImageDataState= MutableLiveData<SaveEditedImageDataState>()
    val saveEditedImageUiState: LiveData<SaveEditedImageDataState> get() = saveEditedImageDataState

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
        val dataState = SaveEditedImageDataState(isLoading, uri, error)
        saveEditedImageDataState.postValue(dataState)
    }

    data class SaveEditedImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion


}