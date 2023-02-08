package pt.ipt.finalproject.repositories

import android.graphics.Bitmap
import android.net.Uri

//repository interface for images
interface EditImageRepository  {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun saveEditImage(editedBitmap: Bitmap): Uri?
}