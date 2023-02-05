package pt.ipt.finalproject.repositories

//import pt.ipt.photoredactor.data.ImageFilter

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class EditImageRepositoryImpl(private val context: Context) : EditImageRepository {

    //Upload photo
    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height * width) / originalBitmap.width)
            val matrix = Matrix()
            matrix.postRotate(90F)
            val rotatedBitmap = Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
            return Bitmap.createScaledBitmap(rotatedBitmap, height, width, false)

        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun saveEditImage(editedBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirectory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
            )
            if (!mediaStorageDirectory.exists()) {
                mediaStorageDirectory.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(mediaStorageDirectory, fileName)
            saveFile(file, editedBitmap)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (exception: Exception) {
            null
        }
    }

    private fun saveFile(file: File, bitmap: Bitmap) {
        with(FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
    }

}