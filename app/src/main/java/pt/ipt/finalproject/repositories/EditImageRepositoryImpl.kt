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

    //Збережені фільтри
//    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
//        val gpuImage = GPUImage(context).apply {
//            setImage(image)
//        }
//        val imageFilters: ArrayList<ImageFilter> = ArrayList()
//        //region::Image Filters
//        //Normal
//        GPUImageFilter().also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Normal",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Btight
//        GPUImageRGBFilter(1.1f, 1.3f, 1.6f).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Bright",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Wole
//        GPUImageSaturationFilter(2.0f).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Wole",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Sepia
//        GPUImageSepiaToneFilter().also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Sepia",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Retro
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.2f, 0.0f,
//                0.1f, 0.1f, 1.0f, 0.0f,
//                1.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Hume",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Just
//        GPUImageColorMatrixFilter(
//            0.0f,
//            floatArrayOf(
//                0.4f, 0.6f, 0.5f, 0.0f,
//                0.0f, 0.4f, 1.0f, 0.0f,
//                0.05f, 0.1f, 0.4f, 0.4f,
//                1.0f, 1.0f, 1.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Just",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Hume
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.25f, 0.0f, 0.2f, 0.0f,
//                0.0f, 1.0f, 0.2f, 0.0f,
//                0.0f, 0.3f, 1.0f, 0.3f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Hume",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Desert
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.6f, 0.4f, 0.2f, 0.05f,
//                0.0f, 0.8f, 0.3f, 0.05f,
//                0.3f, 0.3f, 0.5f, 0.08f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Desert",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Vintage
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.0f, 0.05f, 0.0f, 0.0f,
//                -0.2f, 1.1f, -0.2f, 0.11f,
//                0.2f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Vintage",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Limo
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.0f, 0.0f, 0.08f, 0.0f,
//                0.4f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 1.0f, 0.1f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Limo",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Solar
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.5f, 0.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Solar",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Neutron
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.6f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Neutron",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Milk
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.64f, 0.5f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Milk",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Muli
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.0f, 0.0f, 0.0f, 0.0f,
//                1.0f, 0.0f, 0.0f, 0.0f,
//                1.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Muli",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Mill
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.64f, 0.5f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Milk",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //BW
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "B&W",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Clue
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Clue",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Aero
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 0.0f, 1.0f, 0.0f,
//                1.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 1.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Aero",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Classic
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.763f, 0.0f, 0.2062f, 0.0f,
//                1.0f, 0.9416f, 0.0f, 0.0f,
//                0.1623f, 0.2614f, 0.8852f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Classic",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Atom
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.5162f, 0.3799f, 0.3247f, 0.0f,
//                0.039f, 1.0f, 0.0f, 0.0f,
//                -0.4773f, 0.461f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Atom",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Mars
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                0.0f, 0.0f, 0.5183f, 0.3183f,
//                0.0f, 0.5497f, 0.5416f, 0.0f,
//                0.5237f, 0.5269f, 0.0f, 0.0f,
//                1.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Mars",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//        //Yeli
//        GPUImageColorMatrixFilter(
//            1.0f,
//            floatArrayOf(
//                1.0f, 0.3831f, 0.3883f, 0.0f,
//                0.0f, 1.0f, 0.2f, 0.0f,
//                -0.1961f, 0.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 1.0f
//            )
//        ).also { filter ->
//            gpuImage.setFilter(filter)
//            imageFilters.add(
//                ImageFilter(
//                    name = "Yeli",
//                    filter = filter,
//                    filterPreview = gpuImage.bitmapWithFilterApplied
//                )
//            )
//        }
//
//
//
//
//        return imageFilters
//    }

    //Завантаження оригінального фото
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

    //Збереження відредагованого фото в папку зі зберережиними фото
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun saveEditImage(editedBitmap: Bitmap, textEmotions: String): Uri? {
        return try {
            val mediaStorageDirectory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
                //MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Saved"
            )
            if(!mediaStorageDirectory.exists()){
                mediaStorageDirectory.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
           // val fileName = "IMG_${textEmotions}.jpg"
            val file = File(mediaStorageDirectory, fileName)
            saveFile(file, editedBitmap, textEmotions)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (exception: Exception){
            null
        }
    }

    private fun saveFile(file: File, bitmap: Bitmap, text: String){
        with(FileOutputStream(file)){

          //  val canvas = Canvas(bitmap)
           // canvas.drawBitmap(bitmap, 0f, 0f, null)
          //  val paint = Paint()
           // paint.setColor(Color.RED)
            //canvas.drawText(text, 0f, 0f, paint)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
          //  file.writeText(text)
            flush()
            close()
        }
    }

}