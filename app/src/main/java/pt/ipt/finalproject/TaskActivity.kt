package pt.ipt.finalproject

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.finalproject.databinding.TasksLayoutBinding
import android.widget.GridLayout

class TaskActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PictureAdapter
    private lateinit var binding: TasksLayoutBinding

    private val imageNames: Array<String> by lazy {
        resources.getStringArray(R.array.image_names)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TasksLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayout: GridLayout = findViewById(R.id.gridLayout)

        val imageResourceIds = listOf(
            R.drawable.ballet,
            R.drawable.bikini,
            R.drawable.chilling,
            R.drawable.coffee,
            R.drawable.dance,
            R.drawable.dancing,
            R.drawable.doggie,
            R.drawable.dogjump,
            R.drawable.icecream,
            R.drawable.jumping,
            R.drawable.laying,
            R.drawable.levitate,
            R.drawable.loving,
            R.drawable.meditating,
            R.drawable.messy,
            R.drawable.moshing,
            R.drawable.petting,
            R.drawable.plant,
            R.drawable.reading,
            R.drawable.readingside,
            R.drawable.rollerskating,
            R.drawable.rolling,
            R.drawable.running,
            R.drawable.selfie,
            R.drawable.sitting,
            R.drawable.sittingreading,
            R.drawable.sleek,
            R.drawable.sprinting,
            R.drawable.strolling,
            R.drawable.swinging,
            R.drawable.unboxing,
            R.drawable.zombieing
            // Add more drawable resource IDs as needed
        )

        val columnCount = 4 // Number of columns in the grid

        val taskDescArray = resources.getStringArray(R.array.task_desc)

        val imageResources = imageResourceIds.mapIndexedNotNull { index, resourceId ->
            val description = taskDescArray.getOrNull(index)
            if (description != null) {
                ImageData(resourceId, description)
            } else {
                null
            }
        }

        for (i in imageResources.indices) {
            val imageView = ImageView(this)
            val layoutParams = GridLayout.LayoutParams().apply {
                width = 220
                height = 220
                columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f) // Equal width for each column
            }
            imageView.layoutParams = layoutParams

            val imageResource = imageResources[i].resourceId
            val description = imageResources[i].description

            imageView.setImageResource(imageResource)
            imageView.contentDescription = description
            imageView.setOnClickListener {
                AlertDialog.Builder(this, R.style.AlertDialog)
                    //.setView(dialogView)
                    .setTitle("Task description")
                    .setMessage(description)
                    .setPositiveButton("Let's go") { _, _ ->
                        showToast("Task selected!")
                    }
                    .setNegativeButton("Nope") { _, _ ->
                        showToast("You can choose another task.")
                    }
                    .show()
            }

            gridLayout.addView(imageView)
        }


       /** for (pair in imageResources) {
            val resourceId = pair
            val imageView = ImageView(this)
            val layoutParams = GridLayout.LayoutParams().apply {
                width = 210
                height = 210
                columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f) // Equal width for each column
            }
            imageView.layoutParams = layoutParams
            imageView.setImageResource(resourceId)

            imageView.setOnClickListener {
                AlertDialog.Builder(this)
                    //.setView(dialogView)
                    .setTitle("Task description")
                    .setMessage(description)
                    .setPositiveButton("Yes") { _, _ ->
                        showToast("Task selected!")
                    }
                    .setNegativeButton("No") { _, _ ->
                        showToast("You can choose another task.")
                    }
                    .show()
            }
            gridLayout.addView(imageView)
        }**/

        /**adapter = ImageAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Adjust the number of columns as per your requirement

        adapter.setOnItemClickListener { position ->
        val selectedImage = adapter.getImage(position)
        val imageName = imageNames[position]
        val imageDescription = getString(resources.getIdentifier(imageName, "string", packageName))
        showConfirmationDialog(selectedImage, imageName, imageDescription)
        }

        loadImages()
        }

        private fun loadImages() {
        for (imageName in imageNames) {
        val resourceId = resources.getIdentifier(imageName, "drawable/pics", packageName)
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        adapter.addImage(bitmap)
        }
        }

        private fun showConfirmationDialog(image: Bitmap, imageName: String, imageDescription: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val textView = dialogView.findViewById<TextView>(R.id.dialogTextView)

        imageView.setImageBitmap(image)
        textView.text = imageDescription

        AlertDialog.Builder(this)
        .setView(dialogView)
        .setTitle("Confirmation")
        .setMessage("Do you want to select this image?")
        .setPositiveButton("Yes") { _, _ ->
        showToast("Image selected!")
        }
        .setNegativeButton("No") { _, _ ->
        showToast("Image not selected.")
        }
        .show()
        }
         **/

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
data class ImageData(val resourceId: Int, val description: String)
