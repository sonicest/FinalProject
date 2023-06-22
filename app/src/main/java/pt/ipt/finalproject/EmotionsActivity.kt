package pt.ipt.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_chosen_photo.emSpinner
import kotlinx.android.synthetic.main.activity_chosen_photo.inputEmotions
import kotlinx.android.synthetic.main.activity_emotions.inputDesc
import pt.ipt.finalproject.databinding.ActivityEmotionsBinding
import pt.ipt.finalproject.databinding.TasksLayoutBinding
import pt.ipt.finalproject.utilities.Constant
import pt.ipt.finalproject.utilities.Constant.Companion.helper
import pt.ipt.finalproject.utilities.displayToast
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class EmotionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmotionsBinding
    private lateinit var adapter: ImageAdapter
    private lateinit var nameEmotions: String
    var count = 0
    var typeId = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupEmoji()
        setupSpinner()
        setupDesc()
        setListeners()

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.inputEmotions)


        val res = resources
        val resId = res.getIdentifier("emotions", "array", packageName)
        val emotions = res.getStringArray(resId)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            emotions
        )
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun setupDesc() {
        val editText = findViewById<EditText>(R.id.inputDesc)
        editText.addTextChangedListener { text ->
            val textString = text.toString()
            // Save the text to a variable or a database
        }
    }

    private fun setupEmoji() {
        val gridLayout: GridLayout = findViewById(R.id.gridLayout1)

        val imageResourceIds = listOf(
            R.drawable.happy,
            R.drawable.good,
            R.drawable.okay,
            R.drawable.sad,
            R.drawable.verysad,
        )

        val taskDescArray = resources.getStringArray(R.array.emo_desc)

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
                width = 130
                height = 130
                columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f) // Equal width for each column
            }
            imageView.layoutParams = layoutParams
            val typeEmotions = imageResources[i].resourceId


            val imageResource = imageResources[i].resourceId
            val description = imageResources[i].description

            imageView.setImageResource(imageResource)
            imageView.contentDescription = description

            imageView.setOnClickListener {


                if (count > 0) {
                    println(count)
                    imageView.isClickable = false
                } else {
                    count = count + 1
                    if (typeEmotions == 2131230890)
                        typeId = 5
                    else if (typeEmotions == 2131230887)
                        typeId = 4
                    else if (typeEmotions == 2131231008)
                        typeId = 3
                    else if (typeEmotions == 2131231024)
                        typeId = 2
                    else if (typeEmotions == 2131231039)
                        typeId = 1

                    println(typeEmotions)
                }
                /** Додавання обраного до бази даних для графіку**/
            }


            gridLayout.addView(imageView)
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
                nameEmotions = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun collectEmotions() {
        // val description = inputDesc.text.toString().trim()
        val emotions = inputEmotions.text.toString().trim()
        if (emotions.isEmpty()) {
            displayToast("Please try to distinguish your emotions")
        } else if (typeId < 1) {
            displayToast("Please try to distinguish your general mood")
        } else {
          //  val date: String =
           //     SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentDate = LocalDate.now()

            // Get the day of the week as a string
            val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

            // Print the day of the week
            println(dayOfWeek)
            helper.saveEmotions(
                typeId,
                emotions,
                dayOfWeek
            )
            Intent(applicationContext, MainActivity::class.java).also {
                startActivity(it)
            }
            showToast("Well done!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        binding.save.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                type = "image/*"
                collectEmotions()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        return msg.trim().isEmpty()
    }

    private fun isUriEmpty(uri: Uri?): Boolean {
        return uri == null || uri == Uri.EMPTY
    }

}