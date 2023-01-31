package pt.ipt.finalproject.repositories

import android.graphics.Bitmap
import android.net.Uri
//import pt.ipt.photoredactor.data.ImageFilter

interface EditImageRepository  {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
  //  suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?
}