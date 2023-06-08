package pt.ipt.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_chosen_photo.emSpinner
import kotlinx.android.synthetic.main.activity_chosen_photo.inputEmotions
import pt.ipt.finalproject.databinding.ActivityEmotionsBinding
import pt.ipt.finalproject.databinding.TasksLayoutBinding

class EmotionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmotionsBinding
    private lateinit var adapter: ImageAdapter
    private lateinit var nameEmotions: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEmoji()
        setupSpinner()
        setupDesc()

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.inputEmotions)

        val res = resources
        val resId = res.getIdentifier("emotions", "array", packageName)
        val emotions = res.getStringArray(resId)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            emotions)
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

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        return msg.trim().isEmpty()
    }

    private fun isUriEmpty(uri: Uri?): Boolean {
        return uri == null || uri == Uri.EMPTY
    }

}